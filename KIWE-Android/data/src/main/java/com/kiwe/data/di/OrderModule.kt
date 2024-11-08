package com.kiwe.data.di

import com.kiwe.data.usecase.CancelPaymentUseCaseImpl
import com.kiwe.data.usecase.ConfirmPaymentUseCaseImpl
import com.kiwe.data.usecase.PostOrderUseCaseImpl
import com.kiwe.domain.usecase.kiosk.CancelPaymentUseCase
import com.kiwe.domain.usecase.kiosk.ConfirmPaymentUseCase
import com.kiwe.domain.usecase.kiosk.PostOrderUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OrderModule {
    @Binds
    fun bindPostOrderUseCase(uc: PostOrderUseCaseImpl): PostOrderUseCase

    @Binds
    fun bindConfirmPaymentUseCase(uc: ConfirmPaymentUseCaseImpl): ConfirmPaymentUseCase

    @Binds
    fun bindCancelPaymentUseCase(uc: CancelPaymentUseCaseImpl): CancelPaymentUseCase
}
