package com.kiwe.data.usecase.kiosk

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.model.KioskOrder
import com.kiwe.domain.usecase.kiosk.GetKioskOrderNumber
import javax.inject.Inject

class GetKioskOrderNumberImpl
    @Inject
    constructor(
        private val kioskManagerService: KioskManagerService,
    ) : GetKioskOrderNumber {
        override suspend fun invoke(
            ownerId: Int,
            kioskId: Int,
        ): Result<KioskOrder> = kioskManagerService.getKioskOrder(ownerId, kioskId)
    }
