package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast

import com.google.gson.annotations.SerializedName


data class BroadcastRemoteResponse(
    @SerializedName("data")
    val data: DataBroadcast?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)