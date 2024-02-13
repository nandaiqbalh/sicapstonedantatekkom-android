package com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen

import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface DosenRemoteDataSource {

	suspend fun getDataDosen(
		apiToken: String,
	): DosenRemoteResponse
}

class DosenRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService,
) : DosenRemoteDataSource {

	override suspend fun getDataDosen(
		apiToken: String,
	): DosenRemoteResponse {
		return apiService.getDataDosen(apiToken)
	}
}