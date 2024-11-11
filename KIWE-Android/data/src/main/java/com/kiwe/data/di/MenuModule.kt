package com.kiwe.data.di

import com.kiwe.data.usecase.GetCategoryListUseCaseImpl
import com.kiwe.data.usecase.manager.menu.CreateMenuUseCaseImpl
import com.kiwe.data.usecase.manager.menu.EditMenuUseCaseImpl
import com.kiwe.data.usecase.manager.menu.GetAllMenuListUseCaseImpl
import com.kiwe.data.usecase.manager.menu.GetMenuByIdUseCaseImpl
import com.kiwe.domain.usecase.GetCategoryListUseCase
import com.kiwe.domain.usecase.manager.menu.CreateMenuUseCase
import com.kiwe.domain.usecase.manager.menu.EditMenuUseCase
import com.kiwe.domain.usecase.manager.menu.GetAllMenuListUseCase
import com.kiwe.domain.usecase.manager.menu.GetMenuByIdUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MenuModule {
    @Binds
    fun bindGetCategoryListUseCase(uc: GetCategoryListUseCaseImpl): GetCategoryListUseCase

    @Binds
    fun bindGetMenuByIdUseCase(uc: GetMenuByIdUseCaseImpl): GetMenuByIdUseCase

    @Binds
    fun bindGetAllMenuListUseCase(uc: GetAllMenuListUseCaseImpl): GetAllMenuListUseCase

    @Binds
    fun bindCreateMenuUseCase(uc: CreateMenuUseCaseImpl): CreateMenuUseCase

    @Binds
    fun bindEditMenuUseCase(uc: EditMenuUseCaseImpl): EditMenuUseCase
}
