package com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok

import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface KelompokSayaRemoteDataSource {

	suspend fun getKelompokSaya(
		kelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody
	) : KelompokSayaRemoteResponse
}

class KelompokSayaRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService
) : KelompokSayaRemoteDataSource {

	override suspend fun getKelompokSaya(kelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody): KelompokSayaRemoteResponse {
		return apiService.getKelompokSaya(kelompokSayaRemoteRequestBody)
	}
}