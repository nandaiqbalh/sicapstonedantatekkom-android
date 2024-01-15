package com.kel022322.sicapstonedantatekkom.data.remote.datasource.file

import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
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
}