package com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen

import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing1.DosenPembimbing1RemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing2.DosenPembimbing2RemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface DosenRemoteDataSource {


	suspend fun getDataDosenPembimbing1(
		apiToken: String,
	): DosenPembimbing1RemoteResponse

	suspend fun getDataDosenPembimbing2(
		apiToken: String,
	): DosenPembimbing2RemoteResponse
}

class DosenRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService,
) : DosenRemoteDataSource {

	override suspend fun getDataDosenPembimbing1(apiToken: String): DosenPembimbing1RemoteResponse {
		return apiService.getDataDosenPembimbing1(apiToken)
	}

	override suspend fun getDataDosenPembimbing2(apiToken: String): DosenPembimbing2RemoteResponse {
		return apiService.getDataDosenPembimbing2(apiToken)
	}
}