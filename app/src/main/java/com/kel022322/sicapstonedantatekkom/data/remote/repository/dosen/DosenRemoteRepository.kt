package com.kel022322.sicapstonedantatekkom.data.remote.repository.dosen

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen.DosenRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.request.DosenRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface DosenRemoteRepository {

	suspend fun getDataDosen(
		dosenRemoteRequestBody: DosenRemoteRequestBody
	) : Resource<DosenRemoteResponse>

}

class DosenRemoteRepositoryImpl @Inject constructor(
	private val dataSource: DosenRemoteDataSource
) : DosenRemoteRepository {

	override suspend fun getDataDosen(dosenRemoteRequestBody: DosenRemoteRequestBody): Resource<DosenRemoteResponse> {
		return proceed {
			dataSource.getDataDosen(dosenRemoteRequestBody)
		}
	}

	private suspend fun <T> proceed(coroutines: suspend () -> T ): Resource<T> {
		return try {
			Resource.Success(coroutines.invoke())
		} catch (e:Exception){
			Resource.Error(e)
		}
	}
}