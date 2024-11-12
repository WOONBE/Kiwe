package com.kiwe.data.usecase.manager.menu

import com.kiwe.data.network.service.MenuService
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.manager.menu.GetAllMenuListUseCase
import javax.inject.Inject

class GetAllMenuListUseCaseImpl
    @Inject
    constructor(
        private val menuService: MenuService,
    ) : GetAllMenuListUseCase {
        override suspend fun invoke(): Result<List<MenuCategoryParam>> = menuService.getAllMenuList()
    }
