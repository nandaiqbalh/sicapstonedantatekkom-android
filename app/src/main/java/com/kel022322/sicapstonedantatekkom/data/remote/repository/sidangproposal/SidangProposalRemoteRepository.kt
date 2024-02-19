package com.kel022322.sicapstonedantatekkom.data.remote.repository.sidangproposal

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangproposal.SidangProposalRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response.SidangProposalByKelompokResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface SidangProposalRemoteRepository {

	suspend fun getSidangProposalByKelompok(
		apiToken: String,
	): Resource<SidangProposalByKelompokResponse>

}

class SidangProposalRemoteRepositoryImpl @Inject constructor(private val dataSource: SidangProposalRemoteDataSource) :
	SidangProposalRemoteRepository {

	override suspend fun getSidangProposalByKelompok(
		apiToken: String,
	): Resource<SidangProposalByKelompokResponse> {

		return proceed {
			dataSource.getSidangProposalByKelompok(apiToken)
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