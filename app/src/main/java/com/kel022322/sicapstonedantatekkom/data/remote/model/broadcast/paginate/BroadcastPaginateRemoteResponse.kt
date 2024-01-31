package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate

import com.google.gson.annotations.SerializedName

data class BroadcastPaginateRemoteResponse(
    @SerializedName("data")
    val data: DataBroadcastPaginate?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)