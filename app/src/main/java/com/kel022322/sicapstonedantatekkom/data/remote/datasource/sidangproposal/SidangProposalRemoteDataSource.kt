package com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangproposal

import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response.SidangProposalByKelompokResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface SidangProposalRemoteDataSource {
	suspend fun getSidangProposalByKelompok(
		apiToken: String,
	): SidangProposalByKelompokResponse
}

class SidangProposalRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	SidangProposalRemoteDataSource {

	override suspend fun getSidangProposalByKelompok(
		apiToken: String,
	): SidangProposalByKelompokResponse {
		return apiService.getSidangProposalByKelompok(apiToken)
	}

}