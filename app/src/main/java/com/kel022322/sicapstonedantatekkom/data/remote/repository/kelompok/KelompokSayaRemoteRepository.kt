package com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok.KelompokSayaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.editkelompok.request.EditKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.editkelompok.response.EditKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.terimakelompok.TerimaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.tolakkelompok.TolakKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface KelompokSayaRemoteRepository {

	suspend fun getKelompokSaya(
		apiToken: String,
	): Resource<KelompokSayaRemoteResponse>

	suspend fun terimaKelompok(
		apiToken: String,
	): Resource<TerimaKelompokRemoteResponse>

	suspend fun tolakKelompok(
		apiToken: String,
	): Resource<TolakKelompokRemoteResponse>

	suspend fun addKelompokIndividu(
		apiToken: String,
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody,
	): Resource<AddKelompokIndividuRemoteResponse>

	suspend fun addKelompokPunyaKelompok(
		apiToken: String,
		addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody,
	): Resource<AddKelompokPunyaKelompokRemoteResponse>

	suspend fun editInformasiKelompok(
		apiToken: String,
		editKelompokRemoteRequestBody: EditKelompokRemoteRequestBody
	): Resource<EditKelompokRemoteResponse>
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

	override suspend fun terimaKelompok(apiToken: String): Resource<TerimaKelompokRemoteResponse> {
		return proceed {
			dataSource.terimaKelompok(apiToken)
		}
	}

	override suspend fun tolakKelompok(apiToken: String): Resource<TolakKelompokRemoteResponse> {
		return proceed {
			dataSource.tolakKelompok(apiToken)
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

	override suspend fun editInformasiKelompok(
		apiToken: String,
		editKelompokRemoteRequestBody: EditKelompokRemoteRequestBody,
	): Resource<EditKelompokRemoteResponse> {
		return proceed {
			dataSource.editInformasiKelompok(apiToken, editKelompokRemoteRequestBody)
		}	}

	private suspend fun <T> proceed(coroutines: suspend () -> T): Resource<T> {
		return try {
			Resource.Success(coroutines.invoke())
		} catch (e: Exception) {
			Resource.Error(e)
		}
	}
}