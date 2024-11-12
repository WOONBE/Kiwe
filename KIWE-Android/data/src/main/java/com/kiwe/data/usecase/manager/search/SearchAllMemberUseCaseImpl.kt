package com.kiwe.data.usecase.manager.search

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.usecase.manager.search.SearchAllMemberUseCase
import javax.inject.Inject

class SearchAllMemberUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : SearchAllMemberUseCase {
        override suspend operator fun invoke(): Result<List<MemberInfoResponse>> = memberService.searchAllMember()
    }
