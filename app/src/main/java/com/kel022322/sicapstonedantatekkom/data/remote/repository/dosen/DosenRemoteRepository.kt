package com.kel022322.sicapstonedantatekkom.data.remote.repository.dosen

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen.DosenRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface DosenRemoteRepository {

	suspend fun getDataDosen(
		apiToken: String,
	): Resource<DosenRemoteResponse>

}

class DosenRemoteRepositoryImpl @Inject constructor(
	private val dataSource: DosenRemoteDataSource,
) : DosenRemoteRepository {

	override suspend fun getDataDosen(
		apiToken: String,
	): Resource<DosenRemoteResponse> {
		return proceed {
			dataSource.getDataDosen(apiToken)
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