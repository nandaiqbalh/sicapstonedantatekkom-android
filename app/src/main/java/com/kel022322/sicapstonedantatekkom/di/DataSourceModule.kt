package com.kel022322.sicapstonedantatekkom.di

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.AuthRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.AuthRemoteDataSourceImpl
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
	abstract fun provideAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

	// broadcast
	@Binds
	abstract fun provideBroadcastRemoteDataSource(broadcastRemoteDataSourceImpl: BroadcastRemoteDataSourceImpl): BroadcastRemoteDataSource

}