package com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response

import com.google.gson.annotations.SerializedName

data class UploadC500ProcessRemoteResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)