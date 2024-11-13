package com.kiwe.kiosk.main

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.kiwe.domain.model.Category
import com.kiwe.domain.model.VoiceOrderRequest
import com.kiwe.domain.usecase.VoiceOrderUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.ui.screen.utils.SpeechRecognizerManager
import com.kiwe.kiosk.ui.screen.utils.SpeechResultListener
import com.kiwe.kiosk.ui.screen.utils.helpPopupRegex
import com.kiwe.kiosk.ui.screen.utils.menuRegex
import com.kiwe.kiosk.ui.screen.utils.orderRegex
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
        private val voiceOrderUseCase: VoiceOrderUseCase,
    ) : BaseViewModel<MainState, MainSideEffect>(MainState()),
        SpeechResultListener {
        private var personDetectedRecently = false
        private val delayTime = TimeUnit.MINUTES.toMillis(5)
        private var timerJob: Job? = null

        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                postSideEffect(MainSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
            }
        }

        init {
            testOrder()
            getMenuCategory()
            initSpeechRecognizer()
        }

        private fun initSpeechRecognizer() {
            speechRecognizerManager.setSpeechResultListener(this)
        }

        // 음성 인식 시작
        fun startSpeechRecognition() {
            speechRecognizerManager.startListening()
        }

        // 음성 인식 중단
        fun stopSpeechRecognition() {
            intent {
                reduce { state.copy(isDialogShowing = false) }
                speechRecognizerManager.stopListening()
            }
        }

        override fun onResultsReceived(results: List<String>) {
            val resultText = results.firstOrNull() ?: ""
            intent {
                Timber.tag("MainViewModel").d("Result: $resultText 하고 ${state.isDialogShowing}")
                if (state.isDialogShowing) {
                    Timber.tag("MainViewModel").d("Result: $resultText 하고 ${state.isDialogShowing}")
                    onSpeechResult(resultText)
                } else if (helpPopupRegex.containsMatchIn(resultText)) {
                    reduce {
                        state.copy(isDialogShowing = true)
                    }
                }
            }
        }

        fun onDismissRequest() =
            intent {
                reduce {
                    state.copy(isDialogShowing = false)
                }
            }

        fun onDetectAgeGender(
            age: Int,
            gender: String,
        ) {
            Timber.tag("MainViewModel").d("age: $age, gender: $gender")
        }

        fun onSpeechResult(result: String) =
            intent {
                if (menuRegex.containsMatchIn(result)) { // 메뉴판에 있는 메뉴를 말했다면
                    // 정해둔 폼으로 문장이 끝나는 지 검사
                    if (orderRegex.containsMatchIn(result)) {
                        // 검사해서 fastapi로 통신
                        voiceOrderUseCase(
                            voiceOrder =
                                VoiceOrderRequest(
                                    sentence = result,
                                    have_temp = false,
                                    order_items = emptyList(),
                                ),
                        )
                        reduce {
                            state.copy(
                                isDialogShowing = false,
                                recognizedText = result,
                                shouldShowRetryMessage = false,
                            )
                        }
                    }
                } else {
                    reduce {
                        state.copy(
                            recognizedText = result,
                            shouldShowRetryMessage = true, // 다시 말해달라는 flag
                        )
                    }
                }
            }

        override fun onPartialResultsReceived(partialResults: List<String>) {
            val partialText = partialResults.firstOrNull() ?: ""
            Timber.tag("MainViewModel").d("Partial Result: $partialText")
            intent {
                if (state.isDialogShowing) {
                    // 다이얼로그가 보여지고 있는 상황이라면
                } else if (helpPopupRegex.containsMatchIn(partialText)) {
                    // 다이얼로그가 안보이는 상황에서 음성이 들어온다면 이걸 탐
                    Timber.tag("MainViewModel").d("${helpPopupRegex.containsMatchIn(partialText)}")
                    reduce {
                        state.copy(isDialogShowing = true)
                    }
                }
            }
        }

        fun testOrder() =
            intent {
                voiceOrderUseCase(
                    voiceOrder =
                        VoiceOrderRequest(
                            sentence = "아메리카노 한 잔 주세요",
                            have_temp = false,
                            order_items = emptyList(),
                        ),
                )
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
                    onStartTouchScreen()
//                    onPersonCome()
//                    startPersonDetectionCooldown()
                }
            } else {
                if (!personDetectedRecently) {
                    onPersonLeave()
                }
            }
        }

        fun onStartTouchScreen() {
            onPersonCome()
            startTimer()
            personDetectedRecently = true
        }

        private fun startTimer() {
            // 기존 타이머가 있다면 취소하고 새로운 타이머 시작
            timerJob?.cancel()
            timerJob =
                viewModelScope.launch {
                    var timeLeft = 10L // 5분을 초로 변환 // TODO : 5분
                    while (timeLeft > 0) {
                        intent {
                            reduce { state.copy(remainingTime = timeLeft) }
                        }
                        delay(1000L)
                        timeLeft -= 1
                        Timber.tag("코바치치").d("$timeLeft")
                    }
                    // 타이머가 종료되면 isExistPerson 상태를 false로 변경
//                    intent {
//                        postSideEffect(MainSideEffect.NavigateToAdvertisement)
//                    }
                    personDetectedRecently = false
                }
        }

        fun onPersonCome() =
            intent {
                reduce {
                    state.copy(isExistPerson = true)
                }
            }

        fun onPersonLeave() =
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
    val isDialogShowing: Boolean = false,
    val category: List<Category> = emptyList(),
    val recognizedText: String = "",
    val shouldShowRetryMessage: Boolean = false,
    val isExistPerson: Boolean = false,
    val gazePoint: Offset? = null,
    val remainingTime: Long = 0,
) : BaseState

sealed interface MainSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : MainSideEffect

    data object NavigateToNextScreen : MainSideEffect

    data object NavigateToAdvertisement : MainSideEffect
}
