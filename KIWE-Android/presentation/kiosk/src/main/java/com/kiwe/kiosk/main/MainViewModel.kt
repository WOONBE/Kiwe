package com.kiwe.kiosk.main

import com.kiwe.domain.model.Category
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.utils.MainEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : BaseViewModel<MainState, MainSideEffect>(MainState()) {
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
        }

        fun onDismissSpeechDialog() =
            intent {
                reduce {
                    state.copy(isRecording = false)
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
    }

data class MainState(
    val page: Int = 0,
    val mode: MainEnum.KioskMode = MainEnum.KioskMode.MANUAL,
    val isRecording: Boolean = true,
    val category: List<Category> = emptyList(),
) : BaseState

sealed interface MainSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : MainSideEffect

    data object NavigateToNextScreen : MainSideEffect
}
