package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class RsMahasiswa(
    @SerializedName("created_by") val createdBy: Any?,
    @SerializedName("created_date") val createdDate: Any?,
    @SerializedName("file_name_laporan_ta") val fileNameLaporanTa: String?,
    @SerializedName("file_name_makalah") val fileNameMakalah: String?,
    @SerializedName("file_path_laporan_ta") val filePathLaporanTa: String?,
    @SerializedName("file_path_makalah") val filePathMakalah: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("id_kelompok") val idKelompok: Int?,
    @SerializedName("id_mahasiswa") val idMahasiswa: String?,
    @SerializedName("id_siklus") val idSiklus: Int?,
    @SerializedName("id_topik_mhs") val idTopikMhs: Int?,
    @SerializedName("judul_ta_mhs") val judulTaMhs: Any?,
    @SerializedName("link_upload") val linkUpload: Any?,
    @SerializedName("modified_by") val modifiedBy: Any?,
    @SerializedName("modified_date") val modifiedDate: Any?,
    @SerializedName("nomor_induk") val nomorInduk: String?,
    @SerializedName("progress_kelompok") val progressKelompok: Any?,
    @SerializedName("status_individu") val statusIndividu: Any?,
    @SerializedName("user_name") val userName: String?
)
