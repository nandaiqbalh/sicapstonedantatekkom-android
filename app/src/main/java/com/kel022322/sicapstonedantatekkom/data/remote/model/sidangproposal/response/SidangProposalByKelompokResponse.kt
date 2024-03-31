package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response

import com.google.gson.annotations.SerializedName

data class SidangProposalByKelompokResponse(
    @SerializedName("data") val data: Data?,
    @SerializedName("status") val status: String?,
    @SerializedName("success") val success: Boolean?
)

data class Data(
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("hari_sidang") val hariSidang: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("id_kelompok") val idKelompok: Int?,
    @SerializedName("judul_capstone") val judulCapstone: String?,
    @SerializedName("kelompok") val kelompok: Kelompok?,
    @SerializedName("kode_ruang") val kodeRuang: String?,
    @SerializedName("modified_by") val modifiedBy: String?,
    @SerializedName("modified_date") val modifiedDate: String?,
    @SerializedName("nama_ruang") val namaRuang: String?,
    @SerializedName("ruangan_id") val ruanganId: Int?,
    @SerializedName("siklus_id") val siklusId: Int?,
    @SerializedName("status_kelompok") val statusKelompok: String?,
    @SerializedName("tahun_ajaran") val tahunAjaran: String?,
    @SerializedName("tanggal_sidang") val tanggalSidang: String?,
    @SerializedName("waktu") val waktu: String?,
    @SerializedName("waktu_sidang") val waktuSidang: String?
)

data class Kelompok(
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("file_name_c100") val fileNameC100: String?,
    @SerializedName("file_name_c200") val fileNameC200: String?,
    @SerializedName("file_name_c300") val fileNameC300: String?,
    @SerializedName("file_name_c400") val fileNameC400: String?,
    @SerializedName("file_name_c500") val fileNameC500: String?,
    @SerializedName("file_path_c100") val filePathC100: String?,
    @SerializedName("file_path_c200") val filePathC200: String?,
    @SerializedName("file_path_c300") val filePathC300: String?,
    @SerializedName("file_path_c400") val filePathC400: String?,
    @SerializedName("file_path_c500") val filePathC500: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("id_dosen_pembimbing_1") val idDosenPembimbing1: String?,
    @SerializedName("id_dosen_pembimbing_2") val idDosenPembimbing2: String?,
    @SerializedName("id_dosen_penguji_1") val idDosenPenguji1: String?,
    @SerializedName("id_dosen_penguji_2") val idDosenPenguji2: String?,
    @SerializedName("id_kelompok") val idKelompok: Int?,
    @SerializedName("id_siklus") val idSiklus: Int?,
    @SerializedName("id_topik") val idTopik: Int?,
    @SerializedName("judul_capstone") val judulCapstone: String?,
    @SerializedName("link_berkas_expo") val linkBerkasExpo: String?,
    @SerializedName("modified_by") val modifiedBy: String?,
    @SerializedName("modified_date") val modifiedDate: String?,
    @SerializedName("nama_topik") val namaTopik: String?,
    @SerializedName("nomor_kelompok") val nomorKelompok: String?,
    @SerializedName("status_dosen_pembimbing_1") val statusDosenPembimbing1: String?,
    @SerializedName("status_dosen_pembimbing_2") val statusDosenPembimbing2: String?,
    @SerializedName("status_dosen_penguji_1") val statusDosenPenguji1: String?,
    @SerializedName("status_dosen_penguji_2") val statusDosenPenguji2: String?,
    @SerializedName("status_kelompok") val statusKelompok: String?,
    @SerializedName("status_sidang_proposal") val statusSidangProposal: String?
)
