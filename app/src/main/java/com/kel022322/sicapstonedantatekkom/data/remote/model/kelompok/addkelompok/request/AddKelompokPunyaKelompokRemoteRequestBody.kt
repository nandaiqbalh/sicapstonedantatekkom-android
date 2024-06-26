package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request

import com.google.gson.annotations.SerializedName

data class AddKelompokPunyaKelompokRemoteRequestBody(
	@SerializedName("id_siklus") val idSiklus: String?,
	@SerializedName("judul_capstone") val judulCapstone: String?,
	@SerializedName("id_topik") val idTopik: String?,
	@SerializedName("dosbing_1") val dosbingSatu: String?,
	@SerializedName("dosbing_2") val dosbingDua: String?,

	@SerializedName("angkatan1") val angkatanSatu: String?,
	@SerializedName("email1") val emailSatu: String?,
	@SerializedName("jenis_kelamin1") val jenisKelaminSatu: String?,
	@SerializedName("ipk1") val ipkSatu: String?,
	@SerializedName("sks1") val sksSatu: String?,
	@SerializedName("no_telp1") val noTelpSatu: String?,

	@SerializedName("user_id2") val userIdDua: String?,
	@SerializedName("angkatan2") val angkatanDua: String?,
	@SerializedName("email2") val emailDua: String?,
	@SerializedName("jenis_kelamin2") val jenisKelaminDua: String?,
	@SerializedName("ipk2") val ipkDua: String?,
	@SerializedName("sks2") val sksDua: String?,
	@SerializedName("no_telp2") val noTelpDua: String?,

	@SerializedName("user_id3") val userIdTiga: String?,
	@SerializedName("angkatan3") val angkatanTiga: String?,
	@SerializedName("email3") val emailTiga: String?,
	@SerializedName("jenis_kelamin3") val jenisKelaminTiga: String?,
	@SerializedName("ipk3") val ipkTiga: String?,
	@SerializedName("sks3") val sksTiga: String?,
	@SerializedName("no_telp3") val noTelpTiga: String?,


	)