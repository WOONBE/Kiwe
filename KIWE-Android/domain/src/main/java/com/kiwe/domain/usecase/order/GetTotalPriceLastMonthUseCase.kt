package com.kiwe.domain.usecase.order

interface GetTotalPriceLastMonthUseCase {
    suspend operator fun invoke(): Result<Int>
}
