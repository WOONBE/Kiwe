package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.GetKioskIdUseCase
import javax.inject.Inject

class GetKioskIdUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : GetKioskIdUseCase {
        override suspend fun invoke(): String = kioskDataSource.getKioskId()
    }
