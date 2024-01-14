package com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response

import com.google.gson.annotations.SerializedName

data class DataFileIndex(
   @SerializedName("file_mhs") val fileMhs: FileMhs?
)