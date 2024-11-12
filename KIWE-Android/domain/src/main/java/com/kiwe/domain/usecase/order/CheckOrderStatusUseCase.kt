package com.kiwe.domain.usecase.order

interface CheckOrderStatusUseCase {
    suspend operator fun invoke(kioskId: Int): Result<String>
}