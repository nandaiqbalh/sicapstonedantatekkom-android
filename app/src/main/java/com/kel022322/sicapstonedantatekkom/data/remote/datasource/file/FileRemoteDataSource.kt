package com.kel022322.sicapstonedantatekkom.data.remote.datasource.file

import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c100.response.UploadC100ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response.UploadC200ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response.UploadC300ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response.UploadC400ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response.UploadC500ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response.UploadLaporanProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.request.ViewPdfRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.response.ViewPdfRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

interface FileRemoteDataSource {

	suspend fun getFileIndex(
		fileIndexRemoteRequestBody: FileIndexRemoteRequestBody
	) : FileIndexRemoteResponse

	suspend fun viewPdf(
		viewPdfRemoteRequestBody: ViewPdfRemoteRequestBody
	) : ViewPdfRemoteResponse

	suspend fun uploadMakalahProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		makalah: MultipartBody.Part
	) : UploadMakalahProcessRemoteResponse

	suspend fun uploadLaporanProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		laporan_ta: MultipartBody.Part
	) : UploadLaporanProcessRemoteResponse

	suspend fun uploadC100Process(
		userId: String,
		apiToken: String,
		id: String,
		c100: MultipartBody.Part
	) : UploadC100ProcessRemoteResponse

	suspend fun uploadC200Process(
		userId: String,
		apiToken: String,
		id: String,
		c200: MultipartBody.Part
	) : UploadC200ProcessRemoteResponse

	suspend fun uploadC300Process(
		userId: String,
		apiToken: String,
		id: String,
		c300: MultipartBody.Part
	) : UploadC300ProcessRemoteResponse

	suspend fun uploadC400Process(
		userId: String,
		apiToken: String,
		id: String,
		c400: MultipartBody.Part
	) : UploadC400ProcessRemoteResponse

	suspend fun uploadC500Process(
		userId: String,
		apiToken: String,
		id: String,
		c500: MultipartBody.Part
	) : UploadC500ProcessRemoteResponse
}

class FileRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService
) : FileRemoteDataSource {

	override suspend fun getFileIndex(fileIndexRemoteRequestBody: FileIndexRemoteRequestBody): FileIndexRemoteResponse {
		return apiService.getFileIndex(fileIndexRemoteRequestBody)
	}

	override suspend fun viewPdf(viewPdfRemoteRequestBody: ViewPdfRemoteRequestBody): ViewPdfRemoteResponse {
		return apiService.viewPdf(viewPdfRemoteRequestBody)
	}

	override suspend fun uploadMakalahProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		makalah: MultipartBody.Part,
	): UploadMakalahProcessRemoteResponse {
		return apiService.uploadMakalahProcess(userId, apiToken, idMahasiswa, makalah)
	}

	override suspend fun uploadLaporanProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		laporan_ta: MultipartBody.Part,
	): UploadLaporanProcessRemoteResponse {
		return apiService.uploadLaporanProcess(userId, apiToken, idMahasiswa, laporan_ta)
	}

	override suspend fun uploadC100Process(
		userId: String,
		apiToken: String,
		id: String,
		c100: MultipartBody.Part,
	): UploadC100ProcessRemoteResponse {
		return apiService.uploadC100Process(userId, apiToken, id, c100)
	}

	override suspend fun uploadC200Process(
		userId: String,
		apiToken: String,
		id: String,
		c200: MultipartBody.Part,
	): UploadC200ProcessRemoteResponse {
		return apiService.uploadC200Process(userId, apiToken, id, c200)
	}

	override suspend fun uploadC300Process(
		userId: String,
		apiToken: String,
		id: String,
		c300: MultipartBody.Part,
	): UploadC300ProcessRemoteResponse {
		return apiService.uploadC300Process(userId, apiToken, id, c300)
	}

	override suspend fun uploadC400Process(
		userId: String,
		apiToken: String,
		id: String,
		c400: MultipartBody.Part,
	): UploadC400ProcessRemoteResponse {
		return apiService.uploadC400Process(userId, apiToken, id, c400)
	}

	override suspend fun uploadC500Process(
		userId: String,
		apiToken: String,
		id: String,
		c500: MultipartBody.Part,
	): UploadC500ProcessRemoteResponse {
		return apiService.uploadC500Process(userId, apiToken, id, c500)
	}
}