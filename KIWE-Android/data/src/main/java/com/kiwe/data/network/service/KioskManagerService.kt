package com.kiwe.data.network.service

import com.kiwe.data.di.Spring
import com.kiwe.data.model.request.SignOutKioskRequest
import com.kiwe.data.network.util.deleteResult
import com.kiwe.data.network.util.getResult
import com.kiwe.data.network.util.postResult
import com.kiwe.data.network.util.putResult
import com.kiwe.domain.model.CreateKioskRequest
import com.kiwe.domain.model.Kiosk
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import javax.inject.Inject

class KioskManagerService
    @Inject
    constructor(
        @Spring
        private val client: HttpClient,
    ) {
        /**
         * Kiosk를 삭제합니다
         * @return Unit, HTTP CODE 204
         */
        suspend fun deleteKiosk(id: Int): Result<Unit> = client.deleteResult<Unit>("api/kiosks/$id")

        /**
         * 내 Kiosk를 가져옵니다.
         * @TODO: access token
         */
        suspend fun getMyKiosk(): Result<List<Kiosk>> =
            client
                .getResult<List<Kiosk>>("api/kiosks")

        /**
         * 모든 Kiosk를 가져옵니다.
         */
        suspend fun getAllKiosk(): Result<List<Kiosk>> =
            client
                .getResult<List<Kiosk>>("api/kiosks/all")

        /**
         * Kiosk를 생성합니다.
         * @TODO: access token
         */
        suspend fun createKiosk(request: CreateKioskRequest): Result<Kiosk> =
            client.postResult<Kiosk>("api/kiosks") {
                setBody(request)
            }

        /**
         * Kiosk에서 로그아웃합니다.
         * @TODO: access token
         *
         */
        suspend fun signOutKiosk(request: SignOutKioskRequest): Result<Unit> =
            client.postResult<Unit>("api/kiosks/log-out") {
                setBody(request)
            }

        /**
         * Kiosk를 수정합니다.
         */
        suspend fun editKiosk(
            kioskId: Int,
            request: CreateKioskRequest,
        ): Result<Unit> =
            client.putResult<Unit>("api/kiosks/$kioskId") {
                setBody(request)
            }

        /**
         * Kiosk를 수정합니다.
         * @TODO: access token
         */
        suspend fun editMyKiosk(
            kioskId: Int,
            request: CreateKioskRequest,
        ): Result<Unit> =
            client.putResult<Unit>("api/kiosks/my/$kioskId") {
                setBody(request)
            }
    }
