package com.kel022322.sicapstonedantatekkom.di

import com.kel022322.sicapstonedantatekkom.BuildConfig
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	private const val baseUrl = BuildConfig.BASE_URL

	@Singleton
	@Provides
	fun provideRetrofit(): Retrofit {
		val loggingInterceptor = HttpLoggingInterceptor()
			.setLevel(HttpLoggingInterceptor.Level.HEADERS)
		val client = OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.build()
		return Retrofit.Builder()
			.baseUrl(baseUrl)
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.build()
	}

	@Singleton
	@Provides
	fun provideApiServicePlaces(retrofit: Retrofit): ApiService =
		retrofit.create(ApiService::class.java)
}