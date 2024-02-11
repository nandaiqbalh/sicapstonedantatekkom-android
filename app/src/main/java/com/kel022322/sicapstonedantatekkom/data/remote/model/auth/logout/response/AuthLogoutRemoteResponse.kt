package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response

import com.google.gson.annotations.SerializedName

data class AuthLogoutRemoteResponse(
    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("message")
    val message: String?,

)