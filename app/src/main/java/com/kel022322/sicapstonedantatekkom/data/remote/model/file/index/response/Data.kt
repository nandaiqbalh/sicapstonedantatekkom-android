package com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("file_mhs")
    val fileMhs: FileMhs?
)