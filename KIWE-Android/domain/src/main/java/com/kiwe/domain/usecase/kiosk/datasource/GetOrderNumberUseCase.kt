package com.kiwe.domain.usecase.kiosk.datasource

interface GetOrderNumberUseCase {
    suspend operator fun invoke(): Int
}
