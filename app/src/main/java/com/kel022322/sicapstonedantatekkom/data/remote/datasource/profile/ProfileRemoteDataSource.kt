package com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile

import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatephoto.response.UpdateProfilePhotoRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

interface ProfileRemoteDataSource {

	suspend fun getMahasiswaProfile(
		apiToken: String,
	): ProfileRemoteResponse

	suspend fun updateMahasiswaProfile(
		apiToken: String,
		updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody,
	): UpdateProfileRemoteResponse

	suspend fun updatePasswordProfile(
		apiToken: String,
		updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody,
	): UpdatePasswordRemoteResponse

	suspend fun updatePhotoProfile(
		apiToken: String,
		user_img: MultipartBody.Part,
	): UpdateProfilePhotoRemoteResponse
}

class ProfileRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
	ProfileRemoteDataSource {

	override suspend fun getMahasiswaProfile(
		apiToken: String,
	): ProfileRemoteResponse {
		return apiService.getMahasiswaProfile(apiToken)
	}

	override suspend fun updateMahasiswaProfile(
		apiToken: String,
		updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody,
	): UpdateProfileRemoteResponse {
		return apiService.updateMahasiswaProfile(apiToken, updateProfileRemoteRequestBody)
	}

	override suspend fun updatePasswordProfile(
		apiToken: String,
		updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody,
	): UpdatePasswordRemoteResponse {
		return apiService.updatePasswordProfile(apiToken, updatePasswordRemoteRequestBody)
	}

	override suspend fun updatePhotoProfile(
		apiToken: String,
		user_img: MultipartBody.Part,
	): UpdateProfilePhotoRemoteResponse {
		return apiService.updatePhotoProfile(apiToken, user_img)
	}
}