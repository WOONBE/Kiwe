package com.kiwe.domain.usecase.manager.search

import com.kiwe.domain.model.MemberInfoResponse

interface SearchMemberByEmailUseCase {
    suspend operator fun invoke(email: String): Result<MemberInfoResponse>
}
