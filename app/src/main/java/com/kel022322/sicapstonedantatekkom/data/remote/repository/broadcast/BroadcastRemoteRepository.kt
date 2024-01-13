package com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast.BroadcastRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.BroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface BroadcastRemoteRepository {
	suspend fun getBroadcast(
	): Resource<BroadcastRemoteResponse>

	suspend fun getBroadcastDetail(
		id: String
	) : Resource<BroadcastDetailRemoteResponse>
}

class BroadcastRemoteRepositoryImpl @Inject constructor(private val dataSource: BroadcastRemoteDataSource) :
	BroadcastRemoteRepository {
	override suspend fun getBroadcast(
	): Resource<BroadcastRemoteResponse> {
		return proceed {
			dataSource.getBroadcast()
		}
	}

	override suspend fun getBroadcastDetail(id: String): Resource<BroadcastDetailRemoteResponse> {
		return proceed {
			dataSource.getBroadcastDetail(id)
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