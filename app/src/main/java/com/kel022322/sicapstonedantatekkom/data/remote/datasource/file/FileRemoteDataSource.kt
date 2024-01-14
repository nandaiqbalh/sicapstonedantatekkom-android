package com.kel022322.sicapstonedantatekkom.data.remote.datasource.file

import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface FileRemoteDataSource {

	suspend fun getFileIndex(
		fileIndexRemoteRequestBody: FileIndexRemoteRequestBody
	) : FileIndexRemoteResponse
}

class FileRemoteDataSourceImpl @Inject constructor(
	private val apiService: ApiService
) : FileRemoteDataSource {

	override suspend fun getFileIndex(fileIndexRemoteRequestBody: FileIndexRemoteRequestBody): FileIndexRemoteResponse {
		return apiService.getFileIndex(fileIndexRemoteRequestBody)
	}
}