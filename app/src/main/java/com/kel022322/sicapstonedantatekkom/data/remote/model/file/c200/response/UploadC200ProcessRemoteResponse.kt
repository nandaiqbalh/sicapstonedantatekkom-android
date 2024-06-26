package com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response

import com.google.gson.annotations.SerializedName

data class UploadC200ProcessRemoteResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: String?
)