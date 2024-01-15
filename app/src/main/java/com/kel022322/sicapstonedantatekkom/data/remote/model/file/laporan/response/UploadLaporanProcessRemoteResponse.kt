package com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response

import com.google.gson.annotations.SerializedName

data class UploadLaporanProcessRemoteResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)