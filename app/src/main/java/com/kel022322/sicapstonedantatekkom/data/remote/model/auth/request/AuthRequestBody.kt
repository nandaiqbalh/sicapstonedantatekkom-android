package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.request

import com.google.gson.annotations.SerializedName

data class AuthRequestBody(
	@SerializedName("nomor_induk") val nomorInduk: String?,
	@SerializedName("password") val password: String?
)