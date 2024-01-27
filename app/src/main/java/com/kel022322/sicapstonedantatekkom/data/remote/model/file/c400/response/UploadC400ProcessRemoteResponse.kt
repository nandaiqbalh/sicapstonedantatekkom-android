package com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response

import com.google.gson.annotations.SerializedName

data class UploadC400ProcessRemoteResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)