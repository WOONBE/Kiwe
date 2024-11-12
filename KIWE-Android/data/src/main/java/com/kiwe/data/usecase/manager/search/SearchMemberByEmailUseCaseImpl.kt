package com.kiwe.data.usecase.manager.search

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.usecase.manager.search.SearchMemberByEmailUseCase
import javax.inject.Inject

class SearchMemberByEmailUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : SearchMemberByEmailUseCase {
        override suspend operator fun invoke(email: String): Result<MemberInfoResponse> = memberService.searchMemberByEmail(email)
    }
