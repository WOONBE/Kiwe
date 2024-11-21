package com.kiwe.domain.usecase.manager.token

import com.kiwe.domain.model.Token

interface SetTokenUseCase {
    suspend operator fun invoke(token: Token)
}