package com.kel022322.sicapstonedantatekkom.data.remote.service

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.BroadcastRemoteResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

	@POST("auth/login")
	suspend fun authLogin(
		@Body authRequestBody: AuthRequestBody
	): AuthRemoteResponse

	@POST("mahasiswa/broadcast")
	suspend fun getBroadcast(
	): BroadcastRemoteResponse

}