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
            order: Order,
        ): Result<String> {
            val orderRequest =
                OrderRequest(
                    kioskId = kioskId,
                    menuOrders = order.menuOrders.map { it.toRequest() },
                )
            Timber.tag("결제").d("$kioskId")
            val response = orderService.order(requestBody = orderRequest)
            Timber.tag(javaClass.simpleName).d("response : $response")
            return Result.success(response.toString())
        }
    }
