package com.kiwe.domain.usecase.kiosk.datasource

interface SetKioskNameUseCase {
    suspend operator fun invoke(name: String)
}
