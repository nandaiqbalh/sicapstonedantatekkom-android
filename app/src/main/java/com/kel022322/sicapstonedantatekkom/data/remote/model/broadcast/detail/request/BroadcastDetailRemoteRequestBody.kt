package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request

import com.google.gson.annotations.SerializedName

data class BroadcastDetailRemoteRequestBody(
	@SerializedName("id") val broadcastId: String?,
)