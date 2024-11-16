package com.kiwe.data.usecase

import com.kiwe.data.model.request.OrderRequest
import com.kiwe.data.model.request.toRequest
import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.model.Order
import com.kiwe.domain.usecase.kiosk.PostOrderUseCase
import timber.log.Timber
import javax.inject.Inject

class PostOrderUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : PostOrderUseCase {
        override suspend fun invoke(
            kioskId: Int,
            age: Int,
            gender: String,
            order: Order,
        ): Result<String> {
            val genderInt =
                when (gender) {
                    "Male" -> 1
                    else -> 2
                }
            val ageInt =
                when (age) {
                    in 0..19 -> 10
                    in 20..29 -> 20
                    in 30..39 -> 30
                    in 40..49 -> 40
                    else -> 50
                }
            val orderRequest =
                OrderRequest(
                    kioskId = kioskId,
                    age = ageInt,
                    gender = genderInt,
                    menuOrders = order.menuOrders.map { it.toRequest() },
                )
            Timber.tag("결제 post 요청").d(orderRequest.toString())
            val response = orderService.order(requestBody = orderRequest)
            Timber.tag("결제 post 응답").d("response : $response")
            return Result.success(response.toString())
        }
    }
