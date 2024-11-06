package com.kiwe.kiosk.ui.screen.order

import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.ShoppingCartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ShoppingCartViewModel
    @Inject
    constructor() : BaseViewModel<ShoppingCartState, ShoppingCartSideEffect>(ShoppingCartState()) {
        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                postSideEffect(ShoppingCartSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
            }
        }

        fun onDeleteItem(menuTitle: String) =
            intent {
                reduce {
                    state.copy(
                        shoppingCartItem =
                            state.shoppingCartItem.filter {
                                it.menuTitle != menuTitle
                            },
                    )
                }
            }

        fun onInsertItem(item: ShoppingCartItem) =
            intent {
                val updatedItems =
                    state.shoppingCartItem.map {
                        if (it.menuTitle == item.menuTitle && it.menuRadioOption == item.menuRadioOption) {
                            // 기존 아이템이 있을 경우 count만 업데이트
                            it.copy(count = it.count + item.count)
                        } else {
                            it
                        }
                    }

                // 새로운 아이템인지 확인
                val itemExists =
                    state.shoppingCartItem.any { it.menuTitle == item.menuTitle && it.menuRadioOption == item.menuRadioOption }

                // 아이템이 없다면 리스트에 추가
                val finalItems =
                    if (itemExists) {
                        updatedItems
                    } else {
                        updatedItems.plus(item)
                    }
                reduce {
                    state.copy(shoppingCartItem = finalItems)
                }
            }

        fun onAddItem(
            menuTitle: String,
            menuRadioOption: Map<String, Pair<String, Int>>,
        ) = intent {
            reduce {
                state.copy(
                    shoppingCartItem =
                        state.shoppingCartItem
                            .map {
                                if (it.menuTitle == menuTitle && it.menuRadioOption == menuRadioOption) {
                                    it.copy(count = it.count + 1)
                                } else {
                                    it
                                }
                            },
                )
            }
        }

        fun onMinusItem(
            menuTitle: String,
            menuRadioOption: Map<String, Pair<String, Int>>,
        ) = intent {
            reduce {
                state.copy(
                    shoppingCartItem =
                        state.shoppingCartItem
                            .map {
                                if (it.menuTitle == menuTitle && it.menuRadioOption == menuRadioOption) {
                                    it.copy(count = it.count - 1)
                                } else {
                                    it
                                }
                            },
                )
            }
        }
    }

data class ShoppingCartState(
    val shoppingCartItem: List<ShoppingCartItem> =
        listOf(
//            ShoppingCartItem(
//                menuTitle = "디카페인 카페모카",
//                menuRadioOption = mutableMapOf("샷" to Pair("연하게", 500), "설탕" to Pair("추가", 100)),
//                count = 2,
//            ),
//            ShoppingCartItem(
//                menuTitle = "콜드브루디카페인",
//                menuRadioOption = mutableMapOf("샷" to Pair("연하게", 500), "설탕" to Pair("추가", 100)),
//                count = 1,
//            ),
//            ShoppingCartItem(
//                menuTitle = "딸기쿠키프라페",
//                menuRadioOption = mutableMapOf("샷" to Pair("연하게", 500), "설탕" to Pair("추가", 100)),
//                count = 99,
//            ),
//            ShoppingCartItem(
//                menuTitle = "딸기쿠키프라페11111",
//                menuRadioOption = mutableMapOf("샷" to Pair("연하게", 500), "설탕" to Pair("추가", 100)),
//                count = 99,
//            ),
//            ShoppingCartItem(
//                menuTitle = "딸111기쿠키프라페1111",
//                menuRadioOption = mutableMapOf("샷" to Pair("연하게", 500), "설탕" to Pair("추가", 100)),
//                count = 99,
//            ),
//            ShoppingCartItem(
//                menuTitle = "딸111기쿠키프라페111",
//                menuRadioOption = mutableMapOf("샷" to Pair("연하게", 500), "설탕" to Pair("추가", 100)),
//                count = 99,
//            ),
//            ShoppingCartItem(
//                menuTitle = "딸111기쿠키프라페11",
//                menuRadioOption = mutableMapOf("샷" to Pair("연하게", 500), "설탕" to Pair("추가", 100)),
//                count = 99,
//            ),
//            ShoppingCartItem(
//                menuTitle = "딸111기쿠키프라페1",
// //                menuPrice = 5000,
// //                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
//                count = 99,
//            ),
        ),
) : BaseState

sealed interface ShoppingCartSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : ShoppingCartSideEffect
}
