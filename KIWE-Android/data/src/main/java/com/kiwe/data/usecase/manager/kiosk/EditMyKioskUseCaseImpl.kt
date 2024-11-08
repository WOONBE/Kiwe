package com.kiwe.data.usecase.manager.kiosk

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.model.CreateKioskRequest
import com.kiwe.domain.usecase.manager.kiosk.EditMyKioskUseCase
import javax.inject.Inject

class EditMyKioskUseCaseImpl
    @Inject
    constructor(
        private val kioskService: KioskManagerService,
    ) : EditMyKioskUseCase {
        override suspend fun invoke(
            kioskId: Int,
            request: CreateKioskRequest,
        ) {
            // TODO: 깔끔하게 만들 수 있는 방법 생각하기
            kioskService.editMyKiosk(kioskId, request).getOrThrow()
        }
    }
