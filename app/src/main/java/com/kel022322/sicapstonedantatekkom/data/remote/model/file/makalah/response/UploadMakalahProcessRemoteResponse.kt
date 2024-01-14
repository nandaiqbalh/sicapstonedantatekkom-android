package com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response

import com.google.gson.annotations.SerializedName

data class UploadMakalahProcessRemoteResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)