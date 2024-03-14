package com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response

import com.google.gson.annotations.SerializedName

data class UploadC400ProcessRemoteResponse(
    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: String?
)