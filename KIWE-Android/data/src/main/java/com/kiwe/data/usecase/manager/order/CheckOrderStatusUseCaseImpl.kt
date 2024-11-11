package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.CheckOrderStatusUseCase
import javax.inject.Inject

class CheckOrderStatusUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : CheckOrderStatusUseCase {
        override suspend operator fun invoke(kioskId: Int): Result<String> = orderService.checkOrderStatus(kioskId)
    }
