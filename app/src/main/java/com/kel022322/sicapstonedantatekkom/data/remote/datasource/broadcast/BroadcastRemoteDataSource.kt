package com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast

import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.BroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface BroadcastRemoteDataSource {
	suspend fun getBroadcast(): BroadcastRemoteResponse

	suspend fun getBroadcastDetail(id: String): BroadcastDetailRemoteResponse
}

class BroadcastRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	BroadcastRemoteDataSource {
	override suspend fun getBroadcast(
	): BroadcastRemoteResponse {
		return apiService.getBroadcast()
	}

	override suspend fun getBroadcastDetail(id: String): BroadcastDetailRemoteResponse {
		return apiService.getBroadcastDetail(id)
	}
}