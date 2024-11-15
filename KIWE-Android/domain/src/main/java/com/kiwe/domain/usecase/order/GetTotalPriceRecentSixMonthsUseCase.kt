package com.kiwe.domain.usecase.order

interface GetTotalPriceRecentSixMonthsUseCase {
    suspend operator fun invoke(): Result<Map<String, Int>>
}
