package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail

import com.google.gson.annotations.SerializedName

data class BroadcastDetailRemoteResponse(
    @SerializedName("data")
    val data: DataDetailBroadcast,

    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Boolean
)