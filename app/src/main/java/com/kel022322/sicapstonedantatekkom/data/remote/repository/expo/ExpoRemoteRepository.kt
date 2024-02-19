package com.kel022322.sicapstonedantatekkom.data.remote.repository.expo

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.expo.ExpoRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.request.DaftarExpoRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.response.DaftarExpoRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response.ExpoIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface ExpoRemoteRepository {
	suspend fun getDataExpo(
		apiToken: String,
	): Resource<ExpoIndexRemoteResponse>

	suspend fun daftarExpo(
		apiToken: String,
		daftarExpoRemoteRequestBody: DaftarExpoRemoteRequestBody
	): Resource<DaftarExpoRemoteResponse>

}

class ExpoRemoteRepositoryImpl @Inject constructor(private val dataSource: ExpoRemoteDataSource) :
	ExpoRemoteRepository {

	override suspend fun getDataExpo(
		apiToken: String,
	): Resource<ExpoIndexRemoteResponse> {

		return proceed {
			dataSource.getDataExpo(apiToken)
		}
	}

	override suspend fun daftarExpo(
		apiToken: String,
		daftarExpoRemoteRequestBody: DaftarExpoRemoteRequestBody
	): Resource<DaftarExpoRemoteResponse> {
		return proceed {
			dataSource.daftarExpo(apiToken, daftarExpoRemoteRequestBody)
		}
	}


	private suspend fun <T> proceed(coroutines: suspend () -> T): Resource<T> {
		return try {
			Resource.Success(coroutines.invoke())
		} catch (e: Exception) {
			Resource.Error(e)
		}
	}
}