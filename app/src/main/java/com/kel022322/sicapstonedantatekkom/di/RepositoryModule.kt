package com.kel022322.sicapstonedantatekkom.di

import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.login.AuthLoginRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.login.AuthLoginRemoteRepositoryImpl
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.logout.AuthLogoutRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.logout.AuthLogoutRemoteRepositoryImpl
import com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast.BroadcastRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast.BroadcastRemoteRepositoryImpl
import com.kel022322.sicapstonedantatekkom.data.remote.repository.profile.ProfileRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.profile.ProfileRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
	@Binds
	abstract fun bindsAuthRemoteRepository(authRemoteRepositoryImpl: AuthLoginRemoteRepositoryImpl): AuthLoginRemoteRepository

	@Binds
	abstract fun bindsAuthLogoutRemoteRepository(authLogoutRemoteRepositoryImpl: AuthLogoutRemoteRepositoryImpl) : AuthLogoutRemoteRepository

	@Binds
	abstract fun bindsBroadcastRemoteRepository(broadcastRemoteRepositoryImpl: BroadcastRemoteRepositoryImpl): BroadcastRemoteRepository

	@Binds
	abstract fun bindsProfileRemoteRepository(profileRemoteRepositoryImpl: ProfileRemoteRepositoryImpl) : ProfileRemoteRepository
}