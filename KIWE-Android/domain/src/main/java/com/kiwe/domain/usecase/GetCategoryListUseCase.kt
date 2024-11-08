package com.kiwe.domain.usecase

import com.kiwe.domain.model.MenuCategoryParam

interface GetCategoryListUseCase {
    suspend operator fun invoke(category: String): Result<List<MenuCategoryParam>>
}
