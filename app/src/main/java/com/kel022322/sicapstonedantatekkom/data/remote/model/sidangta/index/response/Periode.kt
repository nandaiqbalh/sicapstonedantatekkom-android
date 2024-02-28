package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response

import com.google.gson.annotations.SerializedName

data class  Periode(
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("modified_by") val modifiedBy: String?,
    @SerializedName("modified_date") val modifiedDate: String?,
    @SerializedName("nama_periode") val namaPeriode: String?,
    @SerializedName("tanggal_mulai") val tanggalMulai: String?,
    @SerializedName("tanggal_selesai") val tanggalSelesai: String?,
    @SerializedName("hari_batas") val hariBatas: String?,
    @SerializedName("tanggal_batas") val tanggalBatas: String?,
    @SerializedName("waktu_batas") val waktuBatas: String?,

    )
