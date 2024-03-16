package com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.request

import com.google.gson.annotations.SerializedName

data class DaftarExpoRemoteRequestBody (
	@SerializedName("id_expo") val idExpo: String?,
	@SerializedName("link_berkas_expo") val linkBerkasExpo: String?,
	@SerializedName("judul_ta_mhs") val judulTaMhs: String?,
)