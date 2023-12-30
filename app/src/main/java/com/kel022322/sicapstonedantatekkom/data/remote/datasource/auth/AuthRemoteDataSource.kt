package com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.request.AuthRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.response.AuthRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import javax.inject.Inject

interface AuthRemoteDataSource {
	suspend fun authLogin(authRequestBody: AuthRequestBody): AuthRemoteResponse
}

class AuthRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	AuthRemoteDataSource {
	override suspend fun authLogin(
		authRequestBody: AuthRequestBody
	): AuthRemoteResponse {
		return apiService.authLogin(authRequestBody)
	}


}