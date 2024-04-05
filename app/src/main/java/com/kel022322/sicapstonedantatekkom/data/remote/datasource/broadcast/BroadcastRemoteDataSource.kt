package com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast

import com.kel022322.sicapstonedantatekkom.data.remote.model.beranda.response.BerandaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.allbroadcast.AllBroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request.BroadcastDetailRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.response.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface BroadcastRemoteDataSource {
	suspend fun getBroadcast(): AllBroadcastRemoteResponse

	suspend fun getBroadcastHome(): BroadcastPaginateRemoteResponse

	suspend fun getBroadcastDetail(
		broadcastDetailRemoteRequestBody: BroadcastDetailRemoteRequestBody,
	): BroadcastDetailRemoteResponse

	suspend fun getDataBeranda(
		apiToken: String,
	): BerandaRemoteResponse

}

class BroadcastRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	BroadcastRemoteDataSource {
	override suspend fun getBroadcast(
	): AllBroadcastRemoteResponse {
		return apiService.getBroadcast()
	}

	override suspend fun getBroadcastHome(
	): BroadcastPaginateRemoteResponse {
		return apiService.getBroadcastHome()
	}

	override suspend fun getBroadcastDetail(
		broadcastDetailRemoteRequestBody: BroadcastDetailRemoteRequestBody,
	): BroadcastDetailRemoteResponse {
		return apiService.getBroadcastDetail(broadcastDetailRemoteRequestBody)
	}

	override suspend fun getDataBeranda(apiToken: String): BerandaRemoteResponse {
		return apiService.getDataBeranda(apiToken)
	}
}