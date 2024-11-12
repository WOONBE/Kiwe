package com.kiwe.data.network.service

import com.kiwe.data.model.request.OrderRequest
import com.kiwe.data.model.response.OrderResponse
import com.kiwe.data.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import timber.log.Timber
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

        suspend fun confirmPayment(kioskId: Int): Boolean =
            runCatching {
                val response = client.get("/api/orders/payment/$kioskId")
                val responseBody = response.body<String>()
                if (responseBody == "COMPLETED") {
                    Timber.tag(javaClass.simpleName).d("결제 확인")
                    true
                } else {
                    Timber.tag(javaClass.simpleName).d("결제 실패")
                    false
                }
            }.onFailure { e ->
                Timber.tag(javaClass.simpleName).e("결제 확인 실패: ${e.message}")
            }.getOrDefault(false) // 실패 시 기본값 false 반환

        suspend fun cancelPayment(kioskId: Int): Boolean =
            runCatching {
                val response = client.delete("/api/orders/payment/$kioskId") { }

                if (response.status == HttpStatusCode.NoContent) {
                    Timber.tag(javaClass.simpleName).d("결제 취소 성공")
                    true
                } else {
                    val errorBody = response.body<String>()
                    throw Exception("Failed to delete order: $errorBody")
                }
            }.onFailure { e ->
                Timber.tag(javaClass.simpleName).e("결제 취소 실패: ${e.message}")
            }.getOrDefault(false)

        suspend fun getLastMonthIncome(): Result<Int> = client.getResult("api/orders/total-price/last-month")

        suspend fun getOrder(orderId: Int): Result<com.kiwe.domain.model.OrderResponse> = client.getResult("api/orders/$orderId")

        suspend fun checkOrderStatus(kioskId: Int): Result<String> = client.getResult("api/orders/payment/$kioskId")

        suspend fun getKioskTotalOrdersLast6Months(kioskId: Int): Result<Map<String, Int>> =
            client.getResult("api/orders/monthly-sales/last-six-months/$kioskId")

        suspend fun getKioskTotalOrdersLastMonth(kioskId: Int): Result<Int> = client.getResult("api/orders/total-price/last-month/$kioskId")

        // suspend fun getOrderAll():
    }
