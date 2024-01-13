package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.response

import com.google.gson.annotations.SerializedName

data class Kelompok(
    @SerializedName("created_by") val createdBy: Any?,
    @SerializedName("created_date") val createdDate: Any?,
    @SerializedName("file_name_c100") val fileNameC100: String?,
    @SerializedName("file_name_c200") val fileNameC200: String?,
    @SerializedName("file_name_c300") val fileNameC300: String?,
    @SerializedName("file_name_c400") val fileNameC400: String?,
    @SerializedName("file_name_c500") val fileNameC500: String?,
    @SerializedName("file_name_laporan_ta") val fileNameLaporanTa: String?,
    @SerializedName("file_name_makalah") val fileNameMakalah: String?,
    @SerializedName("file_path_c100") val filePathC100: String?,
    @SerializedName("file_path_c200") val filePathC200: String?,
    @SerializedName("file_path_c300") val filePathC300: String?,
    @SerializedName("file_path_c400") val filePathC400: String?,
    @SerializedName("file_path_c500") val filePathC500: String?,
    @SerializedName("file_path_laporan_ta") val filePathLaporanTa: String?,
    @SerializedName("file_path_makalah") val filePathMakalah: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("id_dosen_pembimbing_1") val idDosenPembimbing1: String?,
    @SerializedName("id_dosen_pembimbing_2") val idDosenPembimbing2: String?,
    @SerializedName("id_dosen_penguji_1") val idDosenPenguji1: Any?,
    @SerializedName("id_dosen_penguji_2") val idDosenPenguji2: Any?,
    @SerializedName("id_kelompok") val idKelompok: Int?,
    @SerializedName("id_mahasiswa") val idMahasiswa: String?,
    @SerializedName("id_siklus") val idSiklus: Int?,
    @SerializedName("id_topik") val idTopik: Int?,
    @SerializedName("id_topik_mhs") val idTopikMhs: Int?,
    @SerializedName("judul_capstone") val judulCapstone: String?,
    @SerializedName("judul_ta_mhs") val judulTaMhs: Any?,
    @SerializedName("link_upload") val linkUpload: Any?,
    @SerializedName("modified_by") val modifiedBy: Any?,
    @SerializedName("modified_date") val modifiedDate: Any?,
    @SerializedName("nama_topik") val namaTopik: String?,
    @SerializedName("nomor_kelompok") val nomorKelompok: Any?,
    @SerializedName("progress_kelompok") val progressKelompok: Any?,
    @SerializedName("status_dosen_pembimbing_1") val statusDosenPembimbing1: Any?,
    @SerializedName("status_dosen_pembimbing_2") val statusDosenPembimbing2: Any?,
    @SerializedName("status_dosen_penguji_1") val statusDosenPenguji1: Any?,
    @SerializedName("status_dosen_penguji_2") val statusDosenPenguji2: Any?,
    @SerializedName("status_individu") val statusIndividu: Any?,
    @SerializedName("status_kelompok") val statusKelompok: String?
)
