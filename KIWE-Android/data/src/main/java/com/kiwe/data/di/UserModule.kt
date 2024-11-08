package com.kiwe.data.di

import com.kiwe.data.usecase.LoginUseCaseImpl
import com.kiwe.data.usecase.SignUpUseCaseImpl
import com.kiwe.data.usecase.manager.SignUpUseCaseImpl
import com.kiwe.domain.usecase.SignUpUseCase
import com.kiwe.domain.usecase.manager.login.LoginUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UserModule {
    @Binds
    fun bindSignUpUseCase(uc: SignUpUseCaseImpl): SignUpUseCase

    @Binds
    fun bindLoginUseCase(uc: LoginUseCaseImpl): LoginUseCase
}
