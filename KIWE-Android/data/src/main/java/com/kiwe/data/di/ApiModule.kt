package com.kiwe.data.di

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.data.network.service.OrderService
import com.kiwe.data.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideUserService(httpClient: HttpClient): UserService = UserService(httpClient)

    @Provides
    @Singleton
    fun provideOrderService(httpClient: HttpClient): OrderService = OrderService(httpClient)

    @Provides
    @Singleton
    fun provideKioskManagerService(httpClient: HttpClient): KioskManagerService = KioskManagerService(httpClient)
}
