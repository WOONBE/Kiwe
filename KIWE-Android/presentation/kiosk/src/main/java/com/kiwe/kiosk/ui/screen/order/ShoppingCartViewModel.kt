package com.kiwe.kiosk.ui.screen.order

import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.ShoppingCartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.LinkedList
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

        fun onClearAllItem() =
            intent {
                reduce {
                    state.copy(shoppingCartItem = listOf())
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
                    LinkedList(
                        state.shoppingCartItem.map {
                            if (it.menuTitle == item.menuTitle && it.menuRadioOption == item.menuRadioOption) {
                                // 기존 아이템이 있을 경우 count만 업데이트
                                it.copy(count = it.count + item.count)
                            } else {
                                it
                            }
                        },
                    )

                // 새로운 아이템인지 확인
                val itemExists =
                    state.shoppingCartItem.any { it.menuTitle == item.menuTitle && it.menuRadioOption == item.menuRadioOption }

                // 아이템이 없다면 리스트에 추가
                val finalItems =
                    if (itemExists) {
                        updatedItems
                    } else {
                        updatedItems.addFirst(item)
                        updatedItems
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
        LinkedList<ShoppingCartItem>(),
) : BaseState

sealed interface ShoppingCartSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : ShoppingCartSideEffect
}
