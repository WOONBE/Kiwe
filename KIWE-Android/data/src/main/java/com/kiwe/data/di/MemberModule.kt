package com.kiwe.data.di

import com.kiwe.data.usecase.manager.search.SearchAllMemberUseCaseImpl
import com.kiwe.data.usecase.manager.search.SearchMemberByEmailUseCaseImpl
import com.kiwe.data.usecase.manager.search.SearchMemberByIdUseCaseImpl
import com.kiwe.domain.usecase.manager.search.SearchAllMemberUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByEmailUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByIdUseCase
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
}
