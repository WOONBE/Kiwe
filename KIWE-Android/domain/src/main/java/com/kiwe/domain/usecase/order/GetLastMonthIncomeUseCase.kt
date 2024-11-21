package com.kiwe.domain.usecase.order

interface GetLastMonthIncomeUseCase {
    suspend operator fun invoke(): Result<Int>
}
