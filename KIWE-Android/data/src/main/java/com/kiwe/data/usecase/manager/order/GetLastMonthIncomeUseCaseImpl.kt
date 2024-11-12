package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetLastMonthIncomeUseCase
import javax.inject.Inject

class GetLastMonthIncomeUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetLastMonthIncomeUseCase {
        override suspend operator fun invoke(): Result<Int> = orderService.getLastMonthIncome()
    }
