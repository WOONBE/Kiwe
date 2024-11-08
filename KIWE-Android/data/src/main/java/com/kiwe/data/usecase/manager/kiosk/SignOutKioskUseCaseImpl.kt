package com.kiwe.data.usecase.manager.kiosk

import com.kiwe.data.model.request.SignOutKioskRequest
import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.usecase.manager.kiosk.SignOutKioskUseCase
import javax.inject.Inject

class SignOutKioskUseCaseImpl
    @Inject
    constructor(
        private val kioskService: KioskManagerService,
    ) : SignOutKioskUseCase {
        override suspend fun invoke(password: String) {
            val refreshToken = ""

            kioskService.signOutKiosk(SignOutKioskRequest(refreshToken, password)).getOrThrow()
        }
    }
