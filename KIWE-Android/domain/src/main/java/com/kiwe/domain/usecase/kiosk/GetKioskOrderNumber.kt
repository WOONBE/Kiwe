package com.kiwe.domain.usecase.kiosk

import com.kiwe.domain.model.KioskOrder

interface GetKioskOrderNumber {
    suspend operator fun invoke(
        ownerId: Int,
        kioskId: Int,
    ): Result<KioskOrder>
}
