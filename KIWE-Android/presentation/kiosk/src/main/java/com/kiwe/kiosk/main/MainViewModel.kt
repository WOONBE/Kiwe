package com.kiwe.kiosk.main

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.kiwe.domain.model.AnswerType
import com.kiwe.domain.model.Category
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.model.Option
import com.kiwe.domain.model.OrderList
import com.kiwe.domain.model.VoiceBody
import com.kiwe.domain.model.VoiceOrderRequest
import com.kiwe.domain.model.VoiceRecommendRequest
import com.kiwe.domain.usecase.VoiceOrderUseCase
import com.kiwe.domain.usecase.VoiceRecommendUseCase
import com.kiwe.domain.usecase.kiosk.datasource.ClearAllDataUseCase
import com.kiwe.domain.usecase.manager.kiosk.SignOutKioskUseCase
import com.kiwe.domain.usecase.manager.menu.GetMenuByIdUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.ui.screen.utils.SpeechRecognizerManager
import com.kiwe.kiosk.ui.screen.utils.SpeechResultListener
import com.kiwe.kiosk.ui.screen.utils.TEXT_CHECK_TEMPERATURE
import com.kiwe.kiosk.ui.screen.utils.TEXT_INTRO
import com.kiwe.kiosk.ui.screen.utils.TEXT_INTRO_HELP
import com.kiwe.kiosk.ui.screen.utils.TEXT_MENU_RECOMMENDATION
import com.kiwe.kiosk.ui.screen.utils.TEXT_MORE_ORDER
import com.kiwe.kiosk.ui.screen.utils.TEXT_PAYMENT
import com.kiwe.kiosk.ui.screen.utils.TEXT_PAYMENT_DONE
import com.kiwe.kiosk.ui.screen.utils.TEXT_TOGO
import com.kiwe.kiosk.ui.screen.utils.TEXT_TOGO1
import com.kiwe.kiosk.ui.screen.utils.TEXT_TOGO2
import com.kiwe.kiosk.ui.screen.utils.TEXT_TOGO3
import com.kiwe.kiosk.ui.screen.utils.TextToSpeechManager
import com.kiwe.kiosk.ui.screen.utils.helpPopupRegex
import com.kiwe.kiosk.ui.screen.utils.menuRegex
import com.kiwe.kiosk.ui.screen.utils.noRegex
import com.kiwe.kiosk.ui.screen.utils.orderRegex
import com.kiwe.kiosk.ui.screen.utils.payRegex
import com.kiwe.kiosk.ui.screen.utils.recommendRegex
import com.kiwe.kiosk.ui.screen.utils.temperatureRegex
import com.kiwe.kiosk.ui.screen.utils.yesRegex
import com.kiwe.kiosk.utils.MainEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val speechRecognizerManager: SpeechRecognizerManager,
    private val textToSpeechManager: TextToSpeechManager,
    private val voiceOrderUseCase: VoiceOrderUseCase,
    private val voiceRecommendUseCase: VoiceRecommendUseCase,
    private val getMenuByIdUseCase: GetMenuByIdUseCase,
    private val signOutKioskUseCase: SignOutKioskUseCase,
    private val clearAllDataUseCase: ClearAllDataUseCase,
) : BaseViewModel<MainState, MainSideEffect>(MainState()),
    SpeechResultListener {
    private var personDetectedRecently = false
    private val delayTime = TimeUnit.MINUTES.toMillis(5)
    private var timerJob: Job? = null
    private var lastTtsText: String = ""

    override fun handleExceptionIntent(
        coroutineContext: CoroutineContext,
        throwable: Throwable,
    ) {
        intent {
            postSideEffect(MainSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
        }
    }

    init {
        getMenuCategory()
        initSpeechRecognizer()
        initTTSListener()
    }

    private fun initSpeechRecognizer() {
        speechRecognizerManager.setSpeechResultListener(this)
    }

    private fun initTTSListener() {
        textToSpeechManager.setOnCompleteListener {
            // tts가 끝나면 stt 살리게 했음
            intent {
                reduce {
                    Timber.tag("MainViewModel").d("tts 종료 인지")
                    state.copy(isTtsOn = false)
                }
                delay(1000L)
                startSpeechRecognition()
            }
        }
        textToSpeechManager.setOnStartListener { lastText ->
            intent {
                reduce {
                    Timber.tag("MainViewModel").d("tts 시작 인지 - speech 멈춤")
                    state.copy(isTtsOn = true)
                }
                // tts가 시작되면 stt를 끊는다
                speechRecognizerManager.stopListening()
                lastTtsText = lastText // 마지막에 재생된 tts 저장해두기
            }
        }
    }

    private fun removeOverlap(
        sttText: String,
        ttsText: String,
    ): String {

        // TTS 문구 패턴에 해당하는 텍스트 목록을 정의합니다.
        val ttsPatterns = listOf(
            TEXT_INTRO,
            TEXT_INTRO_HELP,
            "주문을 추가하였습니다",
            "온도를 확인해주세요.",
            "차가운 거로 드릴까요  따뜻한 거로 드릴까요?",
            TEXT_TOGO,
            TEXT_TOGO1,
            TEXT_TOGO2,
            TEXT_TOGO3,
            "매장에서 이용하시나요",
            TEXT_PAYMENT,
            TEXT_PAYMENT_DONE,
            TEXT_CHECK_TEMPERATURE,
            TEXT_MENU_RECOMMENDATION,
            TEXT_MORE_ORDER,
        )

        // TTS 패턴과 sttText가 부분적으로 일치하는지 확인
        val matchingPattern = ttsPatterns.firstOrNull { pattern ->
            normalizeText(sttText).contains(normalizeText(pattern))
        }

        // TTS 패턴이 포함된 경우 해당 패턴을 제거
        if (matchingPattern != null) {
            // 매칭된 패턴의 위치를 찾아서 해당 부분을 제거
            val normalizedPattern = normalizeText(matchingPattern)
            val normalizedSttText = normalizeText(sttText)
            val startIndex = normalizedSttText.indexOf(normalizedPattern)

            if (startIndex != -1) {
                val originalStartIndex = getOriginalIndex(sttText, normalizedSttText, startIndex)
                val originalEndIndex = getOriginalIndex(sttText, normalizedSttText, startIndex + normalizedPattern.length)

                // 실제 sttText에서 일치하는 부분 제거
                val filteredText = (sttText.substring(0, originalStartIndex) + sttText.substring(originalEndIndex)).trim()
                Timber.tag("MainViewModel").d("$ttsText || TTS 패턴이 포함되어 있습니다. $filteredText")
                return filteredText
            }
        }
        return sttText

//        val normalizedSttText = normalizeText(sttText)
//        val normalizedTtsText = normalizeText(ttsText)
//
//        val index = normalizedSttText.indexOf(normalizedTtsText)
//        return if (index != -1) {
//            val startIndex = getOriginalIndex(sttText, normalizedSttText, index)
//            val endIndex =
//                getOriginalIndex(sttText, normalizedSttText, index + normalizedTtsText.length)
//
//            val beforeTts = sttText.substring(0, startIndex)
//            val afterTts = sttText.substring(endIndex)
//            (beforeTts + afterTts).trim()
//        } else {
//            sttText
//        }
    }

    private fun normalizeText(text: String): String {
        return text
            .replace("\\s+".toRegex(), "") // 모든 공백 제거
            .replace("[^\\p{L}\\p{N}]".toRegex(), "") // 모든 문장 부호 제거
    }

    private fun getOriginalIndex(
        originalText: String,
        normalizedText: String,
        targetNormalizedIndex: Int,
    ): Int {
        var originalIndex = 0
        var normalizedIndex = 0

        while (originalIndex < originalText.length && normalizedIndex < targetNormalizedIndex) {
            val c = originalText[originalIndex]
            if (!c.isWhitespace() && !c.isPunctuation()) {
                // 원본 텍스트의 문자가 공백이나 문장 부호가 아니면
                if (normalizedText[normalizedIndex] == c) {
                    // 문자가 일치하면 normalizedIndex 증가
                    normalizedIndex++
                }
                // 일치하지 않더라도 originalIndex 증가
            }
            originalIndex++
        }
        return originalIndex
    }

    private fun Char.isPunctuation(): Boolean {
        val type = Character.getType(this)
        return type == Character.CONNECTOR_PUNCTUATION.toInt() ||
                type == Character.DASH_PUNCTUATION.toInt() ||
                type == Character.END_PUNCTUATION.toInt() ||
                type == Character.FINAL_QUOTE_PUNCTUATION.toInt() ||
                type == Character.INITIAL_QUOTE_PUNCTUATION.toInt() ||
                type == Character.OTHER_PUNCTUATION.toInt() ||
                type == Character.START_PUNCTUATION.toInt()
    }

    // 음성 인식 시작
    fun startSpeechRecognition() {
        speechRecognizerManager.startListening()
        Timber.tag("MainViewModel").d("startSpeechRecognition")
    }

    // 음성 인식 중단
    fun stopSpeechRecognition() {
        intent {
            reduce { state.copy(isScreenShowing = false) }
            speechRecognizerManager.stopListening()
        }
    }

    private fun setMySpeechInput(mySentence: String) { // text 처리만 수행한다
        intent {
            reduce {
                state.copy(mySpeechText = mySentence) // 이전 글자를 지우고
            }
        }
    }

    override fun onResultsReceived(results: List<String>) {
        val resultText = results.firstOrNull() ?: ""
        intent {
            if (!state.isTtsOn) { // when tts off
                val cleanedResultText = removeOverlap(resultText.trim(), lastTtsText.trim())
                if (cleanedResultText.isNotEmpty()) {
                    Timber.tag("MainViewModel").d("필터링 된놈 $cleanedResultText")
                    if (!state.isScreenShowing && helpPopupRegex.containsMatchIn(resultText) && !state.isPayment && !state.isCartOpen) {
                        setMySpeechInput(resultText)
                        textToSpeechManager.speak(TEXT_INTRO_HELP)
                        reduce {
                            state.copy(isScreenShowing = true)
                        }
                    } else {
                        setMySpeechInput(resultText)
                        delay(2000L) // 2초 대기
                        if (state.isScreenShowing) { // SpeechScreen여부
                            onSpeechResult(resultText)
                        } else {
                            // 여기서 로직 문제가 생겼음
                            Timber.tag("추천췍").d("$resultText  ${state.isCartOpen}")
                            if (state.isCartOpen) {
                                onProcessResult(resultText)
                            } else {
                                onPayProcess(resultText)
                                onRecommendProcess(resultText)
                            }
                        }
                    }
                } else {
                    Timber.tag("MainViewModel").d("TTS 음성만 인식되어 결과를 무시합니다.")
                }
            }
            Timber
                .tag("MainViewModel")
                .d("Result: $resultText 하고 ${state.isScreenShowing}이고, tts는 ${state.isTtsOn}")
        }
    }

    fun speakPayment() {
        textToSpeechManager.speak(TEXT_PAYMENT)
    }

    fun speakPaymentDone() {
        textToSpeechManager.speak(TEXT_PAYMENT_DONE)
    }

    fun onDismissRequest() =
        intent {
            reduce {
                state.copy(isScreenShowing = false)
            }
        }

    fun onDetectAgeGender(
        age: Int,
        gender: String,
    ) {
        // 나이랑 성별 받아오기
        intent {
            reduce {
                state.copy(age = age, gender = gender)
            }
        }
        Timber.tag("MainViewModel").d("age: $age, gender: $gender")
    }

    fun clearVoiceRecord() =
        intent {
            reduce {
                state.copy(
                    tempOrder = "",
                    isTemperatureEmpty = false,
                    isRecommend = "",
                    voiceResult =
                    VoiceBody(
                        category = 0,
                        need_temp = 1,
                        order = emptyList(),
                        message = "",
                        response = "",
                    ),
                    isScreenShowing = false,
//                        voiceShoppingCart = emptyList() // 어디에 넣을지 생각하자
                )
            }
        }

    fun showSpeechScreen() =
        intent {
            reduce {
                state.copy(
                    isScreenShowing = true,
                )
            }
        }

    fun closeSpeechScreen() =
        intent {
            reduce {
                state.copy(
                    isScreenShowing = false,
                )
            }
        }

    fun clearOrderEndState() {
        intent {
            reduce {
                state.copy(
                    isOrderEndTrue = false,
                    isOrderEndFalse = false,
                )
            }
        }
    }

    fun clearRecommendHistory() {
        intent {
            reduce {
                state.copy(
                    isRecommend = "",
                    isAddCartFalse = false,
                    isAddCartTrue = false,
                )
            }
        }
    }

    fun openShoppingCart() =
        intent {
            reduce {
                state.copy(
                    isCartOpen = true, // 기본값은 state
                )
            }
        }

    fun closeShoppingCart() =
        intent {
            reduce {
                state.copy(
                    isCartOpen = false, // 기본값은 state
                )
            }
        }

    fun clearMySpeech() {
        intent {
            reduce {
                state.copy(mySpeechText = "")
            }
        }
    }

    private fun onProcessResult(result: String) =
        intent {
            // 음성 주문을 했고, 장바구니 카트에 담겨있다면
            // 계속 주문할까요?
            if (state.voiceShoppingCart.isNotEmpty()) {
                if (noRegex.containsMatchIn(result)) {
                    reduce { state.copy(mySpeechText = "") }
                    // 부정
                    textToSpeechManager.speak(TEXT_TOGO)
                    reduce { state.copy(isOrderEndTrue = true) }
                }
                if (yesRegex.containsMatchIn(result)) {
                    // 긍정
                    reduce { state.copy(mySpeechText = "") }
                    textToSpeechManager.speak(TEXT_INTRO_HELP)
                    reduce { state.copy(isOrderEndFalse = true) }
                }
            }
        }

    private fun onPayProcess(result: String) {
        intent {
            if (state.page == 2 && payRegex.containsMatchIn(result)) {
                reduce { state.copy(isPayment = true) }
            }
        }
    }

    private fun onRecommendProcess(result: String) {
        intent {
            if (state.isRecommend.isNotEmpty()) {
                Timber.tag("추천").d("$result")
                if (noRegex.containsMatchIn(result)) {
                    reduce {
                        state.copy(
                            isRecommend = "",
                            voiceResult =
                            VoiceBody(
                                category = 1,
                                need_temp = 1,
                                order = state.voiceShoppingCart,
                                message = "",
                                response = "",
                            ),
                            isScreenShowing = false,
                            isAddCartFalse = true,
                        )
                    }
                    textToSpeechManager.speak(TEXT_MORE_ORDER)
                }
                if (yesRegex.containsMatchIn(result)) {
                    val recommendVoiceBody =
                        VoiceBody(
                            category = AnswerType.SUGGESTION.value,
                            need_temp = 1,
                            order =
                            state.voiceShoppingCart +
                                    listOf(
                                        OrderList(
                                            state.recommendMenu.id,
                                            1,
                                            Option(shot = 0, sugar = 0),
                                        ),
                                    ),
                            message = "",
                            response = "주문을 추가하였습니다",
                        )
                    Timber.tag("추천").d("$recommendVoiceBody")
                    reduce {
                        state.copy(
                            isAddCartTrue = true,
                            voiceResult = recommendVoiceBody,
                            voiceShoppingCart = recommendVoiceBody.order,
                        )
                    }
                    textToSpeechManager.speak(TEXT_MORE_ORDER)
                }
            }
        }
    }

    fun clearPaymentProcess() {
        intent {
            reduce {
                state.copy(isPayment = false)
            }
        }
    }

    private fun onSpeechResult(result: String) =
        intent {
            if (state.tempOrder.isNotEmpty() && state.isTemperatureEmpty) { // 온도 선택을 안한 경우
                // 저장된 문장이 있다는 것은 진행중인 음성 주문이 있다는 것
                // 온도 관련 정규식 때려서 검사하고 주문 넣기
                if (temperatureRegex.containsMatchIn(result)) {
                    Timber.tag("MainViewModel").d("tempOrder: $result")
                    voiceOrderUseCase(
                        voiceOrder =
                        VoiceOrderRequest(
                            sentence = result + " " + state.tempOrder, // 기존 주문 문장에 넣고 진행
                            need_temp = 1, // hot, ice 정보가 있으면 1로 들어가야함
                            order_items = state.voiceShoppingCart, // 빈 장바구니를 의미함(기존에 있는게 있으면 그걸 넣으면 됨)
                        ),
                    ).onSuccess { res ->
                        val it = res.data
                        textToSpeechManager.speak(it.response + "." + TEXT_MORE_ORDER)
                        // 온도까지 잘 들어가 있다면
                        reduce {
                            state.copy(
                                voiceShoppingCart = it.order,
                                recognizedText = "",
                                voiceResult = it,
                                shouldShowRetryMessage = false,
                                isScreenShowing = false,
                            )
                        }
                    }
                }
            }
            if (menuRegex.containsMatchIn(result)) { // 메뉴판에 있는 메뉴를 말했다면
                Timber.tag("MainViewModel").d("menuRegex: $result")
                if (orderRegex.containsMatchIn(result)) { // 정해둔 폼으로 문장이 끝나는 지 검사
                    Timber.tag("Network").d("orderRegex: $result")
                    val modifiedResult = orderRegex.replace(result, " 주세요")
                    val removeHelpResult = helpPopupRegex.replace(modifiedResult, "")
                    voiceOrderUseCase(
                        voiceOrder =
                        VoiceOrderRequest(
                            sentence = removeHelpResult,
                            need_temp = 1, // hot, ice 정보가 있으면 1로 들어가야함
                            order_items = state.voiceShoppingCart, // 빈 장바구니를 의미함(기존에 있는게 있으면 그걸 넣으면 됨)
                        ),
                    ).onSuccess { res ->
                        // AI에서 성공해서 응답이 돌아오면
                        val it = res.data
                        if (it.response == "잘못된 접근입니다.") {
                            postSideEffect(MainSideEffect.Toast(it.response))
                            reduce {
                                state.copy(
                                    shouldShowRetryMessage = true,
                                )
                            }
                        } else {
                            if (it.response.contains("온도를 확인해주세요")) {
                                textToSpeechManager.speak(TEXT_CHECK_TEMPERATURE)
                                Timber.tag("temp").d("${it.response}")
                                val listPart =
                                    it.response.substringAfter("[").substringBefore("]")
                                val itemList = listPart.replace("'", "").split(", ")
                                itemList // 지금은 단일로 할 것 같긴 함
                                reduce {
                                    state.copy(
                                        tempOrder = removeHelpResult,
                                        isTemperatureEmpty = true,
                                    )
                                }
                            } else {
                                textToSpeechManager.speak(it.response + "." + TEXT_MORE_ORDER)
                                reduce {
                                    state.copy(
                                        voiceShoppingCart = it.order,
                                        recognizedText = "",
                                        voiceResult = it,
                                        shouldShowRetryMessage = false,
                                        isScreenShowing = false,
                                    )
                                }
                                Timber
                                    .tag("MainViewModel")
                                    .d("voiceResult: ${state.voiceShoppingCart}")
                            }
                        }
                    }.onFailure {
                        postSideEffect(MainSideEffect.Toast("오류가 발생했습니다"))
                        reduce {
                            state.copy(
                                shouldShowRetryMessage = true,
                                isScreenShowing = false,
                            )
                        }
                    }
                }
            } else if (recommendRegex.containsMatchIn(result)) { // 도와줘 이후에 이용가능
                voiceRecommendUseCase(
                    VoiceRecommendRequest(
                        age = 20,
                        sentence = result,
                    ),
                ).onSuccess { response ->
                    val numberListRegex = "\\[(.*?)]".toRegex()
                    val numberListMatch = numberListRegex.find(response.message)
                    val menuList =
                        numberListMatch
                            ?.groups
                            ?.get(1)
                            ?.value
                            ?.split(", ")
                            ?.map { it.toInt() }
                    val shuffledList = menuList!!.shuffled()
                    val requestMenuId = shuffledList[0]
                    val secondMenuId = shuffledList.getOrNull(1)
                    val thirdMenuId = shuffledList.getOrNull(2)
                    val menu = getMenuByIdUseCase(requestMenuId).getOrThrow()
                    textToSpeechManager.speak(TEXT_MENU_RECOMMENDATION + menu.name + "을 주문하시겠습니까?")
                    val subRecommendMenus =
                        listOfNotNull(
                            secondMenuId?.let { getMenuByIdUseCase(it).getOrNull() },
                            thirdMenuId?.let { getMenuByIdUseCase(it).getOrNull() },
                        )
                    Timber.tag("추천").d("$menu")
                    reduce {
                        state.copy(
                            isScreenShowing = false,
                            isRecommend = menu.description,
                            recommendMenu = menu,
                            subRecommendMenu = subRecommendMenus,
                        )
                    }
                }
            } else {
                reduce {
                    state.copy(
                        recognizedText = "",
                        shouldShowRetryMessage = true, // 다시 말해달라는 flag
                    )
                }
            }
        }

    override fun onPartialResultsReceived(partialResults: List<String>) {
        Timber.tag("MainViewModel").d("Partial Result Outer: $partialResults")
        intent {
            if (!state.isTtsOn) { // tts가 꺼져있을 때 들어오도록
                val partialText = partialResults.firstOrNull() ?: ""
                Timber.tag("MainViewModel").d("Partial Result: $partialResults")
                reduce { state.copy(isMySpeechInputTextOpen = true) }
                if (state.isScreenShowing) {
                    // 다이얼로그가 보여지고 있는 상황이라면
                } else if (helpPopupRegex.containsMatchIn(partialText)) {
                    // 다이얼로그가 안보이는 상황에서 음성이 들어온다면 이걸 탐
                    Timber
                        .tag("MainViewModel")
                        .d("${helpPopupRegex.containsMatchIn(partialText)}")
                    reduce {
                        state.copy(isScreenShowing = true)
                    }
                }
            }
        }
    }

    fun getMenuCategory() =
        intent {
            val category =
                listOf(
                    Category(
                        "커피",
                        "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                    ),
                    Category(
                        "음료",
                        "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                    ),
                    Category(
                        "간식",
                        "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                    ),
                    Category(
                        "추천",
                        "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                    ),
                )
            reduce {
                state.copy(category = category)
            }
        }

    fun setPage(page: Int) =
        intent {
            reduce {
                if (page == 1) {
                    textToSpeechManager.speak(TEXT_INTRO)
                }
                state.copy(
                    page = page,
                )
            }
        }

    fun detectPerson(box: Boolean) {
        if (box) {
            if (!personDetectedRecently) {
                onStartKiosk()
//                    onPersonCome()
//                    startPersonDetectionCooldown()
            }
        } else {
            if (!personDetectedRecently) {
                onPersonLeave()
            }
        }
    }

    /**
     * kiosk 모드 시작
     */
    fun onStartKiosk() {
        onPersonCome()
        startTimer()
        personDetectedRecently = true
    }

    private fun startTimer() {
        // 기존 타이머가 있다면 취소하고 새로운 타이머 시작
        timerJob?.cancel()
        timerJob =
            viewModelScope.launch {
                var timeLeft = 300L // 5분을 초로 변환 // TODO : 5분
                while (timeLeft > 0) {
                    intent {
                        reduce { state.copy(remainingTime = timeLeft) }
                    }
                    delay(1000L)
                    timeLeft -= 1
                    Timber.tag("코바치치").d("$timeLeft")
                }
                // 타이머가 종료되면 isExistPerson 상태를 false로 변경
                intent {
                    postSideEffect(MainSideEffect.ClearCart)
                }
                personDetectedRecently = false
            }
    }

    private fun onPersonCome() =
        intent {
            reduce {
                state.copy(isExistPerson = true)
            }
        }

    private fun onPersonLeave() =
        intent {
            reduce {
                state.copy(isExistPerson = false)
            }
        }

    private fun startPersonDetectionCooldown() {
        personDetectedRecently = true
        CoroutineScope(Dispatchers.Main).launch {
            delay(delayTime)
            personDetectedRecently = false
            intent {
                reduce {
                    state.copy(isExistPerson = false)
                }
            }
        }
    }

    fun updateGazePoint(gazePoint: Offset?) =
        intent {
            reduce { state.copy(gazePoint = gazePoint) }
        }

    fun requestSignOut(password: String) =
        intent {
            runCatching {
                signOutKioskUseCase(password)
            }.onSuccess {
                clearAllDataUseCase()
                postSideEffect(MainSideEffect.NavigateToLoginScreen)
            }.onFailure {
                postSideEffect(MainSideEffect.Toast(it.message ?: "알수 없는 에러"))
            }
        }

    fun resetMainViewModel() {
        timerJob?.cancel()
        personDetectedRecently = false
        intent {
            reduce {
                state.copy(
                    isRecommend = "",
                    isAddCartFalse = false,
                    isAddCartTrue = false,
                    isPayment = false,
                    isScreenShowing = false,
                    voiceShoppingCart = emptyList(),
                    shouldShowRetryMessage = false,
                    recognizedText = "",
                    tempOrder = "",
                    isTtsOn = false,
                    voiceResult =
                    VoiceBody(
                        category = 0,
                        need_temp = 1,
                        order = emptyList(),
                        message = "",
                        response = "",
                    ),
                    isCartOpen = false,
                    isOrderEndTrue = false,
                    isOrderEndFalse = false,
                    isTemperatureEmpty = false,
                    gazePoint = null,
                    remainingTime = 0,
                    isExistPerson = false,
                    page = 0,
                    mode = MainEnum.KioskMode.ASSIST,
                    category = emptyList(),
                    recommendMenu =
                    MenuCategoryParam(
                        id = 0,
                        category = "",
                        categoryNumber = 0,
                        hotOrIce = "",
                        name = "",
                        price = 0,
                        description = "",
                        imgPath = "",
                    ),
                    subRecommendMenu = emptyList(),
                )
            }
        }
    }
}

data class MainState(
    val page: Int = 0,
    val mode: MainEnum.KioskMode = MainEnum.KioskMode.ASSIST,
    val isScreenShowing: Boolean = false,
    val isExistPerson: Boolean = false,
    val isRecommend: String = "", // 추천 문장 유무 -> 이걸 보여줄 거라서 String으로 받음
    val isTemperatureEmpty: Boolean = false, // 온도가 선택되지않았을 때
    val isOrderEndTrue: Boolean = false, // 장바구니에서 더이상 메뉴를 담지 않을 때
    val isOrderEndFalse: Boolean = false, // 장바구니에서 계속 담기
    val isAddCartTrue: Boolean = false, // 장바구니에 추가
    val isAddCartFalse: Boolean = false, // 장바구니에 추가 안함
    val isPayment: Boolean = false, // 포장인지 매장인지 고를 때
    val isCartOpen: Boolean = false,
    val isTtsOn: Boolean = false,
    val voiceShoppingCart: List<OrderList> = mutableListOf(),
    val category: List<Category> = emptyList(),
    val recognizedText: String = "",
    val tempOrder: String = "",
    val shouldShowRetryMessage: Boolean = false,
    val gazePoint: Offset? = null,
    val remainingTime: Long = 0,
    val age: Int = 30,
    val gender: String = "male",
    val mySpeechText: String = "",
    val isMySpeechInputTextOpen: Boolean = false,
    val voiceResult: VoiceBody =
        VoiceBody(
            category = 0,
            need_temp = 1,
            order = emptyList(),
            message = "",
            response = "",
        ),
    val recommendMenu: MenuCategoryParam =
        MenuCategoryParam(
            id = 0,
            category = "",
            categoryNumber = 0,
            hotOrIce = "",
            name = "",
            price = 0,
            description = "",
            imgPath = "",
        ),
    val subRecommendMenu: List<MenuCategoryParam> = emptyList(),
) : BaseState

sealed interface MainSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : MainSideEffect

    data object NavigateToNextScreen : MainSideEffect

    data object NavigateToAdvertisement : MainSideEffect

    data object NavigateToLoginScreen : MainSideEffect

    data object ClearCart : MainSideEffect
}
