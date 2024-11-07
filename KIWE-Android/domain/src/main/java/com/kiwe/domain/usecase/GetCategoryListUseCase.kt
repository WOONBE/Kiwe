package com.kiwe.domain.usecase

import com.kiwe.domain.model.MenuCategory

interface GetCategoryListUseCase {
    suspend operator fun invoke(category: String): Result<List<MenuCategory>>
}
