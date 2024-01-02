package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast

data class DataX(
    val broadcast_image_name: String,
    val broadcast_image_path: String,
    val created_by: String,
    val created_date: String,
    val id: Int,
    val id_siklus: Any,
    val keterangan: String,
    val link_pendukung: String,
    val modified_by: Any,
    val modified_date: Any,
    val nama_event: String,
    val tgl_mulai: String,
    val tgl_selesai: String
)