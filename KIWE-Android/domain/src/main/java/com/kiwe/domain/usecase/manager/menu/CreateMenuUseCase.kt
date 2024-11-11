package com.kiwe.domain.usecase.manager.menu

import com.kiwe.domain.model.CreateMenuParam
import com.kiwe.domain.model.MenuCategoryParam

interface CreateMenuUseCase {
    suspend operator fun invoke(createMenuParam: CreateMenuParam): Result<MenuCategoryParam>
}