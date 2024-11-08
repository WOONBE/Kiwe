package com.kiwe.domain.usecase.manager.kiosk

interface DeleteKioskUseCase {
    suspend operator fun invoke(kioskId: Int)
}
