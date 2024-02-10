package com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.request

import com.google.gson.annotations.SerializedName

data class DosenRemoteRequestBody(
	@SerializedName("user_id") val userId: String?,
	@SerializedName("api_token") val apiToken: String?
)