package com.kiwe.domain.usecase.kiosk.datasource

interface GetKioskNameUseCase {
    suspend operator fun invoke(): String
}
