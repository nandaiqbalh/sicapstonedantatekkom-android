package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response

import com.google.gson.annotations.SerializedName

data class SidangTARemoteResponse(
    @SerializedName("data")
    val data: Data?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?
)