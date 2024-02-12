package com.kel022322.sicapstonedantatekkom.data.remote.repository.topik

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.topik.TopikRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response.TopikRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface TopikRemoteRepository {

	suspend fun getTopikCapstone(
		apiToken: String,
	): Resource<TopikRemoteResponse>

}

class TopikRemoteRepositoryImpl @Inject constructor(
	private val dataSource: TopikRemoteDataSource,
) : TopikRemoteRepository {

	override suspend fun getTopikCapstone(
		apiToken: String,
	): Resource<TopikRemoteResponse> {
		return proceed {
			dataSource.getTopikCapstone(apiToken)
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