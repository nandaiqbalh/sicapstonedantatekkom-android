package com.kel022322.sicapstonedantatekkom.data.remote.repository.auth

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.AuthRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.request.AuthRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.response.AuthRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface AuthRemoteRepository {
	suspend fun authLogin(
		authRequestBody: AuthRequestBody
	): Resource<AuthRemoteResponse>
}

class AuthRemoteRepositoryImpl @Inject constructor(private val dataSource: AuthRemoteDataSource) :
	AuthRemoteRepository {
	override suspend fun authLogin(
		authRequestBody: AuthRequestBody
	): Resource<AuthRemoteResponse> {
		return proceed {
			dataSource.authLogin(authRequestBody)
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