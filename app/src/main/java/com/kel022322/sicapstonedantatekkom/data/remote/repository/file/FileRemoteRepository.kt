package com.kel022322.sicapstonedantatekkom.data.remote.repository.file

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.file.FileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.request.ViewPdfRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.response.ViewPdfRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

interface FileRemoteRepository {

	suspend fun getFileIndex(
		fileIndexRemoteRequestBody: FileIndexRemoteRequestBody,
	): Resource<FileIndexRemoteResponse>

	suspend fun viewPdf(
		viewPdfRemoteRequestBody: ViewPdfRemoteRequestBody
	) : Resource<ViewPdfRemoteResponse>

	suspend fun uploadMakalahProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		makalah: MultipartBody.Part
	) : Resource<UploadMakalahProcessRemoteResponse>
}

class FileRemoteRepositoryImpl @Inject constructor(
	private val dataSource: FileRemoteDataSource,
) : FileRemoteRepository {

	override suspend fun getFileIndex(fileIndexRemoteRequestBody: FileIndexRemoteRequestBody): Resource<FileIndexRemoteResponse> {
		return proceed {
			dataSource.getFileIndex(fileIndexRemoteRequestBody)
		}
	}

	override suspend fun viewPdf(viewPdfRemoteRequestBody: ViewPdfRemoteRequestBody): Resource<ViewPdfRemoteResponse> {
		return proceed {
			dataSource.viewPdf(viewPdfRemoteRequestBody)
		}
	}

	override suspend fun uploadMakalahProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		makalah: MultipartBody.Part,
	): Resource<UploadMakalahProcessRemoteResponse> {
		return proceed {
			dataSource.uploadMakalahProcess(userId, apiToken, idMahasiswa, makalah)
		}
	}

	private suspend fun <T> proceed(coroutines: suspend () -> T): Resource<T> {
		return try {
			Resource.Success(coroutines.invoke())
		} catch (e: Exception) {
			Resource.Error(e)
		}
	}
}