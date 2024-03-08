package com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok.KelompokSayaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.status.UpdateStatusKelompokBackwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.status.UpdateStatusKelompokForwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface KelompokSayaRemoteRepository {

	suspend fun getKelompokSaya(
		apiToken: String,
	): Resource<KelompokSayaRemoteResponse>

	suspend fun updateStatusKelompokForward(
		apiToken: String,
	): Resource<UpdateStatusKelompokForwardRemoteResponse>

	suspend fun updateStatusKelompokBackward(
		apiToken: String,
	): Resource<UpdateStatusKelompokBackwardRemoteResponse>

	suspend fun addKelompokIndividu(
		apiToken: String,
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody,
	): Resource<AddKelompokIndividuRemoteResponse>

	suspend fun addKelompokPunyaKelompok(
		apiToken: String,
		addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody,
	): Resource<AddKelompokPunyaKelompokRemoteResponse>
}

class KelompokSayaRemoteRepositoryImpl @Inject constructor(
	private val dataSource: KelompokSayaRemoteDataSource,
) : KelompokSayaRemoteRepository {

	override suspend fun getKelompokSaya(
		apiToken: String,
	): Resource<KelompokSayaRemoteResponse> {
		return proceed {
			dataSource.getKelompokSaya(apiToken)
		}
	}

	override suspend fun updateStatusKelompokForward(
		apiToken: String,
	): Resource<UpdateStatusKelompokForwardRemoteResponse> {
		return proceed {
			dataSource.updateStatusKelompokForward(apiToken)
		}
	}
	override suspend fun updateStatusKelompokBackward(
		apiToken: String,
	): Resource<UpdateStatusKelompokBackwardRemoteResponse> {
		return proceed {
			dataSource.updateStatusKelompokBackward(apiToken)
		}
	}

	override suspend fun addKelompokIndividu(
		apiToken: String,
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody,
	): Resource<AddKelompokIndividuRemoteResponse> {
		return proceed {
			dataSource.addKelompokIndividu(apiToken, addKelompokIndividuRemoteRequestBody)
		}
	}

	override suspend fun addKelompokPunyaKelompok(
		apiToken: String,
		addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody,
	): Resource<AddKelompokPunyaKelompokRemoteResponse> {
		return proceed {
			dataSource.addKelompokPunyaKelompok(apiToken, addKelompokPunyaKelompokRemoteRequestBody)
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