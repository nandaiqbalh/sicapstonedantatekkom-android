package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class RsMahasiswa(
	@SerializedName("created_by") val createdBy: String?,
	@SerializedName("created_date") val createdDate: String?,
	@SerializedName("file_name_laporan_ta") val fileNameLaporanTA: String?,
	@SerializedName("file_name_makalah") val fileNameMakalah: String?,
	@SerializedName("file_path_laporan_ta") val filePathLaporanTA: String?,
	@SerializedName("file_path_makalah") val filePathMakalah: String?,
	@SerializedName("id") val id: Int?,
	@SerializedName("id_kelompok") val idKelompok: Int?,
	@SerializedName("id_mahasiswa") val idMahasiswa: String?,
	@SerializedName("id_siklus") val idSiklus: Int?,
	@SerializedName("id_topik_mhs") val idTopikMhs: Int?,
	@SerializedName("judul_ta_mhs") val judulTAMhs: String?,
	@SerializedName("link_upload") val linkUpload: String?,
	@SerializedName("modified_by") val modifiedBy: String?,
	@SerializedName("modified_date") val modifiedDate: String?,
	@SerializedName("nomor_induk") val nomorInduk: String?,
	@SerializedName("progress_kelompok") val progressKelompok: String?,
	@SerializedName("status_individu") val statusIndividu: String?,
	@SerializedName("user_name") val userName: String?,
	@SerializedName("user_img_path") val userImgPath: String?,
	@SerializedName("user_img_name") val userImgName: String?,
	@SerializedName("user_img_url") val userImgUrl: String?,
	)
