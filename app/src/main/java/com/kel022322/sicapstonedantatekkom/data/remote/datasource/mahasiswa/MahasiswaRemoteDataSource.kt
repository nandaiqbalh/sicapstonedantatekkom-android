package com.kel022322.sicapstonedantatekkom.data.remote.datasource.mahasiswa

import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface MahasiswaRemoteDataSource {

	suspend fun getDataMahasiswa(
		apiToken: String,
	): MahasiswaIndexRemoteResponse
}

class MahasiswaRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService,
) : MahasiswaRemoteDataSource {

	override suspend fun getDataMahasiswa(
		apiToken: String,
	): MahasiswaIndexRemoteResponse {
		return apiService.getDataMahasiswa(apiToken)
	}
}