package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request

import com.google.gson.annotations.SerializedName

data class AuthLogoutRequestBody(
	@SerializedName("user_id") val userId: String?,
	@SerializedName("api_token") val apiToken: String?
)