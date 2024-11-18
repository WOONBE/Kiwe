package com.kiwe.kiosk.ui.screen.order

import com.kiwe.domain.model.VoiceBody
import com.kiwe.domain.usecase.manager.menu.GetMenuByIdUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.ShoppingCartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.LinkedList
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ShoppingCartViewModel
    @Inject
    constructor(
        private val getMenuByIdUseCase: GetMenuByIdUseCase,
    ) : BaseViewModel<ShoppingCartState, ShoppingCartSideEffect>(ShoppingCartState()) {
        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                postSideEffect(ShoppingCartSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
            }
        }

        fun onVoiceResult(voiceOrder: VoiceBody) =
            intent {
                // voice order -> 실제 장바구니 아이템 변환하고
                val cartList = mutableListOf<ShoppingCartItem>()
                voiceOrder.order.forEach { eachOrder ->
                    val menu = getMenuByIdUseCase(eachOrder.menuId).getOrThrow()
                    Timber.tag("VoiceOrder").d("menu: $menu")
                    val shot =
                        if (eachOrder.option.shot == 1) {
                            "샷 추가" to Pair("1샷 추가", 500)
                        } else {
                            "샷 추가" to Pair("없음", 0)
                        }
                    val sugar =
                        if (eachOrder.option.sugar == 1) {
                            "설탕 추가" to Pair("1개 추가", 100)
                        } else {
                            "설탕 추가" to Pair("없음", 0)
                        }
                    val eachItem =
                        ShoppingCartItem(
                            menuId = menu.id,
                            menuImgPath = menu.imgPath,
                            menuTitle = menu.name,
                            menuRadioOption =
                                mutableMapOf<String, Pair<String, Int>>(
                                    shot,
                                    sugar,
                                ),
                            defaultPrice = menu.price,
                            count = eachOrder.count,
                        )
                    cartList.add(eachItem)
                }
                // 장바구니에 담기
                // 원래 플로우 타도록
                reduce {
                    state.copy(shoppingCartItem = cartList)
                }
                // 담고나서 장바구니 띄우기
                reduce {
                    state.copy(isVoiceOrderConfirm = true)
                }
            }

        fun onConfirmVoiceOrder() =
            intent {
                reduce {
                    Timber.tag("VoiceOrder").d("onConfirmVoiceOrder ${state.isVoiceOrderConfirm}")
                    state.copy(isVoiceOrderConfirm = false)
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

        fun resetShoppingCart() {
            intent {
                reduce {
                    state.copy(
                        shoppingCartItem = emptyList(),
                        voiceShoppingCartItem = emptyList(),
                        isVoiceOrderConfirm = false,
                    )
                }
            }
        }
    }

data class ShoppingCartState(
    val voiceShoppingCartItem: List<ShoppingCartItem> = emptyList(), // linkedlist 오류시 점검할 곳
    val isVoiceOrderConfirm: Boolean = false,
    val shoppingCartItem: List<ShoppingCartItem> =
        LinkedList<ShoppingCartItem>(),
) : BaseState

sealed interface ShoppingCartSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : ShoppingCartSideEffect
}
