package com.kiwe.data.usecase

import com.kiwe.data.network.service.MenuService
import com.kiwe.domain.model.MenuCategoryParam
import com.kiwe.domain.usecase.GetCategoryListUseCase
import javax.inject.Inject

class GetCategoryListUseCaseImpl
    @Inject
    constructor(
        private val menuService: MenuService,
    ) : GetCategoryListUseCase {
        override suspend fun invoke(category: String): Result<List<MenuCategoryParam>> = menuService.getCategoryList(category)
    }
