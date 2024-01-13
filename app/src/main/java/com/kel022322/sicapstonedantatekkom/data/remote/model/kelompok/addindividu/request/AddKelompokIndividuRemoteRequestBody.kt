package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request

import com.google.gson.annotations.SerializedName

data class AddKelompokIndividuRemoteRequestBody(
	@SerializedName("user_id") val userId: String?,
	@SerializedName("api_token") val apiToken: String?,
	@SerializedName("id_siklus") val idSiklus: String?,
	@SerializedName("email") val email: String?,
	@SerializedName("angkatan") val angkatan: String?,
	@SerializedName("jenis_kelamin") val jenisKelamin: String?,
	@SerializedName("ipk") val ipk: String?,
	@SerializedName("sks") val sks: String?,
	@SerializedName("no_telp") val noTelp: String?,
	@SerializedName("alamat") val alamat: String?,
	@SerializedName("s") val s: String?,
	@SerializedName("e") val e: String?,
	@SerializedName("c") val c: String?,
	@SerializedName("m") val m: String?,


	)