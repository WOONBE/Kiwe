package com.kiwe.data.usecase.manager.kiosk

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.model.Kiosk
import com.kiwe.domain.usecase.kiosk.GetKioskByIdUseCase
import javax.inject.Inject

class GetKioskByIdUseCaseImpl
    @Inject
    constructor(
        private val kioskManagerService: KioskManagerService,
    ) : GetKioskByIdUseCase {
        override suspend fun invoke(kioskId: Int): Result<Kiosk> = kioskManagerService.getKioskByKioskId(kioskId)
    }
