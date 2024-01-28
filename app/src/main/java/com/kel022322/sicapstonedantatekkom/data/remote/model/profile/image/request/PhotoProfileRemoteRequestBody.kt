package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.request

import com.google.gson.annotations.SerializedName

data class PhotoProfileRemoteRequestBody(
	@SerializedName("user_id") val userId: String?,
	@SerializedName("api_token") val apiToken: String?
)