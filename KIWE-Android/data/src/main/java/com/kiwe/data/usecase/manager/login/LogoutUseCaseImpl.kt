package com.kiwe.data.usecase.manager.login

import com.kiwe.data.network.service.MemberService
import com.kiwe.domain.model.LogoutParam
import com.kiwe.domain.usecase.manager.login.LogoutUseCase
import javax.inject.Inject

class LogoutUseCaseImpl
    @Inject
    constructor(
        private val memberService: MemberService,
    ) : LogoutUseCase {
        override suspend fun invoke(logoutParam: LogoutParam): Result<Unit> = memberService.logout(logoutParam = logoutParam)
    }
