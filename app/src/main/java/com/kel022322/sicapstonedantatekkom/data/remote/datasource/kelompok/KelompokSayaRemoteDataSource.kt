package com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok

import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface KelompokSayaRemoteDataSource {

	suspend fun getKelompokSaya(
		kelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody
	) : KelompokSayaRemoteResponse

	suspend fun addKelompokIndividu(
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody
	) : AddKelompokIndividuRemoteResponse
}

class KelompokSayaRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService
) : KelompokSayaRemoteDataSource {

	override suspend fun getKelompokSaya(kelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody): KelompokSayaRemoteResponse {
		return apiService.getKelompokSaya(kelompokSayaRemoteRequestBody)
	}

	override suspend fun addKelompokIndividu(addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody): AddKelompokIndividuRemoteResponse {
		return apiService.addKelompokIndividu(addKelompokIndividuRemoteRequestBody)
	}
}