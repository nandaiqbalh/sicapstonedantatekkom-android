package com.kel022322.sicapstonedantatekkom.data.remote.datasource.file

import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c100.response.UploadC100ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response.UploadC200ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response.UploadC300ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response.UploadC400ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response.UploadC500ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response.UploadLaporanProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

interface FileRemoteDataSource {

	suspend fun getFileIndex(
		apiToken: String,
	): FileIndexRemoteResponse


	suspend fun uploadMakalahProcess(
		apiToken: String,
		makalah: MultipartBody.Part,
	): UploadMakalahProcessRemoteResponse

	suspend fun uploadLaporanProcess(
		apiToken: String,
		laporan_ta: MultipartBody.Part,
	): UploadLaporanProcessRemoteResponse

	suspend fun uploadC100Process(
		apiToken: String,
		idKelompok: String,
		c100: MultipartBody.Part,
	): UploadC100ProcessRemoteResponse

	suspend fun uploadC200Process(
		apiToken: String,
		idKelompok: String,
		c200: MultipartBody.Part,
	): UploadC200ProcessRemoteResponse

	suspend fun uploadC300Process(
		apiToken: String,
		idKelompok: String,
		c300: MultipartBody.Part,
	): UploadC300ProcessRemoteResponse

	suspend fun uploadC400Process(
		apiToken: String,
		idKelompok: String,
		c400: MultipartBody.Part,
	): UploadC400ProcessRemoteResponse

	suspend fun uploadC500Process(
		apiToken: String,
		idKelompok: String,
		c500: MultipartBody.Part,
	): UploadC500ProcessRemoteResponse

}

class FileRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService,
) : FileRemoteDataSource {

	override suspend fun getFileIndex(apiToken: String): FileIndexRemoteResponse {
		return apiService.getFileIndex(apiToken)
	}

	override suspend fun uploadMakalahProcess(
		apiToken: String,
		makalah: MultipartBody.Part,
	): UploadMakalahProcessRemoteResponse {
		return apiService.uploadMakalahProcess(apiToken, makalah)
	}

	override suspend fun uploadLaporanProcess(
		apiToken: String,
		laporan_ta: MultipartBody.Part,
	): UploadLaporanProcessRemoteResponse {
		return apiService.uploadLaporanProcess(apiToken, laporan_ta)
	}

	override suspend fun uploadC100Process(
		apiToken: String,
		idKelompok: String,
		c100: MultipartBody.Part,
	): UploadC100ProcessRemoteResponse {
		return apiService.uploadC100Process(apiToken, idKelompok, c100)
	}

	override suspend fun uploadC200Process(
		apiToken: String,
		idKelompok: String,
		c200: MultipartBody.Part,
	): UploadC200ProcessRemoteResponse {
		return apiService.uploadC200Process(apiToken, idKelompok, c200)
	}

	override suspend fun uploadC300Process(
		apiToken: String,
		idKelompok: String,
		c300: MultipartBody.Part,
	): UploadC300ProcessRemoteResponse {
		return apiService.uploadC300Process(apiToken, idKelompok, c300)
	}

	override suspend fun uploadC400Process(
		apiToken: String,
		idKelompok: String,
		c400: MultipartBody.Part,
	): UploadC400ProcessRemoteResponse {
		return apiService.uploadC400Process(apiToken, idKelompok, c400)
	}

	override suspend fun uploadC500Process(
		apiToken: String,
		idKelompok: String,
		c500: MultipartBody.Part,
	): UploadC500ProcessRemoteResponse {
		return apiService.uploadC500Process(apiToken, idKelompok, c500)
	}

}