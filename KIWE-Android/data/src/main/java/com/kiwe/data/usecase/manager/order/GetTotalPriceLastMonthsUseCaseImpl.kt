package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetTotalPriceLastMonthUseCase
import javax.inject.Inject

class GetTotalPriceLastMonthsUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetTotalPriceLastMonthUseCase {
        override suspend operator fun invoke(): Result<Int> = orderService.getTotalPriceLastMonth()
    }
