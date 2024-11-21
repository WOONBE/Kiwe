package com.kiwe.domain.usecase.manager.menu

import com.kiwe.domain.model.MenuCategoryParam

interface GetMenuByIdUseCase {
    suspend operator fun invoke(menuId: Int): Result<MenuCategoryParam>
}
