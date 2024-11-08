package com.kiwe.data.usecase

import com.kiwe.data.datasource.TokenDataSource
import com.kiwe.domain.model.Token
import com.kiwe.domain.usecase.manager.token.SetTokenUseCase
import javax.inject.Inject

class SetTokenUseCaseImpl
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
    ) : SetTokenUseCase {
        override suspend fun invoke(token: Token) {
            tokenDataSource.setToken(token)
        }
    }
