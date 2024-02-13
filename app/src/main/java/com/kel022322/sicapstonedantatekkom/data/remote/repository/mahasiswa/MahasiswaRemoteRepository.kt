package com.kel022322.sicapstonedantatekkom.data.remote.repository.mahasiswa

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.mahasiswa.MahasiswaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface MahasiswaRemoteRepository {

	suspend fun getDataMahasiswa(
		apiToken: String,
	): Resource<MahasiswaIndexRemoteResponse>

}

class MahasiswaRemoteRepositoryImpl @Inject constructor(
	private val dataSource: MahasiswaRemoteDataSource,
) : MahasiswaRemoteRepository {

	override suspend fun getDataMahasiswa(
		apiToken: String,
	): Resource<MahasiswaIndexRemoteResponse> {
		return proceed {
			dataSource.getDataMahasiswa(apiToken)
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