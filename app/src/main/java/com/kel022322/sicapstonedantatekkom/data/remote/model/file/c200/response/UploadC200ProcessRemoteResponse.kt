package com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response

import com.google.gson.annotations.SerializedName

data class UploadC200ProcessRemoteResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)