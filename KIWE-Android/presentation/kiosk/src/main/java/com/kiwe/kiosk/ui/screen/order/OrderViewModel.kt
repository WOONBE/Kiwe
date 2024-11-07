package com.kiwe.kiosk.ui.screen.order

import com.kiwe.domain.exception.APIException
import com.kiwe.domain.usecase.GetCategoryListUseCase
import com.kiwe.kiosk.base.BaseSideEffect
import com.kiwe.kiosk.base.BaseState
import com.kiwe.kiosk.base.BaseViewModel
import com.kiwe.kiosk.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OrderViewModel
    @Inject
    constructor(
        private val getCategoryListUseCase: GetCategoryListUseCase,
    ) : BaseViewModel<OrderState, OrderSideEffect>(OrderState()) {
        override fun handleExceptionIntent(
            coroutineContext: CoroutineContext,
            throwable: Throwable,
        ) {
            intent {
                if (throwable is APIException) {
                    postSideEffect(
                        OrderSideEffect.Toast(
                            "${throwable.code} : " +
                                (throwable.message ?: "알수 없는 에러"),
                        ),
                    )
                } else {
                    postSideEffect(OrderSideEffect.Toast(throwable.message ?: "알수 없는 에러"))
                }
            }
        }

        fun getCategoryList(category: String) =
            intent {
                getCategoryListUseCase(category).getOrThrow()
            }
    }

data class OrderState(
    val category: String = "",
    val orderItem: List<OrderItem> =
        listOf(
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
            OrderItem(
                menuTitle = "디카페인 카페모카",
                menuPrice = 4500,
                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
            ),
        ),
) : BaseState

sealed interface OrderSideEffect : BaseSideEffect {
    data class Toast(
        val message: String,
    ) : OrderSideEffect

    data object NavigateToNextScreen : OrderSideEffect
}
