package com.kiwe.payment_termianl

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @PUT("api/orders/payment/{kioskId}")
    suspend fun sendPaymentRequest(
        @Path("kioskId") kioskId: String,
    ): Response<Unit>

    @GET("/")
    suspend fun test(): Response<Unit>
}
