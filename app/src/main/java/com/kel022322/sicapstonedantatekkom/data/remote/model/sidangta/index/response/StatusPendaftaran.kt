package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response

import com.google.gson.annotations.SerializedName

data class StatusPendaftaran(
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("id_mahasiswa") val idMahasiswa: String?,
    @SerializedName("modified_by") val modifiedBy: String?,
    @SerializedName("modified_date") val modifiedDate: String?,
    @SerializedName("status") val status: String
)
