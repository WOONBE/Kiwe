package com.kiwe.data.usecase.manager

import com.kiwe.data.network.service.UserService
import com.kiwe.domain.model.SignUpParam
import com.kiwe.domain.model.SignUpResponse
import com.kiwe.domain.usecase.SignUpUseCase
import javax.inject.Inject

class SignUpUseCaseImpl
    @Inject
    constructor(
        private val userService: UserService,
    ) : SignUpUseCase {
        override suspend fun invoke(signUpParam: SignUpParam): Result<SignUpResponse> = userService.signUp(signUpParam = signUpParam)
    }
