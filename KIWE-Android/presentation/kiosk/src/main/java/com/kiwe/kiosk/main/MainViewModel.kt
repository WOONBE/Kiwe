package com.kiwe.kiosk.main

import androidx.compose.ui.geometry.Offset
import com.kiwe.domain.model.Category
import com.kiwe.domain.model.VoiceOrderRequest
import com.kiwe.domain.model.VoiceOrderResponse
import com.kiwe.domain.usecase.VoiceOrderUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.ui.screen.utils.SpeechRecognizerManager
import com.kiwe.kiosk.ui.screen.utils.SpeechResultListener
import com.kiwe.kiosk.ui.screen.utils.helpPopupRegex
import com.kiwe.kiosk.ui.screen.utils.menuRegex
import com.kiwe.kiosk.ui.screen.utils.orderRegex
import com.kiwe.kiosk.ui.screen.utils.temperatureRegex
import com.kiwe.kiosk.utils.MainEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        private val voiceOrderUseCase: VoiceOrderUseCase,
    ) : BaseViewModel<MainState, MainSideEffect>(MainState()),
        SpeechResultListener {
        private var personDetectedRecently = false
        private val delayTime = TimeUnit.MINUTES.toMillis(2)

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
        }

        private fun initSpeechRecognizer() {
            speechRecognizerManager.setSpeechResultListener(this)
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

        override fun onResultsReceived(results: List<String>) {
            val resultText = results.firstOrNull() ?: ""
            intent {
                Timber.tag("MainViewModel").d("Result: $resultText 하고 ${state.isScreenShowing}")
                if (state.isScreenShowing) {
                    onSpeechResult(resultText)
                } else if (helpPopupRegex.containsMatchIn(resultText)) {
                    reduce {
                        state.copy(isScreenShowing = true)
                    }
                }
            }
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
            Timber.tag("MainViewModel").d("age: $age, gender: $gender")
        }

        fun clearVoiceRecord() =
            intent {
                reduce {
                    state.copy(
                        tempOrder = "",
                        isTemperatureEmpty = false,
                        voiceResult =
                            VoiceOrderResponse(
                                category = 0,
                                need_temp = 1,
                                order = emptyList(),
                                message = "",
                                response = "",
                            ),
                    )
                }
            }

        fun onSpeechResult(result: String) =
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
                                    order_items = emptyList(), // 빈 장바구니를 의미함(기존에 있는게 있으면 그걸 넣으면 됨)
                                ),
                        ).onSuccess {
                            // 온도까지 잘 들어가 있다면
                            reduce {
                                state.copy(
                                    recognizedText = "",
                                    voiceResult = it,
                                    shouldShowRetryMessage = false,
                                    isScreenShowing = false,
                                )
                            }
                        }.onFailure {
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
                                    order_items = emptyList(), // 빈 장바구니를 의미함(기존에 있는게 있으면 그걸 넣으면 됨)
                                ),
                        ).onSuccess {
                            // AI에서 성공해서 응답이 돌아오면
                            if (it.response == "잘못된 접근입니다.") {
                                postSideEffect(MainSideEffect.Toast(it.response))
                                reduce {
                                    state.copy(
                                        shouldShowRetryMessage = true,
                                    )
                                }
                            } else {
                                if (it.response.contains("온도를 확인해주세요")) {
                                    Timber.tag("temp").d("${it.response}")
                                    val listPart = it.response.substringAfter("[").substringBefore("]")
                                    val itemList = listPart.replace("'", "").split(", ")
                                    // 지금은 단일로 할 것 같긴 함
                                    reduce {
                                        state.copy(
                                            tempOrder = removeHelpResult,
                                            isTemperatureEmpty = true,
                                        )
                                    }
                                } else {
                                    // 온도까지 잘 들어가 있다면
                                    reduce {
                                        state.copy(
                                            recognizedText = "",
                                            voiceResult = it,
                                            shouldShowRetryMessage = false,
                                            isScreenShowing = false,
                                        )
                                    }
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
            val partialText = partialResults.firstOrNull() ?: ""
            Timber.tag("MainViewModel").d("Partial Result: $partialText")
            intent {
                if (state.isScreenShowing) {
                    // 다이얼로그가 보여지고 있는 상황이라면
                } else if (helpPopupRegex.containsMatchIn(partialText)) {
                    // 다이얼로그가 안보이는 상황에서 음성이 들어온다면 이걸 탐
                    Timber.tag("MainViewModel").d("${helpPopupRegex.containsMatchIn(partialText)}")
                    reduce {
                        state.copy(isScreenShowing = true)
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
                    state.copy(
                        page = page,
                    )
                }
            }

        fun detectPerson(box: Boolean) {
            if (box) {
                if (!personDetectedRecently) {
                    onPersonCome()
                    startPersonDetectionCooldown()
                }
            } else {
                if (!personDetectedRecently) {
                    onPersonLeave()
                }
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
    }

data class MainState(
    val page: Int = 0,
    val mode: MainEnum.KioskMode = MainEnum.KioskMode.ASSIST,
    val isScreenShowing: Boolean = false,
    val isExistPerson: Boolean = false,
    val isTemperatureEmpty: Boolean = false,
    val category: List<Category> = emptyList(),
    val recognizedText: String = "",
    val tempOrder: String = "",
    val shouldShowRetryMessage: Boolean = false,
    val gazePoint: Offset? = null,
    val voiceResult: VoiceOrderResponse =
        VoiceOrderResponse(
            category = 0,
            need_temp = 1,
            order = emptyList(),
            message = "",
            response = "",
        ),
) : BaseState

sealed interface MainSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : MainSideEffect

    data object NavigateToNextScreen : MainSideEffect
}
