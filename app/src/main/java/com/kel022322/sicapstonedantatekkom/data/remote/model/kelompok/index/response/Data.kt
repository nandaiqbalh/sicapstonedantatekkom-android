package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("getAkun") val getAkun: GetAkun?,
    @SerializedName("kelompok") val kelompok: Kelompok?,
    @SerializedName("rs_dosbing") val rsDosbing: List<RsDosbing>?,
    @SerializedName("rs_dospeng") val rsDospeng: List<RsDospeng>?,
    @SerializedName("rs_mahasiswa") val rsMahasiswa: List<RsMahasiswa>?,
    @SerializedName("rs_siklus") val rsSiklus: List<RsSiklu>?,
    @SerializedName("rs_topik") val rsTopik: List<RsTopik>?,
)
