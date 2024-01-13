package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response

import com.google.gson.annotations.SerializedName

data class AuthLogoutRemoteResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: Boolean?,
)