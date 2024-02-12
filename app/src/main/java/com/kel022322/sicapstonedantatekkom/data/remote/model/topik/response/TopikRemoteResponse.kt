package com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response

import com.google.gson.annotations.SerializedName

data class TopikRemoteResponse(
    @SerializedName("data") val data: Data?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("success") val success: Boolean?,
)