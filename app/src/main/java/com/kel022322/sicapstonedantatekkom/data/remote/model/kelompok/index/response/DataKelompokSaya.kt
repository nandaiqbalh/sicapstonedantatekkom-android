package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class DataKelompokSaya(
	@SerializedName("getAkun") val getAkun: GetAkun?,
	@SerializedName("kelompok") val kelompok: Kelompok?,
	@SerializedName("proposal") val proposal: Any?, // Replace Any with the actual type if possible
	@SerializedName("rs_dosbing") val rsDosbing: List<RsDosbing>?,
	@SerializedName("rs_dospeng") val rsDospeng: List<Any>?, // Replace Any with the actual type if possible
	@SerializedName("rs_mahasiswa") val rsMahasiswa: List<RsMahasiswa>?,
	@SerializedName("rs_siklus") val rsSiklus: List<RsSiklus>?,
	@SerializedName("rs_topik") val rsTopik: List<RsTopik>?
)
