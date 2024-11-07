package com.kiwe.data.network.util

import com.kiwe.domain.exception.APIException
import com.kiwe.domain.exception.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
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
