package com.kiwe.data.usecase.manager.kiosk

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.model.Kiosk
import com.kiwe.domain.usecase.manager.kiosk.GetAllKioskUseCase
import javax.inject.Inject

class GetAllMyKioskUseCaseImpl
    @Inject
    constructor(
        private val kioskService: KioskManagerService,
    ) : GetAllKioskUseCase {
        override suspend fun invoke(): List<Kiosk> = kioskService.getMyKiosk().getOrThrow()
    }
