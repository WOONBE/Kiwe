package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.ClearOwnerIdUseCase
import javax.inject.Inject

class ClearOwnerIdUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : ClearOwnerIdUseCase {
        override suspend fun invoke() = kioskDataSource.clearOwnerId()
    }
