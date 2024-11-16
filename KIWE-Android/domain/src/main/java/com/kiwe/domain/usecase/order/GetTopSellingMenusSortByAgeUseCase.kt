package com.kiwe.domain.usecase.order

interface GetTopSellingMenusSortByAgeUseCase {
    suspend operator fun invoke(): Result<Map<String, Map<String, Int>>>
}
