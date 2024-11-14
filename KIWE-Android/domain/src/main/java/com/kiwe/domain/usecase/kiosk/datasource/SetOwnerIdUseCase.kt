package com.kiwe.domain.usecase.kiosk.datasource

interface SetOwnerIdUseCase {
    suspend operator fun invoke(id: String)
}
