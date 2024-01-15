package com.kel022322.sicapstonedantatekkom.data.remote.repository.file

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.file.FileRemoteDataSource
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

	suspend fun uploadLaporanProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		laporan_ta: MultipartBody.Part
	) : Resource<UploadLaporanProcessRemoteResponse>

	suspend fun uploadC100Process(
		userId: String,
		apiToken: String,
		id: String,
		c100: MultipartBody.Part
	) : Resource<UploadC100ProcessRemoteResponse>

	suspend fun uploadC200Process(
		userId: String,
		apiToken: String,
		id: String,
		c200: MultipartBody.Part
	) : Resource<UploadC200ProcessRemoteResponse>

	suspend fun uploadC300Process(
		userId: String,
		apiToken: String,
		id: String,
		c300: MultipartBody.Part
	) : Resource<UploadC300ProcessRemoteResponse>

	suspend fun uploadC400Process(
		userId: String,
		apiToken: String,
		id: String,
		c400: MultipartBody.Part
	) : Resource<UploadC400ProcessRemoteResponse>

	suspend fun uploadC500Process(
		userId: String,
		apiToken: String,
		id: String,
		c500: MultipartBody.Part
	) : Resource<UploadC500ProcessRemoteResponse>

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

	override suspend fun uploadLaporanProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		laporan_ta: MultipartBody.Part,
	): Resource<UploadLaporanProcessRemoteResponse> {
		return proceed {
			dataSource.uploadLaporanProcess(userId, apiToken, idMahasiswa, laporan_ta)
		}
	}

	override suspend fun uploadC100Process(
		userId: String,
		apiToken: String,
		id: String,
		c100: MultipartBody.Part,
	): Resource<UploadC100ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC100Process(userId, apiToken, id, c100)
		}
	}

	override suspend fun uploadC200Process(
		userId: String,
		apiToken: String,
		id: String,
		c200: MultipartBody.Part,
	): Resource<UploadC200ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC200Process(userId, apiToken, id, c200)
		}
	}

	override suspend fun uploadC300Process(
		userId: String,
		apiToken: String,
		id: String,
		c300: MultipartBody.Part,
	): Resource<UploadC300ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC300Process(userId, apiToken, id, c300)
		}
	}

	override suspend fun uploadC400Process(
		userId: String,
		apiToken: String,
		id: String,
		c400: MultipartBody.Part,
	): Resource<UploadC400ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC400Process(userId, apiToken, id, c400)
		}
	}

	override suspend fun uploadC500Process(
		userId: String,
		apiToken: String,
		id: String,
		c500: MultipartBody.Part,
	): Resource<UploadC500ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC500Process(userId, apiToken, id, c500)
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