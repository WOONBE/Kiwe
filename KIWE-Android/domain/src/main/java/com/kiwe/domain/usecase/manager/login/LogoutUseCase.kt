package com.kiwe.domain.usecase.manager.login

import com.kiwe.domain.model.LoginParam
import com.kiwe.domain.model.LoginResponse
import com.kiwe.domain.model.LogoutParam

interface LogoutUseCase {
    suspend operator fun invoke(logoutParam: LogoutParam): Result<Unit>
}
