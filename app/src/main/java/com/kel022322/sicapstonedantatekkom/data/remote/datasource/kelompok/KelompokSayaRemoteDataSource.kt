package com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok

import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.editkelompok.request.EditKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.editkelompok.response.EditKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.terimakelompok.TerimaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.tolakkelompok.TolakKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface KelompokSayaRemoteDataSource {

	suspend fun getKelompokSaya(
		apiToken: String,
	): KelompokSayaRemoteResponse

	suspend fun terimaKelompok(
		apiToken: String,
	): TerimaKelompokRemoteResponse

	suspend fun tolakKelompok(
		apiToken: String,
	): TolakKelompokRemoteResponse

	suspend fun addKelompokIndividu(
		apiToken: String,
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody,
	): AddKelompokIndividuRemoteResponse

	suspend fun addKelompokPunyaKelompok(
		apiToken: String,
		addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody,
	): AddKelompokPunyaKelompokRemoteResponse

	suspend fun editInformasiKelompok(
		apiToken: String,
		editKelompokRemoteRequestBody: EditKelompokRemoteRequestBody
	): EditKelompokRemoteResponse
}

class KelompokSayaRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService,
) : KelompokSayaRemoteDataSource {

	override suspend fun getKelompokSaya(
		apiToken: String,
	): KelompokSayaRemoteResponse {
		return apiService.getKelompokSaya(apiToken)
	}

	override suspend fun terimaKelompok(apiToken: String): TerimaKelompokRemoteResponse {
		return apiService.terimaKelompok(apiToken)
	}

	override suspend fun tolakKelompok(apiToken: String): TolakKelompokRemoteResponse {
		return apiService.tolakKelompok(apiToken)
	}

	override suspend fun addKelompokIndividu(
		apiToken: String,
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody,
	): AddKelompokIndividuRemoteResponse {
		return apiService.addKelompokIndividu(apiToken, addKelompokIndividuRemoteRequestBody)
	}

	override suspend fun addKelompokPunyaKelompok(
		apiToken: String,
		addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody,
	): AddKelompokPunyaKelompokRemoteResponse {
		return apiService.addKelompokPunyaKelompok(
			apiToken,
			addKelompokPunyaKelompokRemoteRequestBody
		)
	}

	override suspend fun editInformasiKelompok(
		apiToken: String,
		editKelompokRemoteRequestBody: EditKelompokRemoteRequestBody,
	): EditKelompokRemoteResponse {
		return apiService.editInformasiKelompok(
			apiToken,
			editKelompokRemoteRequestBody
		)
	}
}