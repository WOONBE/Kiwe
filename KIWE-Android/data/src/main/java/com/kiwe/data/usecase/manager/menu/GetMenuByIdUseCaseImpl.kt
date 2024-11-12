package com.kiwe.data.usecase.manager.menu

import com.kiwe.data.network.service.MenuService
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.manager.menu.GetMenuByIdUseCase
import javax.inject.Inject

class GetMenuByIdUseCaseImpl
    @Inject
    constructor(
        private val menuService: MenuService,
    ) : GetMenuByIdUseCase {
        override suspend fun invoke(menuId: Int): Result<MenuCategoryParam> = menuService.getMenuById(menuId)
    }
