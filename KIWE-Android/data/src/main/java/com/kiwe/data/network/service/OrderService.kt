package com.kiwe.data.network.service

import com.kiwe.data.model.request.OrderRequest
import com.kiwe.data.model.response.OrderResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class OrderService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        suspend fun order(requestBody: OrderRequest): OrderResponse =
            client
                .post("api/orders") {
                    setBody(requestBody)
                    contentType(ContentType.Application.Json)
                }.body()
    }
