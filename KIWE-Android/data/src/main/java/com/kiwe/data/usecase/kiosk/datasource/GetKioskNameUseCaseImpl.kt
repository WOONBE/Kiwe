package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.GetKioskNameUseCase
import javax.inject.Inject

class GetKioskNameUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : GetKioskNameUseCase {
        override suspend fun invoke(): String = kioskDataSource.getKioskName()
    }
