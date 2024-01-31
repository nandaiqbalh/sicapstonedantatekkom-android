package com.kel022322.sicapstonedantatekkom.data.local.action

import com.google.gson.annotations.SerializedName

data class ActionModel(

	@SerializedName("iconAction")
	val iconAction: Int,

	@SerializedName("titleAction")
	val titleAction: String?,

	@SerializedName("actionAction")
	val actionAction: Int?
)