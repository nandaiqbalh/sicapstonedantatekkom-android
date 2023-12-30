package com.kel022322.sicapstonedantatekkom.data.remote.service

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.request.AuthRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.response.AuthRemoteResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

	@POST("auth/login")
	suspend fun authLogin(
		@Body authRequestBody: AuthRequestBody
	): AuthRemoteResponse

}