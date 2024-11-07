package com.kiwe.kiosk.ui.screen.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiwe.domain.model.Order
import com.kiwe.domain.model.getDummyOrder
import com.kiwe.domain.usecase.kiosk.OrderUseCase
import com.kiwe.kiosk.model.toOrderItem
import com.kiwe.kiosk.ui.screen.order.ShoppingCartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel
    @Inject
    constructor(
        private val orderUseCase: OrderUseCase,
    ) : ViewModel() {
        fun postOrder(shoppingCartState: ShoppingCartState) {
            val order =
                Order(
                    menuOrders =
                        shoppingCartState.shoppingCartItem.map {
                            it.toOrderItem()
                        },
                )
            shoppingCartState.shoppingCartItem
            Timber.tag(javaClass.simpleName).d("postOrder")
            val dummyOrder = getDummyOrder()
            viewModelScope.launch {
                orderUseCase(order)
            }
        }
    }
