package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.tolakkelompok

import com.google.gson.annotations.SerializedName

data class TolakKelompokRemoteResponse(
	@SerializedName("data")
	val data: String?,

	@SerializedName("status")
	val status: String?,

	@SerializedName("success")
	val success: Boolean?,
)
