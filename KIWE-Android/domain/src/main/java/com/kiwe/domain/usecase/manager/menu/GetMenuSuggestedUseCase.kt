package com.kiwe.domain.usecase.manager.menu

import com.kiwe.domain.model.MenuCategoryParam

interface GetMenuSuggestedUseCase {
    suspend operator fun invoke(): Result<List<MenuCategoryParam>>
}
