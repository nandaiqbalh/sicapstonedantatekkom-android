package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.response

import com.google.gson.annotations.SerializedName

data class PhotoProfileRemoteResponse(
    @SerializedName("data")
    val data: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)