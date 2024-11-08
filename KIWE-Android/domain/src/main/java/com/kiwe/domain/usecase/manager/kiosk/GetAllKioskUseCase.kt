package com.kiwe.domain.usecase.manager.kiosk

import com.kiwe.domain.model.Kiosk

interface GetAllKioskUseCase {
    suspend operator fun invoke(): List<Kiosk>
}
