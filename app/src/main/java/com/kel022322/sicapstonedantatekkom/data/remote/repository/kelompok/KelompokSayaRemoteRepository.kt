package com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok.KelompokSayaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface KelompokSayaRemoteRepository {

	suspend fun getKelompokSaya(
		getKelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody
	) : Resource<KelompokSayaRemoteResponse>

	suspend fun addKelompokIndividu(
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody
	) : Resource<AddKelompokIndividuRemoteResponse>

	suspend fun addKelompokPunyaKelompok(
		addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody
	) : Resource<AddKelompokPunyaKelompokRemoteResponse>
}

class KelompokSayaRemoteRepositoryImpl @Inject constructor(
	private val dataSource: KelompokSayaRemoteDataSource
) : KelompokSayaRemoteRepository {

	override suspend fun getKelompokSaya(getKelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody): Resource<KelompokSayaRemoteResponse> {
		return proceed {
			dataSource.getKelompokSaya(getKelompokSayaRemoteRequestBody)
		}
	}

	override suspend fun addKelompokIndividu(addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody): Resource<AddKelompokIndividuRemoteResponse> {
		return proceed {
			dataSource.addKelompokIndividu(addKelompokIndividuRemoteRequestBody)
		}
	}

	override suspend fun addKelompokPunyaKelompok(addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody): Resource<AddKelompokPunyaKelompokRemoteResponse> {
		return proceed {
			dataSource.addKelompokPunyaKelompok(addKelompokPunyaKelompokRemoteRequestBody)
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