package com.kiwe.data.usecase

import com.kiwe.data.network.service.UserService
import com.kiwe.domain.model.LoginParam
import com.kiwe.domain.model.LoginResponse
import com.kiwe.domain.usecase.manager.login.LoginUseCase
import javax.inject.Inject

class LoginUseCaseImpl
    @Inject
    constructor(
        private val userService: UserService,
    ) : LoginUseCase {
        override suspend fun invoke(loginParam: LoginParam): Result<LoginResponse> = userService.login(loginParam)
    }
