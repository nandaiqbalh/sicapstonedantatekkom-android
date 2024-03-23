package com.kel022322.sicapstonedantatekkom.data.remote.repository.dosen

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen.DosenRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing1.DosenPembimbing1RemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing2.DosenPembimbing2RemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface DosenRemoteRepository {

	suspend fun getDataDosenPembimbing1(
		apiToken: String,
	): Resource<DosenPembimbing1RemoteResponse>

	suspend fun getDataDosenPembimbing2(
		apiToken: String,
	): Resource<DosenPembimbing2RemoteResponse>
}

class DosenRemoteRepositoryImpl @Inject constructor(
	private val dataSource: DosenRemoteDataSource,
) : DosenRemoteRepository {

	override suspend fun getDataDosenPembimbing1(apiToken: String): Resource<DosenPembimbing1RemoteResponse> {
		return proceed {
			dataSource.getDataDosenPembimbing1(apiToken)
		}
	}

	override suspend fun getDataDosenPembimbing2(apiToken: String): Resource<DosenPembimbing2RemoteResponse> {
		return proceed {
			dataSource.getDataDosenPembimbing2(apiToken)
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