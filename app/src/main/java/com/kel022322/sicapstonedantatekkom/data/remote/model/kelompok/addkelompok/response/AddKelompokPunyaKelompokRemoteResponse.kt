package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response

import com.google.gson.annotations.SerializedName

data class AddKelompokPunyaKelompokRemoteResponse(
	@SerializedName("message") val message: String?,
	@SerializedName("status") val status: Boolean?,
)