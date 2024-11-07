package com.kiwe.data.usecase

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.kiosk.CancelPaymentUseCase
import javax.inject.Inject

class CancelPaymentUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : CancelPaymentUseCase {
        override suspend fun invoke(kioskId: Int): Result<String> {
            val response = orderService.cancelPayment(kioskId)
            return Result.success(response.toString())
        }
    }
