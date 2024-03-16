package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BroadcastPaginateRemoteResponse(
    @SerializedName("data")
    val data: DataBroadcastPaginate?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?
)

data class DataBroadcastPaginate(
    val rs_broadcast: RsBroadcastPaginate
)

@Parcelize
data class DataXBroadcastPaginate(
    @SerializedName("broadcast_image_name") val broadcastImageName: String?,
    @SerializedName("broadcast_image_path") val broadcastImagePath: String?,
    @SerializedName("broadcast_image_url") val broadcastImageUrl: String?,
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("id_siklus") val idSiklus: String?,
    @SerializedName("keterangan") val keterangan: String?,
    @SerializedName("link_pendukung") val linkPendukung: String?,
    @SerializedName("modified_by") val modifiedBy: String?,
    @SerializedName("modified_date") val modifiedDate: String?,
    @SerializedName("nama_event") val namaEvent: String?,
    @SerializedName("tgl_mulai") val tglMulai: String?,
    @SerializedName("tgl_selesai") val tglSelesai: String?
) : Parcelable

data class LinkBroadcastPaginate(
    val active: Boolean,
    val label: String,
    val url: String
)

data class RsBroadcastPaginate(
    @SerializedName("current_page") val currentPage: Int?,
    @SerializedName("data") val data: List<DataXBroadcastPaginate>?,
    @SerializedName("first_page_url") val firstPageUrl: String?,
    @SerializedName("from") val from: Int?,
    @SerializedName("last_page") val lastPage: Int?,
    @SerializedName("last_page_url") val lastPageUrl: String?,
    @SerializedName("links") val links: List<LinkBroadcastPaginate>?,
    @SerializedName("next_page_url") val nextPageUrl: Any?,
    @SerializedName("path") val path: String?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("prev_page_url") val prevPageUrl: Any?,
    @SerializedName("to") val to: Int?,
    @SerializedName("total") val total: Int?
)