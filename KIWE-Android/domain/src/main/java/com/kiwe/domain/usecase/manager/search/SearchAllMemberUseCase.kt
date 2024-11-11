package com.kiwe.domain.usecase.manager.search

import com.kiwe.domain.model.MemberInfoResponse

interface SearchAllMemberUseCase {
    suspend operator fun invoke(): Result<List<MemberInfoResponse>>
}
