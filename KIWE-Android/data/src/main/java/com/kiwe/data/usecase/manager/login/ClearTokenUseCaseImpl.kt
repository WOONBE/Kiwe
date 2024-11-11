package com.kiwe.data.usecase.manager.login

import com.kiwe.data.datasource.TokenDataSource
import com.kiwe.domain.usecase.manager.login.ClearTokenUseCase
import javax.inject.Inject

class ClearTokenUseCaseImpl
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
    ) : ClearTokenUseCase {
        override suspend fun invoke(): Unit = tokenDataSource.clearToken()
    }
