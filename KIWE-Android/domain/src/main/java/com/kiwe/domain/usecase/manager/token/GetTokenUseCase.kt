package com.kiwe.domain.usecase.manager.token

import com.kiwe.domain.model.Token

interface GetTokenUseCase {
    suspend operator fun invoke(): Token?
}