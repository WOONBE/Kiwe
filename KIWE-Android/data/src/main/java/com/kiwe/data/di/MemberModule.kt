package com.kiwe.data.di

import com.kiwe.data.usecase.manager.edit.EditMemberInfoUseCaseImpl
import com.kiwe.data.usecase.manager.edit.EditMyInfoUseCaseImpl
import com.kiwe.data.usecase.manager.login.LogoutUseCaseImpl
import com.kiwe.data.usecase.manager.search.SearchAllMemberUseCaseImpl
import com.kiwe.data.usecase.manager.search.SearchMemberByEmailUseCaseImpl
import com.kiwe.data.usecase.manager.search.SearchMemberByIdUseCaseImpl
import com.kiwe.data.usecase.manager.search.SearchMyInfoUseCaseImpl
import com.kiwe.domain.usecase.manager.edit.EditMemberInfoUseCase
import com.kiwe.domain.usecase.manager.edit.EditMyInfoUseCase
import com.kiwe.domain.usecase.manager.login.LogoutUseCase
import com.kiwe.domain.usecase.manager.search.SearchAllMemberUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByEmailUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByIdUseCase
import com.kiwe.domain.usecase.manager.search.SearchMyInfoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MemberModule {
    @Binds
    fun bindSearchMemberByEmailUseCase(uc: SearchMemberByEmailUseCaseImpl): SearchMemberByEmailUseCase

    @Binds
    fun bindSearchMemberByIdUseCase(uc: SearchMemberByIdUseCaseImpl): SearchMemberByIdUseCase

    @Binds
    fun bindSearchAllMemberUseCase(uc: SearchAllMemberUseCaseImpl): SearchAllMemberUseCase

    @Binds
    fun bindLogoutUseCase(uc: LogoutUseCaseImpl): LogoutUseCase

    @Binds
    fun bindEditMemberInfoUseCase(uc: EditMemberInfoUseCaseImpl): EditMemberInfoUseCase

    @Binds
    fun bindSearchMyInfoUseCase(uc: SearchMyInfoUseCaseImpl): SearchMyInfoUseCase

    @Binds
    fun bindEditMyInfoUseCase(uc: EditMyInfoUseCaseImpl): EditMyInfoUseCase
}
