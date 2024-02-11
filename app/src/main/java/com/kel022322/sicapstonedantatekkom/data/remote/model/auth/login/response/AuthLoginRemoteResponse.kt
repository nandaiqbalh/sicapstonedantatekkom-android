package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response

import com.google.gson.annotations.SerializedName

data class AuthLoginRemoteResponse(
    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val userData: UserData? = null,

)