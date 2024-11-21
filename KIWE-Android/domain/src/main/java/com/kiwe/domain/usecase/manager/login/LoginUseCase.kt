package com.kiwe.domain.usecase.manager.login

import com.kiwe.domain.model.LoginParam
import com.kiwe.domain.model.LoginResponse

interface LoginUseCase {
    suspend operator fun invoke(loginParam: LoginParam): Result<LoginResponse>
}
