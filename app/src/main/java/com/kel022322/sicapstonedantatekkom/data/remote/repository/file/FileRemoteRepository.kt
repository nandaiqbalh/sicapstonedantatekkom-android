package com.kel022322.sicapstonedantatekkom.data.remote.repository.file

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.file.FileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface FileRemoteRepository {

	suspend fun getFileIndex(
		fileIndexRemoteRequestBody: FileIndexRemoteRequestBody,
	): Resource<FileIndexRemoteResponse>
}

class FileRemoteRepositoryImpl @Inject constructor(
	private val dataSource: FileRemoteDataSource,
) : FileRemoteRepository {

	override suspend fun getFileIndex(fileIndexRemoteRequestBody: FileIndexRemoteRequestBody): Resource<FileIndexRemoteResponse> {
		return proceed {
			dataSource.getFileIndex(fileIndexRemoteRequestBody)
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