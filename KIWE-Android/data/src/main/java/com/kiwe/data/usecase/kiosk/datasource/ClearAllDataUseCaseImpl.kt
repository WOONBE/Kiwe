package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.ClearAllDataUseCase
import javax.inject.Inject

class ClearAllDataUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : ClearAllDataUseCase {
        override suspend fun invoke() = kioskDataSource.clearAll()
    }
