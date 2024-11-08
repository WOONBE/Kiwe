package com.kiwe.domain.usecase.kiosk

interface CancelPaymentUseCase {
    suspend operator fun invoke(kioskId: Int): Result<String>
}
