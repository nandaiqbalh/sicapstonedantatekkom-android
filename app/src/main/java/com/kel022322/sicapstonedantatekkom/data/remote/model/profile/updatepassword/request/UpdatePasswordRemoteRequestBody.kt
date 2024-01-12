package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRemoteRequestBody(
	@SerializedName("user_id") val userId: String?,
	@SerializedName("api_token") val apiToken: String?,
	@SerializedName("current_password") val currentPassword: String?,
	@SerializedName("new_password") val newPassword: String?,
	@SerializedName("repeat_new_password") val repeatNewPassword: String?,


	)