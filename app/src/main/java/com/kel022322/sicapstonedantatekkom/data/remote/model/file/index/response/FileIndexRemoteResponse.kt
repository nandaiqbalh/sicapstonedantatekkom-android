package com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response

import com.google.gson.annotations.SerializedName

data class FileIndexRemoteResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: Data?
)