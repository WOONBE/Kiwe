package com.kiwe.data.usecase.manager.kiosk

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.domain.model.CreateKioskRequest
import com.kiwe.domain.model.Kiosk
import com.kiwe.domain.usecase.manager.kiosk.CreateKioskUseCase
import timber.log.Timber
import javax.inject.Inject

class CreateKioskUseCaseImpl
    @Inject
    constructor(
        private val kioskService: KioskManagerService,
    ) : CreateKioskUseCase {
        override suspend fun invoke(kiosk: CreateKioskRequest): Result<Kiosk> =
            runCatching {
                val response = kioskService.createKiosk(kiosk)
                Timber.tag(javaClass.simpleName).d("키오스크 생성 성공: $response")
                response
            }.getOrElse { e ->
                Timber.tag(javaClass.simpleName).e("키오스크 생성 중 에러 발생: $e")
                throw e
            }
    }
