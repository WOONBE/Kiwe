package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetRecentSixMonthOrderUseCase
import javax.inject.Inject

class GetRecentSixMonthOrderUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetRecentSixMonthOrderUseCase {
        override suspend operator fun invoke(): Result<Map<String, Int>> = orderService.getOrderRecentSixMonth()
    }
