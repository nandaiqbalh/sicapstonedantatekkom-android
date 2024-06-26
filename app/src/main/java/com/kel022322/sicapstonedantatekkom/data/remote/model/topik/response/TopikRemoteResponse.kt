package com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response

import com.google.gson.annotations.SerializedName

data class TopikRemoteResponse(
	@SerializedName("data") val data: Data?,
	@SerializedName("status") val status: String?,
	@SerializedName("success") val success: Boolean?,
)

data class Data(
    val rs_topik: List<RsTopik>?,
)

data class RsTopik(
	@SerializedName("created_by") val createdBy: String?,
	@SerializedName("created_date") val createdDate: String?,
	@SerializedName("id") val id: Int?,
	@SerializedName("modified_by") val modifiedBy: String?,
	@SerializedName("modified_date") val modifiedDate: String?,
	@SerializedName("nama") val nama: String?,
)