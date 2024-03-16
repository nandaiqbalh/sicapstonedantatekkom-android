package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request

import com.google.gson.annotations.SerializedName

data class AddKelompokIndividuRemoteRequestBody(
	@SerializedName("id_siklus") val idSiklus: String?,
	@SerializedName("email") val email: String?,
	@SerializedName("user_name") val userName: String?,
	@SerializedName("nomor_induk") val nomorInduk: String?,
	@SerializedName("angkatan") val angkatan: String?,
	@SerializedName("jenis_kelamin") val jenisKelamin: String?,
	@SerializedName("ipk") val ipk: String?,
	@SerializedName("sks") val sks: String?,
	@SerializedName("no_telp") val noTelp: String?,
	@SerializedName("judul_capstone") val judulCapstone: String?,
	@SerializedName("s") val s: String?,
	@SerializedName("e") val e: String?,
	@SerializedName("c") val c: String?,
	@SerializedName("m") val m: String?,
	@SerializedName("ews") val ews: String?,
	@SerializedName("bac") val bac: String?,
	@SerializedName("smb") val smb: String?,
	@SerializedName("smc") val smc: String?,


	)