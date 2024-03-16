package com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response

import com.google.gson.annotations.SerializedName

data class FileIndexRemoteResponse(
    @SerializedName("status")
    val status: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: Data?
)

data class Data(
    @SerializedName("file_mhs")
    val fileMhs: FileMhs?
)

data class FileMhs(
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("file_name_c100")
    val fileNameC100: String,
    @SerializedName("file_name_c200")
    val fileNameC200: String,
    @SerializedName("file_name_c300")
    val fileNameC300: String,
    @SerializedName("file_name_c400")
    val fileNameC400: String,
    @SerializedName("file_name_c500")
    val fileNameC500: String,
    @SerializedName("file_name_laporan_ta")
    val fileNameLaporanTa: String,
    @SerializedName("file_name_makalah")
    val fileNameMakalah: String,
    @SerializedName("file_path_c100")
    val filePathC100: String,
    @SerializedName("file_path_c200")
    val filePathC200: String,
    @SerializedName("file_path_c300")
    val filePathC300: String,
    @SerializedName("file_path_c400")
    val filePathC400: String,
    @SerializedName("file_path_c500")
    val filePathC500: String,
    @SerializedName("file_path_laporan_ta")
    val filePathLaporanTa: String,
    @SerializedName("file_path_makalah")
    val filePathMakalah: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_dosen_pembimbing_1")
    val idDosenPembimbing1: String,
    @SerializedName("id_dosen_pembimbing_2")
    val idDosenPembimbing2: String,
    @SerializedName("id_dosen_penguji_1")
    val idDosenPenguji1: String,
    @SerializedName("id_dosen_penguji_2")
    val idDosenPenguji2: String,
    @SerializedName("id_dosen_penguji_ta_1")
    val idDosenPengujiTa1: String,
    @SerializedName("id_dosen_penguji_ta_2")
    val idDosenPengujiTa2: String,
    @SerializedName("id_kel_mhs")
    val idKelMhs: Int,
    @SerializedName("id_siklus")
    val idSiklus: Int,
    @SerializedName("id_topik")
    val idTopik: Int,
    @SerializedName("judul_capstone")
    val judulCapstone: String,
    @SerializedName("modified_by")
    val modifiedBy: String,
    @SerializedName("modified_date")
    val modifiedDate: String,
    @SerializedName("nomor_kelompok")
    val nomorKelompok: String,
    @SerializedName("status_dosen_pembimbing_1")
    val statusDosenPembimbing1: String,
    @SerializedName("status_dosen_pembimbing_2")
    val statusDosenPembimbing2: String,
    @SerializedName("status_dosen_penguji_1")
    val statusDosenPenguji1: String,
    @SerializedName("status_dosen_penguji_2")
    val statusDosenPenguji2: String,
    @SerializedName("status_dosen_penguji_ta1")
    val statusDosenPengujiTa1: String,
    @SerializedName("status_dosen_penguji_ta2")
    val statusDosenPengujiTa2: String,
    @SerializedName("status_kelompok")
    val statusKelompok: String,
    @SerializedName("file_url_c100")
    val fileUrlC100: String,
    @SerializedName("file_url_c200")
    val fileUrlC200: String,
    @SerializedName("file_url_c300")
    val fileUrlC300: String,
    @SerializedName("file_url_c400")
    val fileUrlC400: String,
    @SerializedName("file_url_c500")
    val fileUrlC500: String,
    @SerializedName("file_url_laporan_ta")
    val fileUrlLaporanTa: String,
    @SerializedName("file_url_makalah")
    val fileUrlMakalah: String,
)
