package com.kel022322.sicapstonedantatekkom.data.remote.model.beranda.response

import com.google.gson.annotations.SerializedName

data class BerandaRemoteResponse(
    @SerializedName("data") val data: Data,
    @SerializedName("status") val status: String,
    @SerializedName("success") val success: Boolean
)

data class Data(
    @SerializedName("expo") val expo: String,
    @SerializedName("sidang_proposal") val sidangProposal: String,
    @SerializedName("sidang_ta") val sidangTA: String
)
