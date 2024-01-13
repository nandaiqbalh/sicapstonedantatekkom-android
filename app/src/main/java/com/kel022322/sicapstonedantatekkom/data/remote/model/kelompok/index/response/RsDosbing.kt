package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class RsDosbing(
    @SerializedName("id") val id: Int?,
    @SerializedName("id_dosen") val idDosen: String?,
    @SerializedName("id_kelompok") val idKelompok: Int?,
    @SerializedName("nomor_induk") val nomorInduk: String?,
    @SerializedName("status_dosen") val statusDosen: String?,
    @SerializedName("status_persetujuan") val statusPersetujuan: String?,
    @SerializedName("user_name") val userName: String?
)
