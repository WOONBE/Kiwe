package com.kiwe.data.network.util

import com.kiwe.domain.exception.APIException
import com.kiwe.domain.exception.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import kotlinx.serialization.json.Json.Default.decodeFromString

suspend inline fun <reified T> HttpClient.getResult(
    url: String,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Result<T> =
    try {
        val response = get(urlString = url, block = httpRequestBuilder)
        if (response.status.isSuccess()) {
            Result.success(decodeFromString(response.body()))
        } else {
            val errorBody = response.bodyAsText()
            val errorResponse = decodeFromString(ErrorResponse.serializer(), errorBody)
            Result.failure(
                APIException(
                    message = errorResponse.message,
                    code = errorResponse.code,
                    throwable = IOException("API error"),
                ),
            )
        }
    } catch (e: Exception) {
        Result.failure(
            APIException(
                message = e.message ?: "에러 메세지가 없습니다!",
                code = 1,
                throwable = e,
            ),
        )
    }

suspend inline fun <reified T> HttpClient.postResult(
    url: String,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Result<T> =
    try {
        val response = post(urlString = url, block = httpRequestBuilder)
        if (response.status.isSuccess()) {
            Result.success(response.body())
        } else {
            val errorBody = response.bodyAsText()
            val errorResponse = decodeFromString(ErrorResponse.serializer(), errorBody)
            Result.failure(
                APIException(
                    message = errorResponse.message,
                    code = errorResponse.code,
                    throwable = IOException("API error"),
                ),
            )
        }
    } catch (e: Exception) {
        Result.failure(
            APIException(
                message = e.message ?: "에러 메세지가 없습니다!",
                code = -1,
                throwable = e,
            ),
        )
    }

suspend inline fun <reified T> HttpClient.deleteResult(
    url: String,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Result<T> =
    try {
        val response = delete(urlString = url, block = httpRequestBuilder)
        if (response.status.isSuccess()) {
            Result.success(decodeFromString(response.body()))
        } else {
            val errorBody = response.bodyAsText()
            val errorResponse = decodeFromString(ErrorResponse.serializer(), errorBody)
            Result.failure(
                APIException(
                    message = errorResponse.message,
                    code = errorResponse.code,
                    throwable = IOException("API error"),
                ),
            )
        }
    } catch (e: Exception) {
        Result.failure(
            APIException(
                message = e.message ?: "에러 메세지가 없습니다!",
                code = 1,
                throwable = e,
            ),
        )
    }

suspend inline fun <reified T> HttpClient.patchResult(
    url: String,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Result<T> =
    try {
        val response = patch(urlString = url, block = httpRequestBuilder)
        if (response.status.isSuccess()) {
            Result.success(decodeFromString(response.body()))
        } else {
            val errorBody = response.bodyAsText()
            val errorResponse = decodeFromString(ErrorResponse.serializer(), errorBody)
            Result.failure(
                APIException(
                    message = errorResponse.message,
                    code = errorResponse.code,
                    throwable = IOException("API error"),
                ),
            )
        }
    } catch (e: Exception) {
        Result.failure(
            APIException(
                message = e.message ?: "에러 메세지가 없습니다!",
                code = 1,
                throwable = e,
            ),
        )
    }

suspend inline fun <reified T> HttpClient.putResult(
    url: String,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Result<T> =
    try {
        val response = put(urlString = url, block = httpRequestBuilder)
        if (response.status.isSuccess()) {
            Result.success(decodeFromString(response.body()))
        } else {
            val errorBody = response.bodyAsText()
            val errorResponse = decodeFromString(ErrorResponse.serializer(), errorBody)
            Result.failure(
                APIException(
                    message = errorResponse.message,
                    code = errorResponse.code,
                    throwable = IOException("API error"),
                ),
            )
        }
    } catch (e: Exception) {
        Result.failure(
            APIException(
                message = e.message ?: "에러 메세지가 없습니다!",
                code = 1,
                throwable = e,
            ),
        )
    }
