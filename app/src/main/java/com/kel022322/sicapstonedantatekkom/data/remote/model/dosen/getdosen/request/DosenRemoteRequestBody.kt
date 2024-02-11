package com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.request

import com.google.gson.annotations.SerializedName

data class DosenRemoteRequestBody(
	@SerializedName("api_token") val apiToken: String?
)