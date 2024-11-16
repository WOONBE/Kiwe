package com.kiwe.data.usecase.manager.order

import com.kiwe.data.network.service.OrderService
import com.kiwe.domain.usecase.order.GetTopSellingMenusSortByAgeUseCase
import jakarta.inject.Inject

class GetTopSellingMenusSortByAgeUseCaseImpl
    @Inject
    constructor(
        private val orderService: OrderService,
    ) : GetTopSellingMenusSortByAgeUseCase {
        override suspend operator fun invoke(): Result<Map<String, Map<String, Int>>> = orderService.getTopSellingMenusSortByAge()
    }
