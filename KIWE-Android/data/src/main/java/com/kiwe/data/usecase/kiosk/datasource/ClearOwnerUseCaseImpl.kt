package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.ClearOwnerUseCase
import javax.inject.Inject

class ClearOwnerUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : ClearOwnerUseCase {
        override suspend fun invoke() = kioskDataSource.clearKioskId()
    }
