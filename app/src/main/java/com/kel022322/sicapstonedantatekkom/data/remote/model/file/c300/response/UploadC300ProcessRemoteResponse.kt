package com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response

import com.google.gson.annotations.SerializedName

data class UploadC300ProcessRemoteResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: String?
)