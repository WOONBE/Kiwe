package com.kiwe.domain.usecase.manager.search

import com.kiwe.domain.model.MemberInfoResponse

interface SearchMyInfoUseCase {
    suspend operator fun invoke(): Result<MemberInfoResponse>
}