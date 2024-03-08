package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status

import com.google.gson.annotations.SerializedName

data class UpdateStatusIndividuBackwardRemoteResponse(
	@SerializedName("data")
	val data: Data?,

	@SerializedName("status")
	val status: String?,

	@SerializedName("success")
	val success: Boolean?
)
