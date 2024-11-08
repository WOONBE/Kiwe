package com.kiwe.domain.usecase.manager.kiosk

import com.kiwe.domain.model.CreateKioskRequest
import com.kiwe.domain.model.Kiosk

interface CreateKioskUseCase {
    suspend operator fun invoke(kiosk: CreateKioskRequest): Result<Kiosk>
}
