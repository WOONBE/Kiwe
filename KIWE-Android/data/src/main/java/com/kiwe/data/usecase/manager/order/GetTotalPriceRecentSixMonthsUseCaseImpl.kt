package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetTotalPriceRecentSixMonthsUseCase
import javax.inject.Inject

class GetTotalPriceRecentSixMonthsUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetTotalPriceRecentSixMonthsUseCase {
        override suspend operator fun invoke(): Result<Map<String, Int>> = orderService.getTotalPriceRecentSixMonth()
    }
