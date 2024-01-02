package com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast.BroadcastRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.BroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface BroadcastRemoteRepository {
	suspend fun getBroadcast(
	): Resource<BroadcastRemoteResponse>
}

class BroadcastRemoteRepositoryImpl @Inject constructor(private val dataSource: BroadcastRemoteDataSource) :
	BroadcastRemoteRepository {
	override suspend fun getBroadcast(
	): Resource<BroadcastRemoteResponse> {
		return proceed {
			dataSource.getBroadcast()
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