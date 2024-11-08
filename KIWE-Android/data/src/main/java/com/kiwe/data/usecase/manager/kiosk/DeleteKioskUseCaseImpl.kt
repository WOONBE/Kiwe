package com.kiwe.data.usecase.manager.kiosk

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.usecase.manager.kiosk.DeleteKioskUseCase
import javax.inject.Inject

class DeleteKioskUseCaseImpl
    @Inject
    constructor(
        private val kioskService: KioskManagerService,
    ) : DeleteKioskUseCase {
        override suspend fun invoke(kioskId: Int) {
            kioskService.deleteKiosk(kioskId).getOrThrow()
        }
    }
