package com.kiwe.domain.usecase.kiosk

import com.kiwe.domain.model.Order

interface PostOrderUseCase {
    suspend operator fun invoke(
        kioskId: Int,
        age: Int,
        gender: String,
        orderNumber: Int,
        order: Order,
    ): Result<String>
}
