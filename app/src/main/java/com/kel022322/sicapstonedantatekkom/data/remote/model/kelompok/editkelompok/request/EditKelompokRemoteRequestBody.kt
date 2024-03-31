package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.editkelompok.request

import com.google.gson.annotations.SerializedName

data class EditKelompokRemoteRequestBody(
	@SerializedName("judul_capstone") val judulCapstone: String?,
	@SerializedName("id_topik") val idTopik: String?,

	)