package com.kiwe.data.usecase

import com.kiwe.data.model.request.OrderRequest
import com.kiwe.data.model.request.toRequest
import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.model.Order
import com.kiwe.domain.usecase.kiosk.OrderUseCase
import timber.log.Timber
import javax.inject.Inject

class OrderUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : OrderUseCase {
        override suspend fun invoke(order: Order): Result<String> {
            val orderRequest =
                OrderRequest(
                    menuOrders = order.menuOrders.map { it.toRequest() },
                )
            val response = orderService.order(requestBody = orderRequest)
            Timber.tag(javaClass.simpleName).d("response : $response")
            return Result.success(response.toString())
        }
    }
