package com.kiwe.domain.usecase.order

import com.kiwe.domain.model.OrderResponse

interface GetOrderUseCase {
    suspend operator fun invoke(orderId: Int): Result<OrderResponse>
}
