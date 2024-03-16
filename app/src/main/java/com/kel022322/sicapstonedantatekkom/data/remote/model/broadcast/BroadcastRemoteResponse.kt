package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast

import com.google.gson.annotations.SerializedName


data class BroadcastRemoteResponse(
    @SerializedName("data")
    val data: DataBroadcast?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?
)

data class DataBroadcast(
    val rs_broadcast: RsBroadcast
)

data class DataX(
    @SerializedName("broadcast_image_name") val broadcastImageName: String?,
    @SerializedName("broadcast_image_path") val broadcastImagePath: String?,
    @SerializedName("broadcast_image_url") val broadcastImageUrl: String?,
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

data class Link(
    @SerializedName("active") val active: Boolean?,
    @SerializedName("label") val label: String?,
    @SerializedName("url") val url: String?
)

data class RsBroadcast(
    @SerializedName("current_page") val currentPage: Int?,
    @SerializedName("data") val data: List<DataX>?,
    @SerializedName("first_page_url") val firstPageUrl: String?,
    @SerializedName("from") val from: Int?,
    @SerializedName("last_page") val lastPage: Int?,
    @SerializedName("last_page_url") val lastPageUrl: String?,
    @SerializedName("links") val links: List<Link>?,
    @SerializedName("next_page_url") val nextPageUrl: Any?,
    @SerializedName("path") val path: String?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("prev_page_url") val prevPageUrl: Any?,
    @SerializedName("to") val to: Int?,
    @SerializedName("total") val total: Int?
)

