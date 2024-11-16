package com.kiwe.domain.usecase.order

interface GetRecentSixMonthOrderUseCase {
    suspend operator fun invoke(): Result<Map<String, Int>>
}
