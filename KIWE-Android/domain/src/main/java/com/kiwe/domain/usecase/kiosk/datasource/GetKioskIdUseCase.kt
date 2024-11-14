package com.kiwe.domain.usecase.kiosk.datasource

interface GetKioskIdUseCase {
    suspend operator fun invoke(): String?
}
