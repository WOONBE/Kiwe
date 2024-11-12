package com.kiwe.data.usecase.manager.search

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.usecase.manager.search.SearchMemberByIdUseCase
import javax.inject.Inject

class SearchMemberByIdUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : SearchMemberByIdUseCase {
        override suspend operator fun invoke(id: Int): Result<MemberInfoResponse> = memberService.searchMemberById(id)
    }
