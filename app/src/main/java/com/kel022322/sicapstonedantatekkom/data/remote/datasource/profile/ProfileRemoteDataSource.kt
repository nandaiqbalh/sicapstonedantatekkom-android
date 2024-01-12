package com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile

import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface ProfileRemoteDataSource {

	suspend fun getMahasiswaProfile(
		profileRemoteRequestBody: ProfileRemoteRequestBody
	) : ProfileRemoteResponse

	suspend fun updateMahasiswaProfile(
		updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody
	) : UpdateProfileRemoteResponse
}

class ProfileRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService):ProfileRemoteDataSource{

	override suspend fun getMahasiswaProfile(profileRemoteRequestBody: ProfileRemoteRequestBody): ProfileRemoteResponse {
		return apiService.getMahasiswaProfile(profileRemoteRequestBody)
	}

	override suspend fun updateMahasiswaProfile(updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody): UpdateProfileRemoteResponse {
		return apiService.updateMahasiswaProfile(updateProfileRemoteRequestBody)
	}
}