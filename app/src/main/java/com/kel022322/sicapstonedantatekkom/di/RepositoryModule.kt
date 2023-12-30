package com.kel022322.sicapstonedantatekkom.di

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.AuthRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.AuthRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.AuthRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
	@Binds
	abstract fun bindsAuthRemoteRepository(authRemoteRepositoryImpl: AuthRemoteRepositoryImpl): AuthRemoteRepository

}