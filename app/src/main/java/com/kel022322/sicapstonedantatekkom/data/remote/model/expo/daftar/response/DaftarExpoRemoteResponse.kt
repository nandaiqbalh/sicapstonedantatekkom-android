package com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.response

import com.google.gson.annotations.SerializedName

data class DaftarExpoRemoteResponse(
    @SerializedName("data") val data: Data?,
    @SerializedName("status") val status: String?,
    @SerializedName("success") val success: Boolean?,
)

data class Data(
    @SerializedName("status_expo")
    val statusExpo: String
)