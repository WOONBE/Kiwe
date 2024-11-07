package com.kiwe.data.di

import com.kiwe.data.usecase.GetCategoryListUseCaseImpl
import com.kiwe.domain.usecase.GetCategoryListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MenuModule {
    @Binds
    fun bindGetCategoryListUseCase(uc: GetCategoryListUseCaseImpl): GetCategoryListUseCase
}
