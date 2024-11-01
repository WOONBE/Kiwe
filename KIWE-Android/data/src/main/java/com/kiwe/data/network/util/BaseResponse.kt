package com.kiwe.data.network.util

import com.kiwe.data.exception.ApiException

data class BaseResponse<T>(
    val code: String,
    val data: T?,
    val message: String,
)

suspend fun <T> emitApiResponse(
    apiResponse: suspend () -> BaseResponse<T>,
    default: T,
): ApiResponse<T> =
    runCatching {
        apiResponse()
    }.fold(
        onSuccess = { result ->
            ApiResponse.Success(data = result.data ?: default)
        },
        onFailure = { e ->
            when (e) {
                is ApiException ->
                    ApiResponse.Error.ServerError(
                        errorCode = e.error.errorCode,
                        errorMessage = e.error.message,
                    )
                else ->
                    ApiResponse.Error.UnknownError(
                        errorMessage = e.message ?: "",
                    )
            }
        },
    )
