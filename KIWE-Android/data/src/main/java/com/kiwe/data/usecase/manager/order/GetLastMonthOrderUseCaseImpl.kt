package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetLastMonthOrderUseCase
import javax.inject.Inject

class GetLastMonthOrderUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetLastMonthOrderUseCase {
        override suspend operator fun invoke(): Result<Int> = orderService.getOrderLastMonth()
    }
