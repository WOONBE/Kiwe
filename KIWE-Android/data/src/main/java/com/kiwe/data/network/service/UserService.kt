package com.kiwe.data.network.service

import com.kiwe.data.model.response.TestResponse
import com.kiwe.data.network.util.ApiResponse
import com.kiwe.data.network.util.BaseResponse
import com.kiwe.data.network.util.postResult
import com.kiwe.domain.model.LoginParam
import com.kiwe.domain.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

        suspend fun login(request: LoginParam): Result<LoginResponse> =
            client.postResult(url = "api/members/login") {
                parameter("email", request.email)
                parameter("password", request.password)
                contentType(ContentType.Application.Json)
            }

        suspend fun logout(): ApiResponse<TestResponse> = ApiResponse.Success(TestResponse())
    }
