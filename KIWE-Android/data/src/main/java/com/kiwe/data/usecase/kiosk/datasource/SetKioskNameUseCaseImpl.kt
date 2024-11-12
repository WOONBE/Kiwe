package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.SetKioskNameUseCase
import javax.inject.Inject

class SetKioskNameUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : SetKioskNameUseCase {
        override suspend fun invoke(name: String) = kioskDataSource.setKioskName(name)
    }
