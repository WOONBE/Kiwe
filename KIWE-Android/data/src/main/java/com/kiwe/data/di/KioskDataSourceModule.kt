package com.kiwe.data.di

import com.kiwe.data.usecase.kiosk.datasource.ClearKioskNameUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.ClearOrderNumberUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.ClearOwnerIdUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.ClearOwnerUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.GetKioskIdUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.GetKioskNameUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.GetOrderNumberUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.GetOwnerIdUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.SetKioskIdUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.SetKioskNameUseCaseImpl
import com.kiwe.data.usecase.kiosk.datasource.SetOwnerIdUseCaseImpl
import com.kiwe.domain.usecase.kiosk.datasource.ClearKioskNameUseCase
import com.kiwe.domain.usecase.kiosk.datasource.ClearOrderNumberUseCase
import com.kiwe.domain.usecase.kiosk.datasource.ClearOwnerIdUseCase
import com.kiwe.domain.usecase.kiosk.datasource.ClearOwnerUseCase
import com.kiwe.domain.usecase.kiosk.datasource.GetKioskIdUseCase
import com.kiwe.domain.usecase.kiosk.datasource.GetKioskNameUseCase
import com.kiwe.domain.usecase.kiosk.datasource.GetOrderNumberUseCase
import com.kiwe.domain.usecase.kiosk.datasource.GetOwnerIdUseCase
import com.kiwe.domain.usecase.kiosk.datasource.SetKioskIdUseCase
import com.kiwe.domain.usecase.kiosk.datasource.SetKioskNameUseCase
import com.kiwe.domain.usecase.kiosk.datasource.SetOwnerIdUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface KioskDataSourceModule {
    @Binds
    fun bindClearKioskNameUseCase(uc: ClearKioskNameUseCaseImpl): ClearKioskNameUseCase

    @Binds
    fun bindClearOrderNumberUseCase(uc: ClearOrderNumberUseCaseImpl): ClearOrderNumberUseCase

    @Binds
    fun bindClearOwnerIdUseCase(uc: ClearOwnerIdUseCaseImpl): ClearOwnerIdUseCase

    @Binds
    fun bindClearOwnerUseCase(uc: ClearOwnerUseCaseImpl): ClearOwnerUseCase

    @Binds
    fun bindGetKioskIdUseCase(uc: GetKioskIdUseCaseImpl): GetKioskIdUseCase

    @Binds
    fun bindGetKioskNameUseCase(uc: GetKioskNameUseCaseImpl): GetKioskNameUseCase

    @Binds
    fun bindGetOrderNumberUseCase(uc: GetOrderNumberUseCaseImpl): GetOrderNumberUseCase

    @Binds
    fun bindGetOwnerIdUseCase(uc: GetOwnerIdUseCaseImpl): GetOwnerIdUseCase

    @Binds
    fun bindSetKioskIdUseCase(uc: SetKioskIdUseCaseImpl): SetKioskIdUseCase

    @Binds
    fun bindSetKioskNameUseCase(uc: SetKioskNameUseCaseImpl): SetKioskNameUseCase

    @Binds
    fun bindSetOwnerIdUseCase(uc: SetOwnerIdUseCaseImpl): SetOwnerIdUseCase
}
