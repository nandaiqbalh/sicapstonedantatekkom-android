package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRemoteResponse(
    @SerializedName("data")
    val data: DataProfilePassword,

    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Boolean
)