package com.kel022322.sicapstonedantatekkom.data.remote.repository.profile

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile.ProfileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.request.PhotoProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.response.PhotoProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import javax.inject.Inject

interface ProfileRemoteRepository {

	suspend fun getMahasiswaProfile(
		profileRemoteRequestBody: ProfileRemoteRequestBody
	) : Resource<ProfileRemoteResponse>

	suspend fun updateMahasiswaProfile(
		updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody
	) : Resource<UpdateProfileRemoteResponse>

	suspend fun updatePasswordProfile(
		updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody
	) : Resource<UpdatePasswordRemoteResponse>

	suspend fun getPhotoProfile(
		photoProfileRemoteRequestBody: PhotoProfileRemoteRequestBody
	) : Resource<PhotoProfileRemoteResponse>
}

class ProfileRemoteRepositoryImpl @Inject constructor(private val dataSource: ProfileRemoteDataSource) : ProfileRemoteRepository{

	override suspend fun getMahasiswaProfile(profileRemoteRequestBody: ProfileRemoteRequestBody): Resource<ProfileRemoteResponse> {

		return proceed {
			dataSource.getMahasiswaProfile(profileRemoteRequestBody)
		}
	}

	override suspend fun updateMahasiswaProfile(updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody): Resource<UpdateProfileRemoteResponse> {
		return proceed {
			dataSource.updateMahasiswaProfile(updateProfileRemoteRequestBody)
		}
	}

	override suspend fun updatePasswordProfile(updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody): Resource<UpdatePasswordRemoteResponse> {
		return proceed {
			dataSource.updatePasswordProfile(updatePasswordRemoteRequestBody)
		}
	}

	override suspend fun getPhotoProfile(photoProfileRemoteRequestBody: PhotoProfileRemoteRequestBody): Resource<PhotoProfileRemoteResponse> {
		return proceed {
			dataSource.getPhotoProfile(photoProfileRemoteRequestBody)
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