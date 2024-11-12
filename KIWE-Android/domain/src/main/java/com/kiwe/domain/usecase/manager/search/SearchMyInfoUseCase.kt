package com.kiwe.domain.usecase.manager.search

import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.model.Token

interface SearchMyInfoUseCase {
    suspend operator fun invoke(token: Token): Result<MemberInfoResponse>
}