package com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.login

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface AuthLoginRemoteDataSource {
	suspend fun authLogin(authLoginRequestBody: AuthLoginRequestBody): AuthLoginRemoteResponse
}

class AuthLoginRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	AuthLoginRemoteDataSource {
	override suspend fun authLogin(
		authLoginRequestBody: AuthLoginRequestBody
	): AuthLoginRemoteResponse {
		return apiService.authLogin(authLoginRequestBody)
	}


}