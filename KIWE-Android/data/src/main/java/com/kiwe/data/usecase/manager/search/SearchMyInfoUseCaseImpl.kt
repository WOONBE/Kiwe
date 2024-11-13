package com.kiwe.data.usecase.manager.search

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.usecase.manager.search.SearchMyInfoUseCase
import javax.inject.Inject

class SearchMyInfoUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : SearchMyInfoUseCase {
        override suspend fun invoke(): Result<MemberInfoResponse> = memberService.searchMyInfo()
    }
