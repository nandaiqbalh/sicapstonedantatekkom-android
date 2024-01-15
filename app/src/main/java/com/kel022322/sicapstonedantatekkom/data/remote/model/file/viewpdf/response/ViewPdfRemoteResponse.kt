package com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.response

import com.google.gson.annotations.SerializedName

data class ViewPdfRemoteResponse(
    @SerializedName("data")
    val data: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)