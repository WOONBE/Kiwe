package com.kiwe.data.usecase.manager.menu

import com.kiwe.data.network.service.MenuService
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.model.MenuParam
import com.kiwe.domain.usecase.manager.menu.CreateMenuUseCase
import javax.inject.Inject

class CreateMenuUseCaseImpl
    @Inject
    constructor(
        private val menuService: MenuService,
    ) : CreateMenuUseCase {
        override suspend fun invoke(createMenuParam: MenuParam): Result<MenuCategoryParam> = menuService.createMenu(createMenuParam)
    }
