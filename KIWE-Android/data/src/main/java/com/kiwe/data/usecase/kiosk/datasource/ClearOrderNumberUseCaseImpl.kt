package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.ClearOrderNumberUseCase
import javax.inject.Inject

class ClearOrderNumberUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : ClearOrderNumberUseCase {
        override suspend fun invoke() = kioskDataSource.clearOrderNumber()
    }
