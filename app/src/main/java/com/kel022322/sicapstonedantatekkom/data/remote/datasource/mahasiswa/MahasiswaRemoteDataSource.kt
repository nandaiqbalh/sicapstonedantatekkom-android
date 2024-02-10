package com.kel022322.sicapstonedantatekkom.data.remote.datasource.mahasiswa

import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.request.MahasiswaIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface MahasiswaRemoteDataSource {

	suspend fun getDataMahasiswa(
		mahasiswaIndexRemoteRequestBody: MahasiswaIndexRemoteRequestBody
	) : MahasiswaIndexRemoteResponse
}

class MahasiswaRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService
) : MahasiswaRemoteDataSource {

	override suspend fun getDataMahasiswa(mahasiswaIndexRemoteRequestBody: MahasiswaIndexRemoteRequestBody): MahasiswaIndexRemoteResponse {
		return apiService.getDataMahasiswa(mahasiswaIndexRemoteRequestBody)
	}
}