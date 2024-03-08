package com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangta

import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.request.DaftarSidangTARemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.response.DaftarSidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response.SidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status.UpdateStatusIndividuBackwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status.UpdateStatusIndividuForwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface SidangTARemoteDataSource {

	suspend fun getJadwalSidangTA(
		apiToken: String,
	): SidangTARemoteResponse

	suspend fun updateStatusIndividuForward(
		apiToken: String,
	): UpdateStatusIndividuForwardRemoteResponse

	suspend fun updateStatusIndividuBackward(
		apiToken: String,
	): UpdateStatusIndividuBackwardRemoteResponse


	suspend fun daftarSidangTA(
		apiToken: String,
		sidangTARemoteRequestBody: DaftarSidangTARemoteRequestBody
	): DaftarSidangTARemoteResponse

}

class SidangTARemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	SidangTARemoteDataSource {

	override suspend fun getJadwalSidangTA(
		apiToken: String,
	): SidangTARemoteResponse {
		return apiService.getJadwalSidangTA(apiToken)
	}

	override suspend fun updateStatusIndividuForward(apiToken: String): UpdateStatusIndividuForwardRemoteResponse {
		return apiService.updateStatusIndividuForward(apiToken)
	}

	override suspend fun updateStatusIndividuBackward(apiToken: String): UpdateStatusIndividuBackwardRemoteResponse {
		return apiService.updateStatusIndividuBackward(apiToken)
	}

	override suspend fun daftarSidangTA(
		apiToken: String,
		daftarSidangTARemoteRequestBody: DaftarSidangTARemoteRequestBody
	): DaftarSidangTARemoteResponse {
		return apiService.daftarSidangTA(apiToken, daftarSidangTARemoteRequestBody)
	}

}