package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetKioskTotalOrdersLastMonthUseCase
import javax.inject.Inject

class GetKioskTotalOrdersLastMonthUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetKioskTotalOrdersLastMonthUseCase {
        override suspend operator fun invoke(kioskId: Int): Result<Int> = orderService.getKioskTotalOrdersLastMonth(kioskId)
    }
