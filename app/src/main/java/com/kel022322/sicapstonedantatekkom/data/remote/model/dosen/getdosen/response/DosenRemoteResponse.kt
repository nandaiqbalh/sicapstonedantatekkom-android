package com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response

import com.google.gson.annotations.SerializedName

data class DosenRemoteResponse(
    @SerializedName("data") val data: DataDosen?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: Boolean?,
)