package com.kiwe.data.di

import com.kiwe.data.usecase.GetTokenUseCaseImpl
import com.kiwe.data.usecase.SetTokenUseCaseImpl
import com.kiwe.domain.usecase.manager.token.GetTokenUseCase
import com.kiwe.domain.usecase.manager.token.SetTokenUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TokenModule {
    @Binds
    fun bindGetTokenUseCase(uc: GetTokenUseCaseImpl): GetTokenUseCase

    @Binds
    fun bindSetTokenUseCase(uc: SetTokenUseCaseImpl): SetTokenUseCase
}
