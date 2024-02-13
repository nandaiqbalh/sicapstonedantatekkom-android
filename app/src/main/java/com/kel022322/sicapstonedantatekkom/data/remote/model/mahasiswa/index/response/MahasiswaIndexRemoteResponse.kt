package com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response

import com.google.gson.annotations.SerializedName

data class MahasiswaIndexRemoteResponse(
    @SerializedName("data") val data: DataMahasiswa?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("success") val success: Boolean?,
)