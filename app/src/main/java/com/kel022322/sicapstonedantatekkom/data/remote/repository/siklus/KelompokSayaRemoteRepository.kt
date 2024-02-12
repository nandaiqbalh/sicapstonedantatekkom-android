package com.kel022322.sicapstonedantatekkom.data.remote.repository.siklus

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.siklus.SiklusRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.siklus.response.SiklusRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface SiklusRemoteRepository {

	suspend fun getSiklusCapstone(
		apiToken: String,
	): Resource<SiklusRemoteResponse>

}

class SiklusRemoteRepositoryImpl @Inject constructor(
	private val dataSource: SiklusRemoteDataSource,
) : SiklusRemoteRepository {

	override suspend fun getSiklusCapstone(
		apiToken: String,
	): Resource<SiklusRemoteResponse> {
		return proceed {
			dataSource.getSiklusCapstone(apiToken)
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