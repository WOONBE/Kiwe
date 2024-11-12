package com.kiwe.domain.usecase.manager.menu

import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.model.MenuParam

interface EditMenuUseCase {
    suspend operator fun invoke(menuId: Int, menuParam: MenuParam): Result<MenuCategoryParam>
}