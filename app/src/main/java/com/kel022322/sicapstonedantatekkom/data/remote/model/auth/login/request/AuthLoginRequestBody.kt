package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request

import com.google.gson.annotations.SerializedName

data class AuthLoginRequestBody(
	@SerializedName("nomor_induk") val nomorInduk: String?,
	@SerializedName("password") val password: String?
)