package com.kel022322.sicapstonedantatekkom.data.remote.repository.mahasiswa

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.mahasiswa.MahasiswaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.request.MahasiswaIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface MahasiswaRemoteRepository {

	suspend fun getDataMahasiswa(
		mahasiswaIndexRemoteRequestBody: MahasiswaIndexRemoteRequestBody
	) : Resource<MahasiswaIndexRemoteResponse>

}

class MahasiswaRemoteRepositoryImpl @Inject constructor(
	private val dataSource: MahasiswaRemoteDataSource
) : MahasiswaRemoteRepository {

	override suspend fun getDataMahasiswa(mahasiswaIndexRemoteRequestBody: MahasiswaIndexRemoteRequestBody): Resource<MahasiswaIndexRemoteResponse> {
		return proceed {
			dataSource.getDataMahasiswa(mahasiswaIndexRemoteRequestBody)
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