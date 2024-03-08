package com.kel022322.sicapstonedantatekkom.data.remote.repository.sidangta

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangta.SidangTARemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.request.DaftarSidangTARemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.response.DaftarSidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response.SidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status.UpdateStatusIndividuBackwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status.UpdateStatusIndividuForwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface SidangTARemoteRepository {
	suspend fun getJadwalSidangTA(
		apiToken: String,
	): Resource<SidangTARemoteResponse>

	suspend fun updateStatusIndividuForward(
		apiToken: String,
	): Resource<UpdateStatusIndividuForwardRemoteResponse>

	suspend fun updateStatusIndividuBackward(
		apiToken: String,
	): Resource<UpdateStatusIndividuBackwardRemoteResponse>

	suspend fun daftarSidangTA(
		apiToken: String,
		daftarSidangTARemoteRequestBody: DaftarSidangTARemoteRequestBody
	): Resource<DaftarSidangTARemoteResponse>

}

class SidangTARemoteRepositoryImpl @Inject constructor(private val dataSource: SidangTARemoteDataSource) :
	SidangTARemoteRepository {

	override suspend fun getJadwalSidangTA(
		apiToken: String,
	): Resource<SidangTARemoteResponse> {

		return proceed {
			dataSource.getJadwalSidangTA(apiToken)
		}
	}

	override suspend fun updateStatusIndividuForward(apiToken: String): Resource<UpdateStatusIndividuForwardRemoteResponse> {
		return proceed {
			dataSource.updateStatusIndividuForward(apiToken)
		}
	}

	override suspend fun updateStatusIndividuBackward(apiToken: String): Resource<UpdateStatusIndividuBackwardRemoteResponse> {
		return proceed {
			dataSource.updateStatusIndividuBackward(apiToken)
		}
	}

	override suspend fun daftarSidangTA(
		apiToken: String,
		daftarSidangTARemoteRequestBody: DaftarSidangTARemoteRequestBody
	): Resource<DaftarSidangTARemoteResponse> {
		return proceed {
			dataSource.daftarSidangTA(apiToken, daftarSidangTARemoteRequestBody)
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