package com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok.KelompokSayaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface KelompokSayaRemoteRepository {

	suspend fun getKelompokSaya(
		getKelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody
	) : Resource<KelompokSayaRemoteResponse>

}

class KelompokSayaRemoteRepositoryImpl @Inject constructor(
	private val dataSource: KelompokSayaRemoteDataSource
) : KelompokSayaRemoteRepository {

	override suspend fun getKelompokSaya(getKelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody): Resource<KelompokSayaRemoteResponse> {
		return proceed {
			dataSource.getKelompokSaya(getKelompokSayaRemoteRequestBody)
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