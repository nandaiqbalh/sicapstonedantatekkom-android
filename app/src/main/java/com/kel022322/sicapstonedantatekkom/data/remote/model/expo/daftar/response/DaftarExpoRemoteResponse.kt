package com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.response

import com.google.gson.annotations.SerializedName

data class DaftarExpoRemoteResponse(
    @SerializedName("data") val data: Data?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("success") val success: Boolean?,
)