package com.kiwe.data.usecase.kiosk.datasource

import com.kiwe.data.datasource.KioskDataSource
import com.kiwe.domain.usecase.kiosk.datasource.GetOwnerIdUseCase
import javax.inject.Inject

class GetOwnerIdUseCaseImpl
    @Inject
    constructor(
        private val kioskDataSource: KioskDataSource,
    ) : GetOwnerIdUseCase {
        override suspend fun invoke(): String = kioskDataSource.getOwnerId()
    }
