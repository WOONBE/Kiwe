package com.kiwe.data.di

import com.kiwe.data.usecase.manager.kiosk.CreateKioskUseCaseImpl
import com.kiwe.data.usecase.manager.kiosk.DeleteKioskUseCaseImpl
import com.kiwe.data.usecase.manager.kiosk.EditKioskUseCaseImpl
import com.kiwe.data.usecase.manager.kiosk.EditMyKioskUseCaseImpl
import com.kiwe.data.usecase.manager.kiosk.GetAllKioskUseCaseImpl
import com.kiwe.data.usecase.manager.kiosk.GetAllMyKioskUseCaseImpl
import com.kiwe.data.usecase.manager.kiosk.SignOutKioskUseCaseImpl
import com.kiwe.domain.usecase.manager.kiosk.CreateKioskUseCase
import com.kiwe.domain.usecase.manager.kiosk.DeleteKioskUseCase
import com.kiwe.domain.usecase.manager.kiosk.EditKioskUseCase
import com.kiwe.domain.usecase.manager.kiosk.EditMyKioskUseCase
import com.kiwe.domain.usecase.manager.kiosk.GetAllKioskUseCase
import com.kiwe.domain.usecase.manager.kiosk.GetAllMyKioskUseCase
import com.kiwe.domain.usecase.manager.kiosk.SignOutKioskUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface KioskManagerModule {
    @Binds
    fun bindDeleteKioskUseCase(uc: DeleteKioskUseCaseImpl): DeleteKioskUseCase

    @Binds
    fun bindGetAllMyKioskUseCase(uc: GetAllMyKioskUseCaseImpl): GetAllMyKioskUseCase

    @Binds
    fun bindGetAllKioskUseCase(uc: GetAllKioskUseCaseImpl): GetAllKioskUseCase

    @Binds
    fun bindCreateKioskUseCase(uc: CreateKioskUseCaseImpl): CreateKioskUseCase

    @Binds
    fun bindSignOutKioskUseCase(uc: SignOutKioskUseCaseImpl): SignOutKioskUseCase

    @Binds
    fun bindEditKioskUseCase(uc: EditKioskUseCaseImpl): EditKioskUseCase

    @Binds
    fun bindEditMyKioskUseCase(uc: EditMyKioskUseCaseImpl): EditMyKioskUseCase
}
