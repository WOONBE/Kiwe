package com.kiwe.domain.usecase.order

interface GetKioskTotalOrdersLastMonthUseCase {
    suspend operator fun invoke(kioskId: Int): Result<Int>
}
