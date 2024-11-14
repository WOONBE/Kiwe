package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.SetKioskIdUseCase
import javax.inject.Inject

class SetKioskIdUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : SetKioskIdUseCase {
        override suspend fun invoke(kioskId: String) = kioskDataSource.setKioskId(kioskId)
    }
