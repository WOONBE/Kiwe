package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.GetOrderNumberUseCase
import javax.inject.Inject

class GetOrderNumberUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : GetOrderNumberUseCase {
        override suspend fun invoke(): Int = kioskDataSource.getOrderNumber()
    }
