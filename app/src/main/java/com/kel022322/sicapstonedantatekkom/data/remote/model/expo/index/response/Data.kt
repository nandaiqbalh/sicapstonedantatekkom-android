package com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("cekStatusExpo")
    val cekStatusExpo: CekExpo,

    @SerializedName("id_kelompok")
    val idKelompok: Int,

    @SerializedName("kelengkapan")
    val kelengkapan: Kelengkapan,

    @SerializedName("kelompok")
    val kelompok: Kelompok,

    @SerializedName("rs_expo")
    val rsExpo: RsExpo
)