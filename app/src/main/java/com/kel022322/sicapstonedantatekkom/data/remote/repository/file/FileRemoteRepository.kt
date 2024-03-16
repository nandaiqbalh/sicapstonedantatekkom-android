package com.kel022322.sicapstonedantatekkom.data.remote.repository.file

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.file.FileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c100.response.UploadC100ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response.UploadC200ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response.UploadC300ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response.UploadC400ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response.UploadC500ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response.UploadLaporanProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

interface FileRemoteRepository {

	suspend fun getFileIndex(
		apiToken: String,
	): Resource<FileIndexRemoteResponse>

	suspend fun uploadMakalahProcess(
		apiToken: String,
		makalah: MultipartBody.Part,
	): Resource<UploadMakalahProcessRemoteResponse>

	suspend fun uploadLaporanProcess(
		apiToken: String,
		laporan_ta: MultipartBody.Part,
	): Resource<UploadLaporanProcessRemoteResponse>

	suspend fun uploadC100Process(
		apiToken: String,
		idKelompok: String,
		c100: MultipartBody.Part,
	): Resource<UploadC100ProcessRemoteResponse>

	suspend fun uploadC200Process(
		apiToken: String,
		idKelompok: String,
		c200: MultipartBody.Part,
	): Resource<UploadC200ProcessRemoteResponse>

	suspend fun uploadC300Process(
		apiToken: String,
		idKelompok: String,
		c300: MultipartBody.Part,
	): Resource<UploadC300ProcessRemoteResponse>

	suspend fun uploadC400Process(
		apiToken: String,
		idKelompok: String,
		c400: MultipartBody.Part,
	): Resource<UploadC400ProcessRemoteResponse>

	suspend fun uploadC500Process(
		apiToken: String,
		idKelompok: String,
		c500: MultipartBody.Part,
	): Resource<UploadC500ProcessRemoteResponse>

}

class FileRemoteRepositoryImpl @Inject constructor(
	private val dataSource: FileRemoteDataSource,
) : FileRemoteRepository {

	override suspend fun getFileIndex(apiToken: String): Resource<FileIndexRemoteResponse> {
		return proceed {
			dataSource.getFileIndex(apiToken)
		}
	}

	override suspend fun uploadMakalahProcess(
		apiToken: String,
		makalah: MultipartBody.Part,
	): Resource<UploadMakalahProcessRemoteResponse> {
		return proceed {
			dataSource.uploadMakalahProcess(apiToken, makalah)
		}
	}

	override suspend fun uploadLaporanProcess(
		apiToken: String,
		laporan_ta: MultipartBody.Part,
	): Resource<UploadLaporanProcessRemoteResponse> {
		return proceed {
			dataSource.uploadLaporanProcess(apiToken, laporan_ta)
		}
	}

	override suspend fun uploadC100Process(
		apiToken: String,
		idKelompok: String,
		c100: MultipartBody.Part,
	): Resource<UploadC100ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC100Process( apiToken, idKelompok, c100)
		}
	}

	override suspend fun uploadC200Process(
		apiToken: String,
		idKelompok: String,
		c200: MultipartBody.Part,
	): Resource<UploadC200ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC200Process( apiToken, idKelompok, c200)
		}
	}

	override suspend fun uploadC300Process(
		apiToken: String,
		idKelompok: String,
		c300: MultipartBody.Part,
	): Resource<UploadC300ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC300Process( apiToken, idKelompok, c300)
		}
	}

	override suspend fun uploadC400Process(
		apiToken: String,
		idKelompok: String,
		c400: MultipartBody.Part,
	): Resource<UploadC400ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC400Process( apiToken, idKelompok,  c400)
		}
	}

	override suspend fun uploadC500Process(
		apiToken: String,
		idKelompok: String,
		c500: MultipartBody.Part,
	): Resource<UploadC500ProcessRemoteResponse> {
		return proceed {
			dataSource.uploadC500Process(apiToken, idKelompok, c500)
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