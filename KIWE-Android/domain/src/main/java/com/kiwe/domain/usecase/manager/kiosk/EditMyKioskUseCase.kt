package com.kiwe.domain.usecase.manager.kiosk

import com.kiwe.domain.model.CreateKioskRequest

interface EditMyKioskUseCase {
    suspend operator fun invoke(
        kioskId: Int,
        request: CreateKioskRequest,
    )
}
