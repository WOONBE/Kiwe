package com.kiwe.data.usecase.manager.menu

import com.kiwe.data.network.service.MenuService
import com.kiwe.domain.usecase.manager.menu.DeleteMenuUseCase
import javax.inject.Inject

class DeleteMenuUseCaseImpl
    @Inject
    constructor(
        private val menuService: MenuService,
    ) : DeleteMenuUseCase {
        override suspend fun invoke(menuId: Int): Result<Unit> = menuService.deleteMenu(menuId)
    }
