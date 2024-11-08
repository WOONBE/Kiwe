package com.kiwe.data.network.service

import com.kiwe.data.model.response.TestResponse
import com.kiwe.data.network.util.ApiResponse
import com.kiwe.data.network.util.BaseResponse
import com.kiwe.data.network.util.postResult
import com.kiwe.domain.model.LoginParam
import com.kiwe.domain.model.LoginResponse
import com.kiwe.data.network.util.postResult
import com.kiwe.domain.model.SignUpParam
import com.kiwe.domain.model.SignUpResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import timber.log.Timber
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class UserService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        suspend fun signUp(signUpParam: SignUpParam): Result<SignUpResponse> =
            client.postResult("api/members/register") {
                setBody(signUpParam)
                contentType(ContentType.Application.Json)
            }

        suspend fun login(request: LoginParam): Result<LoginResponse> =
            client.postResult(url = "api/members/login") {
                parameter("email", request.email)
                parameter("password", request.password)
                contentType(ContentType.Application.Json)
            }

//        suspend fun logout(): ApiResponse<TestResponse> = ApiResponse.Success(TestResponse())
    }
