package com.kiwe.domain.usecase

import com.kiwe.domain.model.SignUpParam
import com.kiwe.domain.model.SignUpResponse

interface SignUpUseCase {
    suspend operator fun invoke(signUpParam: SignUpParam): Result<SignUpResponse>
}
