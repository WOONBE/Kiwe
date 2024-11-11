package com.kiwe.data.di

import com.kiwe.data.usecase.manager.search.SearchMemberByEmailUseCaseImpl
import com.kiwe.domain.usecase.manager.search.SearchMemberByEmailUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MemberModule {
    @Binds
    fun bindSearchMemberByEmailUseCase(uc: SearchMemberByEmailUseCaseImpl): SearchMemberByEmailUseCase
}
