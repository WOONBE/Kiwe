package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.model.OrderResponse
import com.kiwe.domain.usecase.order.GetOrderUseCase
import javax.inject.Inject

class GetOrderUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetOrderUseCase {
        override suspend operator fun invoke(orderId: Int): Result<OrderResponse> = orderService.getOrder(orderId)
    }
