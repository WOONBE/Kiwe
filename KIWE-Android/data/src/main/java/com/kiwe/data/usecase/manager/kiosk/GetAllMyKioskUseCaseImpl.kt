package com.kiwe.data.usecase.manager.kiosk

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.model.Kiosk
import com.kiwe.domain.usecase.manager.kiosk.GetAllMyKioskUseCase
import javax.inject.Inject

class GetAllMyKioskUseCaseImpl
    @Inject
    constructor(
        private val kioskService: KioskManagerService,
    ) : GetAllMyKioskUseCase {
        override suspend fun invoke(): Result<List<Kiosk>> = kioskService.getMyKiosk()
    }
