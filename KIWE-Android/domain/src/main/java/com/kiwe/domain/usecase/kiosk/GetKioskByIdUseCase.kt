package com.kiwe.domain.usecase.kiosk

import com.kiwe.domain.model.Kiosk

interface GetKioskByIdUseCase {
    suspend operator fun invoke(kioskId: Int): Result<Kiosk>
}
