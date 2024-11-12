package com.kiwe.data.usecase.manager.search

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.MemberInfoResponse
import com.kiwe.domain.model.Token
import com.kiwe.domain.usecase.manager.search.SearchMyInfoUseCase
import javax.inject.Inject

class SearchMyInfoUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : SearchMyInfoUseCase {
        override suspend fun invoke(token: Token): Result<MemberInfoResponse> = memberService.searchMyInfo(token)
    }
