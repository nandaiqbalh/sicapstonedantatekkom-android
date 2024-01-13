package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class RsSiklus(
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("modified_by") val modifiedBy: Any?,
    @SerializedName("modified_date") val modifiedDate: Any?,
    @SerializedName("status") val status: String?,
    @SerializedName("tahun_ajaran") val tahunAjaran: String?,
    @SerializedName("tanggal_mulai") val tanggalMulai: String?,
    @SerializedName("tanggal_selesai") val tanggalSelesai: String?
)
