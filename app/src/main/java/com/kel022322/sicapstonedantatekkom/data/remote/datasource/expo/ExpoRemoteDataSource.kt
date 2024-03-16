package com.kel022322.sicapstonedantatekkom.data.remote.datasource.expo

import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.request.DaftarExpoRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.response.DaftarExpoRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response.ExpoIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface ExpoRemoteDataSource {

	suspend fun getDataExpo(
		apiToken: String,
	): ExpoIndexRemoteResponse

	suspend fun daftarExpo(
		apiToken: String,
		daftarExpoRemoteRequestBody: DaftarExpoRemoteRequestBody
	): DaftarExpoRemoteResponse

}

class ExpoRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	ExpoRemoteDataSource {

	override suspend fun getDataExpo(
		apiToken: String,
	): ExpoIndexRemoteResponse {
		return apiService.getDataExpo(apiToken)
	}

	override suspend fun daftarExpo(
		apiToken: String,
		daftarExpoRemoteRequestBody: DaftarExpoRemoteRequestBody
	): DaftarExpoRemoteResponse {
		return apiService.daftarExpo(apiToken, daftarExpoRemoteRequestBody)
	}

}