package com.kel022322.sicapstonedantatekkom.data.remote.repository.profile

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile.ProfileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatephoto.response.UpdateProfilePhotoRemoteResponse
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

interface ProfileRemoteRepository {

	suspend fun getMahasiswaProfile(
		apiToken: String,
	): Resource<ProfileRemoteResponse>

	suspend fun updateMahasiswaProfile(
		apiToken: String,
		updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody,
	): Resource<UpdateProfileRemoteResponse>

	suspend fun updatePasswordProfile(
		apiToken: String,
		updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody,
	): Resource<UpdatePasswordRemoteResponse>


	suspend fun updatePhotoProfile(
		apiToken: String,
		user_img: MultipartBody.Part,
	): Resource<UpdateProfilePhotoRemoteResponse>
}

class ProfileRemoteRepositoryImpl @Inject constructor(private val dataSource: ProfileRemoteDataSource) :
	ProfileRemoteRepository {

	override suspend fun getMahasiswaProfile(
		apiToken: String,
	): Resource<ProfileRemoteResponse> {

		return proceed {
			dataSource.getMahasiswaProfile(apiToken)
		}
	}

	override suspend fun updateMahasiswaProfile(
		apiToken: String,
		updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody,
	): Resource<UpdateProfileRemoteResponse> {
		return proceed {
			dataSource.updateMahasiswaProfile(apiToken, updateProfileRemoteRequestBody)
		}
	}

	override suspend fun updatePasswordProfile(
		apiToken: String,
		updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody,
	): Resource<UpdatePasswordRemoteResponse> {
		return proceed {
			dataSource.updatePasswordProfile(apiToken, updatePasswordRemoteRequestBody)
		}
	}

	override suspend fun updatePhotoProfile(
		apiToken: String,
		user_img: MultipartBody.Part,
	): Resource<UpdateProfilePhotoRemoteResponse> {
		return proceed {
			dataSource.updatePhotoProfile(apiToken, user_img)
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