package com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast.BroadcastRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.allbroadcast.AllBroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request.BroadcastDetailRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.response.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface BroadcastRemoteRepository {
	suspend fun getBroadcast(
	): Resource<AllBroadcastRemoteResponse>

	suspend fun getBroadcastHome(
	): Resource<BroadcastPaginateRemoteResponse>

	suspend fun getBroadcastDetail(
		broadcastDetailRemoteRequestBody: BroadcastDetailRemoteRequestBody,
	): Resource<BroadcastDetailRemoteResponse>
}

class BroadcastRemoteRepositoryImpl @Inject constructor(private val dataSource: BroadcastRemoteDataSource) :
	BroadcastRemoteRepository {
	override suspend fun getBroadcast(
	): Resource<AllBroadcastRemoteResponse> {
		return proceed {
			dataSource.getBroadcast()
		}
	}

	override suspend fun getBroadcastHome(
	): Resource<BroadcastPaginateRemoteResponse> {
		return proceed {
			dataSource.getBroadcastHome()
		}
	}

	override suspend fun getBroadcastDetail(
		broadcastDetailRemoteRequestBody: BroadcastDetailRemoteRequestBody,
	): Resource<BroadcastDetailRemoteResponse> {
		return proceed {
			dataSource.getBroadcastDetail(broadcastDetailRemoteRequestBody)
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