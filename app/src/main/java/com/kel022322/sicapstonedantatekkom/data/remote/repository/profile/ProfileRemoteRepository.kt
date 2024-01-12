package com.kel022322.sicapstonedantatekkom.data.remote.repository.profile

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile.ProfileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface ProfileRemoteRepository {

	suspend fun getMahasiswaProfile(
		profileRemoteRequestBody: ProfileRemoteRequestBody
	) : Resource<ProfileRemoteResponse>
}

class ProfileRemoteRepositoryImpl @Inject constructor(private val dataSource: ProfileRemoteDataSource) : ProfileRemoteRepository{

	override suspend fun getMahasiswaProfile(profileRemoteRequestBody: ProfileRemoteRequestBody): Resource<ProfileRemoteResponse> {

		return proceed {
			dataSource.getMahasiswaProfile(profileRemoteRequestBody)
		}
	}

	private suspend fun <T> proceed (coroutines: suspend () -> T): Resource <T> {
		return  try{
			Resource.Success(coroutines.invoke())
		} catch (e: Exception){
			Resource.Error(e)
		}
	}

}