package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetKioskTotalOrdersLast6MonthsUseCase
import javax.inject.Inject

class GetKioskTotalOrdersLast6MonthsUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetKioskTotalOrdersLast6MonthsUseCase {
        override suspend operator fun invoke(kioskId: Int): Result<Map<String, Int>> = orderService.getKioskTotalOrdersLast6Months(kioskId)
    }
