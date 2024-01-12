package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response

import com.google.gson.annotations.SerializedName

data class ProfileRemoteResponse(
	@SerializedName("data")
    val data: DataProfile,

	@SerializedName("message")
    val message: String,

	@SerializedName("status")
    val status: Boolean
)