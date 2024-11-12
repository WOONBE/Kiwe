package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.ClearKioskNameUseCase
import javax.inject.Inject

class ClearKioskNameUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : ClearKioskNameUseCase {
        override suspend fun invoke() = kioskDataSource.clearKioskName()
    }
