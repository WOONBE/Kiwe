package com.kiwe.data.di

import com.kiwe.data.usecase.CancelPaymentUseCaseImpl
import com.kiwe.data.usecase.ConfirmPaymentUseCaseImpl
import com.kiwe.data.usecase.PostOrderUseCaseImpl
import com.kiwe.data.usecase.VoiceOrderUseCaseImpl
import com.kiwe.data.usecase.VoiceRecommendUseCaseImpl
import com.kiwe.data.usecase.manager.order.CheckOrderStatusUseCaseImpl
import com.kiwe.data.usecase.manager.order.GetKioskTotalOrdersLast6MonthsUseCaseImpl
import com.kiwe.data.usecase.manager.order.GetKioskTotalOrdersLastMonthUseCaseImpl
import com.kiwe.data.usecase.manager.order.GetLastMonthIncomeUseCaseImpl
import com.kiwe.data.usecase.manager.order.GetOrderAllUseCaseImpl
import com.kiwe.data.usecase.manager.order.GetOrderUseCaseImpl
import com.kiwe.domain.usecase.VoiceOrderUseCase
import com.kiwe.domain.usecase.VoiceRecommendUseCase
import com.kiwe.domain.usecase.kiosk.CancelPaymentUseCase
import com.kiwe.domain.usecase.kiosk.ConfirmPaymentUseCase
import com.kiwe.domain.usecase.kiosk.PostOrderUseCase
import com.kiwe.domain.usecase.order.CheckOrderStatusUseCase
import com.kiwe.domain.usecase.order.GetKioskTotalOrdersLast6MonthsUseCase
import com.kiwe.domain.usecase.order.GetKioskTotalOrdersLastMonthUseCase
import com.kiwe.domain.usecase.order.GetLastMonthIncomeUseCase
import com.kiwe.domain.usecase.order.GetOrderAllUseCase
import com.kiwe.domain.usecase.order.GetOrderUseCase
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

    @Binds
    fun bindVoiceOrderUseCase(uc: VoiceOrderUseCaseImpl): VoiceOrderUseCase

    @Binds
    fun bindVoiceRecommendUseCase(uc: VoiceRecommendUseCaseImpl): VoiceRecommendUseCase

    @Binds
    fun bindGetLastMonthIncomeUseCase(uc: GetLastMonthIncomeUseCaseImpl): GetLastMonthIncomeUseCase

    @Binds
    fun bindGetOrderUseCase(uc: GetOrderUseCaseImpl): GetOrderUseCase

    @Binds
    fun bindCheckOrderStatusUseCase(uc: CheckOrderStatusUseCaseImpl): CheckOrderStatusUseCase

    @Binds
    fun bindGetKioskTotalOrdersLast6MonthsUseCase(uc: GetKioskTotalOrdersLast6MonthsUseCaseImpl): GetKioskTotalOrdersLast6MonthsUseCase

    @Binds
    fun bindGetKioskTotalOrdersLastMonthUseCase(uc: GetKioskTotalOrdersLastMonthUseCaseImpl): GetKioskTotalOrdersLastMonthUseCase

    @Binds
    fun bindGetOrderAllUseCase(uc: GetOrderAllUseCaseImpl): GetOrderAllUseCase
}
