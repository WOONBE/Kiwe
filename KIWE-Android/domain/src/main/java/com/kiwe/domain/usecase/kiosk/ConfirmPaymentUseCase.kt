package com.kiwe.domain.usecase.kiosk

interface ConfirmPaymentUseCase {
    suspend operator fun invoke(kioskId: Int): Result<String>
}
