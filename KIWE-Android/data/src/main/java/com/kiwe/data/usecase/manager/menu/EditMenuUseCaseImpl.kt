package com.kiwe.data.usecase.manager.menu

import com.kiwe.data.network.service.MenuService
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.model.MenuParam
import com.kiwe.domain.usecase.manager.menu.EditMenuUseCase
import javax.inject.Inject

class EditMenuUseCaseImpl
    @Inject
    constructor(
        private val menuService: MenuService,
    ) : EditMenuUseCase {
        override suspend fun invoke(
            menuId: Int,
            menuParam: MenuParam,
        ): Result<MenuCategoryParam> = menuService.editMenu(menuId, menuParam)
    }
