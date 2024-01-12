package com.kel022322.sicapstonedantatekkom.data.remote.service

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request.AuthLogoutRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.BroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

	@POST("auth/login")
	suspend fun authLogin(
		@Body authLoginRequestBody: AuthLoginRequestBody
	): AuthLoginRemoteResponse

	@POST("auth/logout")
	suspend fun authLogout(
		@Body authLogoutRequestBody: AuthLogoutRequestBody
	): AuthLogoutRemoteResponse

	@POST("mahasiswa/broadcast")
	suspend fun getBroadcast(
	): BroadcastRemoteResponse

	@POST("mahasiswa/profile")
	suspend fun getMahasiswaProfile(
		@Body profileRemoteRequestBody: ProfileRemoteRequestBody
	): ProfileRemoteResponse

	@POST("mahasiswa/profile/editProcess")
	suspend fun updateMahasiswaProfile(
		@Body updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody
	) : UpdateProfileRemoteResponse

	@POST("mahasiswa/profile/editPassword")
	suspend fun updatePasswordProfile(
		@Body updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody
	) : UpdatePasswordRemoteResponse

}