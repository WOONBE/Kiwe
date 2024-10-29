package com.kiwe.data.network.service

import com.kiwe.data.model.request.LoginRequest
import com.kiwe.data.model.response.TestResponse
import com.kiwe.data.network.util.ApiResponse
import com.kiwe.data.network.util.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import timber.log.Timber
import javax.inject.Inject

class UserService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        suspend fun singUp(): BaseResponse<TestResponse> {
            Timber.tag(javaClass.simpleName).d("singUp")
            return client.get("test/api/test01").body()
        }

        suspend fun login(request: LoginRequest): ApiResponse<Unit> = ApiResponse.Success(Unit)

        suspend fun logout(): ApiResponse<TestResponse> = ApiResponse.Success(TestResponse())
    }
