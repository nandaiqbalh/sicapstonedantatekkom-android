package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response

import com.google.gson.annotations.SerializedName

data class AuthLoginRemoteResponse(

    @SerializedName("data")
    val userData: UserData? = null,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)