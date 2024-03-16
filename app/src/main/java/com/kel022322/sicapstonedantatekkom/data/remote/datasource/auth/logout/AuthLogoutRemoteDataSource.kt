package com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.logout

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface AuthLogoutRemoteDataSource {
	suspend fun authLogout(
		apiToken: String,
	): AuthLogoutRemoteResponse
}

class AuthLogoutRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	AuthLogoutRemoteDataSource {
	override suspend fun authLogout(
		apiToken: String,
	): AuthLogoutRemoteResponse {
		return apiService.authLogout(apiToken)
	}

}