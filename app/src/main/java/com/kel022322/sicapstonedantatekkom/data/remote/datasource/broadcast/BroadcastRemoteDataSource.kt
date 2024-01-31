package com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast

import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface BroadcastRemoteDataSource {
	suspend fun getBroadcast(): BroadcastPaginateRemoteResponse

	suspend fun getBroadcastHome(): BroadcastPaginateRemoteResponse

	suspend fun getBroadcastDetail(id: String): BroadcastDetailRemoteResponse
}

class BroadcastRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	BroadcastRemoteDataSource {
	override suspend fun getBroadcast(
	): BroadcastPaginateRemoteResponse {
		return apiService.getBroadcast()
	}

	override suspend fun getBroadcastHome(
	): BroadcastPaginateRemoteResponse {
		return apiService.getBroadcastHome()
	}

	override suspend fun getBroadcastDetail(id: String): BroadcastDetailRemoteResponse {
		return apiService.getBroadcastDetail(id)
	}
}