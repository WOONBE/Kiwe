package com.kiwe.domain.usecase.order

interface GetKioskTotalOrdersLast6MonthsUseCase {
    suspend operator fun invoke(kioskId: Int): Result<Map<String, Int>>
}
