package com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile

import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface ProfileRemoteDataSource {

	suspend fun getMahasiswaProfile(
		profileRemoteRequestBody: ProfileRemoteRequestBody
	) : ProfileRemoteResponse
}

class ProfileRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService):ProfileRemoteDataSource{

	override suspend fun getMahasiswaProfile(profileRemoteRequestBody: ProfileRemoteRequestBody): ProfileRemoteResponse {
		return apiService.getMahasiswaProfile(profileRemoteRequestBody)
	}
}