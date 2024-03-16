package com.kel022322.sicapstonedantatekkom.data.remote.datasource.topik

import com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response.TopikRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface TopikRemoteDataSource {

	suspend fun getTopikCapstone(
		apiToken: String,
	): TopikRemoteResponse

}

class TopikRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService,
) : TopikRemoteDataSource {

	override suspend fun getTopikCapstone(
		apiToken: String,
	): TopikRemoteResponse {
		return apiService.getTopikCapstone(apiToken)
	}

}