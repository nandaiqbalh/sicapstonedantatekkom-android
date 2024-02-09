package com.kel022322.sicapstonedantatekkom.data.local.model.jeniskelamin

import com.google.gson.annotations.SerializedName

data class JenisKelaminModel(
	@SerializedName("id") val id: Int?,
	@SerializedName("jenis_kelamin") val jenisJelamin: String?,

)
