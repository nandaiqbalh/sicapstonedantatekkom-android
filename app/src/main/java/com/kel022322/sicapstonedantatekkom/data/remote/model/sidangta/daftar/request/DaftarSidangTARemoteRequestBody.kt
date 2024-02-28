package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.request

import com.google.gson.annotations.SerializedName

data class DaftarSidangTARemoteRequestBody (
	@SerializedName("link_upload") val linkUpload: String?,
	@SerializedName("judul_ta_mhs") val judulTaMhs: String?,
)