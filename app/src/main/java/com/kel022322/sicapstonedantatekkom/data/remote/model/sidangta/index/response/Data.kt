package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("kelompok") val kelompok: Kelompok?,
    @SerializedName("periode") val periode: Periode?,
    @SerializedName("rsSidang") val rsSidang: RsSidang?,
    @SerializedName("status_pendaftaran") val statusPendaftaran: StatusPendaftaran?
)
