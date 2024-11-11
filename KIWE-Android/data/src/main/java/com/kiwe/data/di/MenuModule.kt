package com.kiwe.data.di

import com.kiwe.data.usecase.GetCategoryListUseCaseImpl
import com.kiwe.data.usecase.manager.menu.GetMenuByIdUseCaseImpl
import com.kiwe.domain.usecase.GetCategoryListUseCase
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
}
