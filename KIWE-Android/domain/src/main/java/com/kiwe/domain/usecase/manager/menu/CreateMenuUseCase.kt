package com.kiwe.domain.usecase.manager.menu

import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.model.MenuParam

interface CreateMenuUseCase {
    suspend operator fun invoke(createMenuParam: MenuParam): Result<MenuCategoryParam>
}
