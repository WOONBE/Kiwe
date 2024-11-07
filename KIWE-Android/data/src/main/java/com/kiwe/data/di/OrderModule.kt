package com.kiwe.data.di

import com.kiwe.data.usecase.OrderUseCaseImpl
import com.kiwe.domain.usecase.kiosk.OrderUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OrderModule {
    @Binds
    fun bindOrderUseCase(uc: OrderUseCaseImpl): OrderUseCase
}
