package com.kel022322.sicapstonedantatekkom.data.remote.service

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request.AuthLogoutRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.BroadcastRemoteResponse
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

}