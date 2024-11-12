package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.model.OrderResponse
import com.kiwe.domain.usecase.order.GetOrderAllUseCase
import javax.inject.Inject

class GetOrderAllUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetOrderAllUseCase {
        override suspend fun invoke(): Result<List<OrderResponse>> = orderService.getOrderAll()
    }
