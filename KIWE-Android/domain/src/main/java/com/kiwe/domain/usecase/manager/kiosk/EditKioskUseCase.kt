package com.kiwe.domain.usecase.manager.kiosk

import com.kiwe.domain.model.CreateKioskRequest

interface EditKioskUseCase {
    suspend operator fun invoke(
        kioskId: Int,
        request: CreateKioskRequest,
    )
}
