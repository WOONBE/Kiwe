package com.kiwe.domain.usecase.kiosk.datasource

interface GetOwnerIdUseCase {
    suspend operator fun invoke(): String?
}
