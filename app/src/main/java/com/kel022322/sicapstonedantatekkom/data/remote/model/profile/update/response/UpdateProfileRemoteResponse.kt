package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response

import com.google.gson.annotations.SerializedName
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.DataProfile

data class UpdateProfileRemoteResponse(
    @SerializedName("data")
    val data: DataProfile?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?
)