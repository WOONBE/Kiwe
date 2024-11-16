package com.kiwe.data.usecase.manager.menu

import com.kiwe.data.network.service.MenuService
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.manager.menu.GetMenuSuggestedUseCase
import jakarta.inject.Inject

class GetMenuSuggestedUseCaseImpl
    @Inject
    constructor(
        private val menuService: MenuService,
    ) : GetMenuSuggestedUseCase {
        override suspend fun invoke(): Result<List<MenuCategoryParam>> = menuService.getMenuSuggested()
    }
