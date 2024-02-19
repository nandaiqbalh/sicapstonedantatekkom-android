package com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response

import com.google.gson.annotations.SerializedName

data class RsExpo(
    @SerializedName("created_by")
    val createdBy: String,

    @SerializedName("created_date")
    val createdDate: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_siklus")
    val idSiklus: Int,

    @SerializedName("modified_by")
    val modifiedBy: String,

    @SerializedName("modified_date")
    val modifiedDate: String,

    @SerializedName("tahun_ajaran")
    val tahunAjaran: String,

    @SerializedName("tanggal_mulai")
    val tanggalMulai: String,

    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String
)