package com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.login

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.login.AuthLoginRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface AuthLoginRemoteRepository {
	suspend fun authLogin(
		authLoginRequestBody: AuthLoginRequestBody
	): Resource<AuthLoginRemoteResponse>
}

class AuthLoginRemoteRepositoryImpl @Inject constructor(private val dataSource: AuthLoginRemoteDataSource) :
	AuthLoginRemoteRepository {
	override suspend fun authLogin(
		authLoginRequestBody: AuthLoginRequestBody
	): Resource<AuthLoginRemoteResponse> {
		return proceed {
			dataSource.authLogin(authLoginRequestBody)
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