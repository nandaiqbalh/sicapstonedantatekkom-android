package com.kel022322.sicapstonedantatekkom.di

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.login.AuthLoginRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.login.AuthLoginRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.logout.AuthLogoutRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.logout.AuthLogoutRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast.BroadcastRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast.BroadcastRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

	// auth
	@Binds
	abstract fun provideAuthRemoteDataSource(authRemoteDataSourceImpl: AuthLoginRemoteDataSourceImpl): AuthLoginRemoteDataSource

	@Binds
	abstract fun provideAuthLogoutRemoteDataSource(authLogoutRemoteDataSourceImpl: AuthLogoutRemoteDataSourceImpl) : AuthLogoutRemoteDataSource

	// broadcast
	@Binds
	abstract fun provideBroadcastRemoteDataSource(broadcastRemoteDataSourceImpl: BroadcastRemoteDataSourceImpl): BroadcastRemoteDataSource

}