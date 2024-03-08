package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.status

import com.google.gson.annotations.SerializedName

data class UpdateStatusKelompokForwardRemoteResponse (
	@SerializedName("data")
	val data: Data?,

	@SerializedName("status")
	val status: String?,

	@SerializedName("success")
	val success: Boolean?,
)