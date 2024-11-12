package com.kiwe.domain.usecase.manager.search

import com.kiwe.domain.model.MemberInfoResponse

interface SearchMemberByIdUseCase {
    suspend operator fun invoke(id: Int): Result<MemberInfoResponse>
}
