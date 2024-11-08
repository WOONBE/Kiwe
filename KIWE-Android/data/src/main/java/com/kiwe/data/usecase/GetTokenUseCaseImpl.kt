package com.kiwe.data.usecase

import com.kiwe.data.datasource.TokenDataSource
import com.kiwe.domain.model.Token
import com.kiwe.domain.usecase.manager.token.GetTokenUseCase
import javax.inject.Inject

class GetTokenUseCaseImpl
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
    ) : GetTokenUseCase {
        override suspend fun invoke(): Token? = tokenDataSource.getToken()
    }
