package com.kiwe.domain.usecase.manager.kiosk

interface SignOutKioskUseCase {
    suspend operator fun invoke(password: String)
}
