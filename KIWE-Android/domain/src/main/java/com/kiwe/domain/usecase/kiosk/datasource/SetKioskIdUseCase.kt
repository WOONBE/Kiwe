package com.kiwe.domain.usecase.kiosk.datasource

interface SetKioskIdUseCase {
    suspend operator fun invoke(kioskId: String)
}
