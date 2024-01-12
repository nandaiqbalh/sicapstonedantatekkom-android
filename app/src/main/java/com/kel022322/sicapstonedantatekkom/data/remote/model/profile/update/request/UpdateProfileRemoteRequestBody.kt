package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRemoteRequestBody(
	@SerializedName("user_id") val userId: String?,
	@SerializedName("api_token") val apiToken: String?,
	@SerializedName("user_name") val userName: String?,
	@SerializedName("no_telp") val noTelp: String?,
	@SerializedName("user_email") val userEmail: String?,
	@SerializedName("angkatan") val angkatan: String?,
	@SerializedName("ipk") val ipk: String?,
	@SerializedName("sks") val sks: String?,
	@SerializedName("jenis_kelamin") val jenisKelamin: String?,
	@SerializedName("alamat") val alamat: String?,
	@SerializedName("user_img") val userImage: String?,

	)