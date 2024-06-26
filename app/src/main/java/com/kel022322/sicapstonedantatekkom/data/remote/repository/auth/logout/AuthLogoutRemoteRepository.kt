package com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.logout

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.logout.AuthLogoutRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface AuthLogoutRemoteRepository {

	suspend fun authLogout(
		apiToken: String,
	): Resource<AuthLogoutRemoteResponse>
}

class AuthLogoutRemoteRepositoryImpl @Inject constructor(private val dataSource: AuthLogoutRemoteDataSource) :
	AuthLogoutRemoteRepository {
	override suspend fun authLogout(
		apiToken: String,
	): Resource<AuthLogoutRemoteResponse> {

		return proceed {
			dataSource.authLogout(apiToken)
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