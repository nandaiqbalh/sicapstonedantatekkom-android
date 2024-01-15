package com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.request

import com.google.gson.annotations.SerializedName

data class ViewPdfRemoteRequestBody(
	@SerializedName("user_id") val userId: String?,
	@SerializedName("api_token") val apiToken: String?,
	@SerializedName("filepath") val filePath: String?,
	@SerializedName("filename") val fileName: String?,
)