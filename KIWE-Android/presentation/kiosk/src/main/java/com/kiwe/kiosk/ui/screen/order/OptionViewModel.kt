package com.kiwe.kiosk.ui.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kiwe.domain.model.MenuCategoryParam
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
        @Assisted("menuItem") private val menuItem: MenuCategoryParam,
    ) : BaseViewModel<OptionState, OptionSideEffect>(
            OptionState(
                menuItem,
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

        fun init(menuItem: MenuCategoryParam) =
            intent {
                reduce {
                    state.copy(
                        menuItem = menuItem,
                        optionList = generateOptionList(menuItem),
                        menuCount = 1,
                    )
                }
            }

        fun onClear() =
            intent {
                reduce {
                    OptionState(MenuCategoryParam(), 0)
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

        private fun generateOptionList(menuItem: MenuCategoryParam): Map<String, List<OrderOption>> {
            val options = mutableMapOf<String, List<OrderOption>>()

            // 조건에 따라 옵션을 동적으로 추가
            if (menuItem.category.contains("커피")) {
                options["샷 추가"] =
                    listOf(
                        OrderOption(
                            optionImgUrl = null,
                            optionImgRes = R.drawable.img_option_shot_one,
                            title = "없음",
                            price = 0,
                            radio = true,
                        ),
                        OrderOption(
                            optionImgUrl = null,
                            optionImgRes = R.drawable.img_option_shot_two,
                            title = "1샷 추가",
                            price = 500,
                            radio = true,
                        ),
                    )
            }

            if (menuItem.category != "디저트") {
                options["설탕 추가"] =
                    listOf(
                        OrderOption(
                            optionImgUrl = null,
                            optionImgRes = R.drawable.img_option_sugar_one,
                            title = "없음",
                            price = 0,
                            radio = true,
                        ),
                        OrderOption(
                            optionImgUrl = null,
                            optionImgRes = R.drawable.img_option_sugar_two,
                            title = "1개 추가",
                            price = 100,
                            radio = true,
                        ),
                    )
            }

            return options
        }

//        fun onCheckBoxOptionClick() {
//        }

        @AssistedFactory
        interface OptionViewModelFactory {
            fun create(
                @Assisted("menuItem") menuItem: MenuCategoryParam,
            ): OptionViewModel
        }

        companion object {
            fun provideFactory(
                assistedFactory: OptionViewModelFactory,
                menuItem: MenuCategoryParam,
            ): ViewModelProvider.Factory =
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        assistedFactory.create(
                            menuItem,
                        ) as T
                }
        }
    }

data class OptionState(
    val menuItem: MenuCategoryParam,
    val menuCount: Int = 1,
    val radioOptionCost: MutableMap<String, Pair<String, Int>> = mutableMapOf(),
    val checkBoxOptionCost: Map<Pair<String, String>, String> = mapOf(),
    val optionList: Map<String, List<OrderOption>> =
        mutableMapOf<String, List<OrderOption>>().apply {
            this["샷 추가"] =
                listOf(
                    OrderOption(
                        optionImgUrl = null,
                        optionImgRes = R.drawable.img_option_shot_one,
                        title = "없음",
                        price = 0,
                        radio = true,
                    ),
                    OrderOption(
                        optionImgUrl = null,
                        optionImgRes = R.drawable.img_option_shot_two,
                        title = "1샷 추가",
                        price = 500,
                        radio = true,
                    ),
                )

            this["설탕 추가"] =
                listOf(
                    OrderOption(
                        optionImgUrl = null,
                        optionImgRes = R.drawable.img_option_sugar_one,
                        title = "없음",
                        price = 0,
                        radio = true,
                    ),
                    OrderOption(
                        optionImgUrl = null,
                        optionImgRes = R.drawable.img_option_sugar_two,
                        title = "1개 추가",
                        price = 100,
                        radio = true,
                    ),
                )
        },
) : BaseState {
    val totalPrice
        get() =
            menuItem.price +
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
