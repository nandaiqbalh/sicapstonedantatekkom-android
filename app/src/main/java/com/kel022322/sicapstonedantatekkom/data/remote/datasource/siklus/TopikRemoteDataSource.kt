package com.kel022322.sicapstonedantatekkom.data.remote.datasource.siklus

import com.kel022322.sicapstonedantatekkom.data.remote.model.siklus.response.SiklusRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface SiklusRemoteDataSource {

	suspend fun getSiklusCapstone(
		apiToken: String,
	): SiklusRemoteResponse

}

class SiklusRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService,
) : SiklusRemoteDataSource {

	override suspend fun getSiklusCapstone(
		apiToken: String,
	): SiklusRemoteResponse {
		return apiService.getSiklusCapstone(apiToken)
	}

}