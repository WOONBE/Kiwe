package com.kiwe.kiosk.ui.screen.order

import android.util.Log
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.ShoppingCartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val TAG = "ShoppingCartViewModel 싸피"

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

        fun onAddItem(menuTitle: String) =
            intent {
                reduce {
                    Log.d(TAG, "onAddItem: ")
                    state.copy(
                        shoppingCartItem =
                            state.shoppingCartItem
                                .map {
                                    if (it.menuTitle == menuTitle) {
                                        it.copy(count = it.count + 1)
                                    } else {
                                        it
                                    }
                                },
                    )
                }
            }

        fun onMinusItem(menuTitle: String) =
            intent {
                reduce {
                    state.copy(
                        shoppingCartItem =
                            state.shoppingCartItem
                                .map {
                                    if (it.menuTitle == menuTitle) {
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
            ShoppingCartItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 2,
                option = mapOf("샷" to "연하게", "당도" to "추가"),
            ),
            ShoppingCartItem(
                menuTitle = "콜드브루디카페인",
                menuPrice = 3500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 1,
                option = mapOf("샷" to "연하게"),
            ),
            ShoppingCartItem(
                menuTitle = "딸기쿠키프라페",
                menuPrice = 5000,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
                option = mapOf("당도" to "추가"),
            ),
            ShoppingCartItem(
                menuTitle = "딸기쿠키프라페11111",
                menuPrice = 5000,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
                option = mapOf("당도" to "추가"),
            ),
            ShoppingCartItem(
                menuTitle = "딸111기쿠키프라페1111",
                menuPrice = 5000,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
                option = mapOf("당도" to "추가", "샷" to "연하게", "얼음" to "추가", "테이크 아웃" to "O"),
            ),
            ShoppingCartItem(
                menuTitle = "딸111기쿠키프라페111",
                menuPrice = 5000,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
                option = mapOf("당도" to "추가", "샷" to "연하게", "얼음" to "추가", "테이크 아웃" to "O"),
            ),
            ShoppingCartItem(
                menuTitle = "딸111기쿠키프라페11",
                menuPrice = 5000,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
                option = mapOf("당도" to "추가", "샷" to "연하게", "얼음" to "추가", "테이크 아웃" to "O"),
            ),
            ShoppingCartItem(
                menuTitle = "딸111기쿠키프라페1",
                menuPrice = 5000,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
            ),
        ),
) : BaseState

sealed interface ShoppingCartSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : ShoppingCartSideEffect
}
