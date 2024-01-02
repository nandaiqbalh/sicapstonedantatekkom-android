package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response

import com.google.gson.annotations.SerializedName

data class AuthRemoteResponse(

    @SerializedName("data")
    val userData: Any? = null,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)