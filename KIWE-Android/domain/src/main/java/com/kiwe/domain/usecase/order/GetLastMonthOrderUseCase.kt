package com.kiwe.domain.usecase.order

interface GetLastMonthOrderUseCase {
    suspend operator fun invoke(): Result<Int>
}
