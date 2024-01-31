package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate

import com.google.gson.annotations.SerializedName

data class DataXBroadcastPaginate(
    @SerializedName("broadcast_image_name") val broadcastImageName: String?,
    @SerializedName("broadcast_image_path") val broadcastImagePath: String?,
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("id_siklus") val idSiklus: Any?,
    @SerializedName("keterangan") val keterangan: String?,
    @SerializedName("link_pendukung") val linkPendukung: String?,
    @SerializedName("modified_by") val modifiedBy: Any?,
    @SerializedName("modified_date") val modifiedDate: Any?,
    @SerializedName("nama_event") val namaEvent: String?,
    @SerializedName("tgl_mulai") val tglMulai: String?,
    @SerializedName("tgl_selesai") val tglSelesai: String?
)