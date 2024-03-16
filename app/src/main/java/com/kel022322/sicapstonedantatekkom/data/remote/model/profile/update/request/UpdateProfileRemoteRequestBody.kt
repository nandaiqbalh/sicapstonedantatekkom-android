package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRemoteRequestBody(
	@SerializedName("user_name") val userName: String?,
	@SerializedName("no_telp") val noTelp: String?,
	@SerializedName("user_email") val userEmail: String?,
	)