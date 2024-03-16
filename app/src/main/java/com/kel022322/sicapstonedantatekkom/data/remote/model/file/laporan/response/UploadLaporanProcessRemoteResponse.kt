package com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response

import com.google.gson.annotations.SerializedName

data class UploadLaporanProcessRemoteResponse(
    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: String?
)