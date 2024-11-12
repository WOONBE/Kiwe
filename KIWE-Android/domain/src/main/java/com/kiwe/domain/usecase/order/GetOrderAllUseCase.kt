package com.kiwe.domain.usecase.order

import com.kiwe.domain.model.OrderResponse

interface GetOrderAllUseCase {
    suspend operator fun invoke(): Result<List<OrderResponse>>
}
