package com.kiwe.kiosk.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kiwe.kiosk.R
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.OrderOption
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.coroutines.CoroutineContext

@HiltViewModel(assistedFactory = OptionViewModel.OptionViewModelFactory::class)
class OptionViewModel
    @AssistedInject
    constructor(
        @Assisted("menuId") private val menuId: Int,
        @Assisted("menuImgPath") private val menuImgPath: String,
        @Assisted("menuTitle") private val menuTitle: String,
        @Assisted("menuDescription") private val menuDescription: String,
        @Assisted("menuCost") private val menuCost: Int,
    ) : BaseViewModel<OptionState, OptionSideEffect>(
            OptionState(
                menuId,
                menuImgPath,
                menuTitle,
                menuDescription,
                menuCost,
            ),
        ) {
        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                postSideEffect(OptionSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
            }
        }

        fun init(
            menuId: Int,
            menuImgPath: String,
            menuTitle: String,
            menuDescription: String,
            menuCost: Int,
        ) = intent {
            reduce {
                state.copy(
                    menuId = menuId,
                    menuImgPath = menuImgPath,
                    menuTitle = menuTitle,
                    menuDescription = menuDescription,
                    menuCost = menuCost,
                )
            }
        }

        fun onClear() =
            intent {
                reduce {
                    OptionState(0, "", "", "", 0)
                }
            }

        fun onPlus() =
            intent {
                reduce {
                    state.copy(menuCount = this.state.menuCount + 1)
                }
            }

        fun onMinus() =
            intent {
                reduce {
                    state.copy(menuCount = this.state.menuCount - 1)
                }
            }

        fun onRadioOptionClick(
            optionCategory: String,
            optionTitle: String,
            optionPrice: Int,
        ) = intent {
            reduce {
                state.copy(
                    radioOptionCost =
                        this.state.radioOptionCost.apply {
                            this[optionCategory] = Pair(optionTitle, optionPrice)
                        },
                )
            }
        }

//        fun onCheckBoxOptionClick() {
//        }

        @AssistedFactory
        interface OptionViewModelFactory {
            fun create(
                @Assisted("menuId") menuId: Int,
                @Assisted("menuImgPath") menuImgPath: String,
                @Assisted("menuTitle") menuTitle: String,
                @Assisted("menuDescription") menuDescription: String,
                @Assisted("menuCost") menuCost: Int,
            ): OptionViewModel
        }

        companion object {
            fun provideFactory(
                assistedFactory: OptionViewModelFactory,
                menuId: Int,
                menuImgPath: String,
                menuTitle: String,
                menuDescription: String,
                menuCost: Int,
            ): ViewModelProvider.Factory =
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        assistedFactory.create(
                            menuId,
                            menuImgPath,
                            menuTitle,
                            menuDescription,
                            menuCost,
                        ) as T
                }
        }
    }

data class OptionState(
    val menuId: Int,
    val menuImgPath: String,
    val menuTitle: String,
    val menuDescription: String,
    val menuCost: Int,
    val menuCount: Int = 1,
    val radioOptionCost: MutableMap<String, Pair<String, Int>> = mutableMapOf(),
    val checkBoxOptionCost: Map<Pair<String, String>, String> = mapOf(),
    val optionList: Map<String, List<OrderOption>> =
        mutableMapOf<String, List<OrderOption>>().apply {
            this["샷 추가"] =
                listOf(
                    OrderOption(
                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        optionImgRes = R.drawable.img_option_shot_one,
                        title = "없음",
                        price = 0,
                        radio = true,
                    ),
                    OrderOption(
                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        optionImgRes = R.drawable.img_option_shot_two,
                        title = "1샷 추가",
                        price = 500,
                        radio = true,
                    ),
                )

            this["설탕 추가"] =
                listOf(
                    OrderOption(
                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        optionImgRes = R.drawable.img_option_sugar_one,
                        title = "없음",
                        price = 0,
                        radio = true,
                    ),
                    OrderOption(
                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                        optionImgRes = R.drawable.img_option_sugar_two,
                        title = "1개 추가",
                        price = 100,
                        radio = true,
                    ),
                )

//            this["추가 선택"] =
//                listOf(
//                    OrderOption(
//                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
//                        title = "기타1",
//                        price = 0,
//                        radio = true,
//                    ),
//                    OrderOption(
//                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
//                        title = "기타2",
//                        price = 0,
//                        radio = true,
//                    ),
//                )
//
//            this["옵션2"] =
//                listOf(
//                    OrderOption(
//                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
//                        title = "테이크 아웃",
//                        price = -500,
//                        radio = true,
//                    ),
//                    OrderOption(
//                        optionImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
//                        title = "매장에서",
//                        price = 0,
//                        radio = true,
//                    ),
//                )
        },
) : BaseState {
    val totalPrice
        get() =
            menuCost +
                radioOptionCost.values.sumOf {
                    it.second
                }
}

sealed interface OptionSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : OptionSideEffect

    data object NavigateToNextScreen : OptionSideEffect
}
