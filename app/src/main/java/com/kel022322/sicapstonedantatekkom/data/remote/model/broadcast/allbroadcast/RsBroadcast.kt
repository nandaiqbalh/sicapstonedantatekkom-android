package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.allbroadcast

import com.google.gson.annotations.SerializedName

data class RsBroadcast(
    @SerializedName("broadcast_image_name")
    val broadcastImageName: String,
    @SerializedName("broadcast_image_path")
    val broadcastImagePath: String,
    @SerializedName("broadcast_image_url")
    val broadcastImageUrl: String,
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("created_date")
    val createdDate: String,
    val id: Int,
    @SerializedName("id_siklus")
    val idSiklus: Any,
    val keterangan: String,
    @SerializedName("link_pendukung")
    val linkPendukung: String,
    @SerializedName("modified_by")
    val modifiedBy: Any,
    @SerializedName("modified_date")
    val modifiedDate: Any,
    @SerializedName("nama_event")
    val namaEvent: String,
    val postDate: String,
    @SerializedName("tgl_mulai")
    val tglMulai: String,
    @SerializedName("tgl_selesai")
    val tglSelesai: String
)
