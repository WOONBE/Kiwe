package com.kiwe.data.usecase

import com.kiwe.data.model.response.TestResponse
import com.kiwe.data.network.service.UserService
import com.kiwe.data.network.util.ApiResponse
import com.kiwe.data.network.util.emitApiResponse
import com.kiwe.domain.usecase.SignUpUseCase
import javax.inject.Inject

class SignUpUseCaseImpl
    @Inject
    constructor(
        private val userService: UserService,
    ) : SignUpUseCase {
        override suspend fun invoke(): Result<String> {
            val response =
                emitApiResponse(
                    apiResponse = { userService.singUp() },
                    default = TestResponse(),
                )
            return when (response) {
                is ApiResponse.Success -> {
                    Result.success(response.data.toString())
                }

                is ApiResponse.Error -> {
                    Result.success("에러")
                }
            }
        }
    }
