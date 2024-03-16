package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.response

import com.google.gson.annotations.SerializedName

data class DaftarSidangTARemoteResponse(
    @SerializedName("data")
    val data: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?
)