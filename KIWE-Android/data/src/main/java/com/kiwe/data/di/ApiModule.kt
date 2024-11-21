package com.kiwe.data.di

import com.kiwe.data.network.service.KioskManagerService
import com.kiwe.data.network.service.OrderService
import com.kiwe.data.network.service.UserService
import com.kiwe.data.network.service.VoiceService
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
    fun provideUserService(
        @Spring httpClient: HttpClient,
    ): UserService = UserService(httpClient)

    @Provides
    @Singleton
    fun provideOrderService(
        @Spring httpClient: HttpClient,
    ): OrderService = OrderService(httpClient)

    @Provides
    @Singleton
    fun provideVoiceService(
        @Fast httpClient: HttpClient,
    ): VoiceService = VoiceService(httpClient)

    @Provides
    @Singleton
    fun provideKioskManagerService(
        @Spring httpClient: HttpClient,
    ): KioskManagerService = KioskManagerService(httpClient)
}
