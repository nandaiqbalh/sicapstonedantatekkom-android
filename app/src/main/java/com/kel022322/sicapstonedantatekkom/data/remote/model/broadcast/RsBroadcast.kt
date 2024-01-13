package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast

import com.google.gson.annotations.SerializedName

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
