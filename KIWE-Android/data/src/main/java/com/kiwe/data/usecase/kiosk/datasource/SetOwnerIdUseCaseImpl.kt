package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.SetOwnerIdUseCase
import javax.inject.Inject

class SetOwnerIdUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : SetOwnerIdUseCase {
        override suspend fun invoke(id: String) = kioskDataSource.setOwnerId(id)
    }
