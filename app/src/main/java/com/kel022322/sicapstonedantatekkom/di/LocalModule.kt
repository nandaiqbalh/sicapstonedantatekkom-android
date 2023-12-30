package com.kel022322.sicapstonedantatekkom.di

import android.content.Context
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

	@Provides
	fun provideAuthDataStoreManager(@ApplicationContext context: Context): AuthDataStoreManager =
		AuthDataStoreManager(context)

}