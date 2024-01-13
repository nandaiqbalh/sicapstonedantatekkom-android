package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response

import com.google.gson.annotations.SerializedName

data class AddKelompokIndividuRemoteResponse(
	@SerializedName("message") val message: String?,
	@SerializedName("status") val status: Boolean?,
)