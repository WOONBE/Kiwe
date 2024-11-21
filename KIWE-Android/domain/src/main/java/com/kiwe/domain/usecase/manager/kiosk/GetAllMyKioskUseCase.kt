package com.kiwe.domain.usecase.manager.kiosk

import com.kiwe.domain.model.Kiosk

interface GetAllMyKioskUseCase {
    suspend operator fun invoke(): Result<List<Kiosk>>
}
