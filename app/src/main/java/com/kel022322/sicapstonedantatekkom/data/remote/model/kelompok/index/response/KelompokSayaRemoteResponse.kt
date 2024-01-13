package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class KelompokSayaRemoteResponse(
    @SerializedName("data")
    val data: DataKelompokSaya?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)