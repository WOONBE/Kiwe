package com.kiwe.domain.usecase

interface SignUpUseCase {
    suspend operator fun invoke(): Result<String>
}
