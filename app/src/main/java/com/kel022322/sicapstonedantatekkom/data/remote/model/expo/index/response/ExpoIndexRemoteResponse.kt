package com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response

import com.google.gson.annotations.SerializedName

data class ExpoIndexRemoteResponse(
    @SerializedName("data") val data: Data?,
    @SerializedName("status") val status: String?,
    @SerializedName("success") val success: Boolean?,
)

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

data class CekExpo(
    @SerializedName("status_expo")
    val statusExpo: String
)

data class Kelengkapan(
    @SerializedName("created_by")
    val createdBy: Any,

    @SerializedName("created_date")
    val createdDate: Any,

    @SerializedName("file_name_laporan_ta")
    val fileNameLaporanTA: String,

    @SerializedName("file_name_makalah")
    val fileNameMakalah: String,

    @SerializedName("file_path_laporan_ta")
    val filePathLaporanTA: String,

    @SerializedName("file_path_makalah")
    val filePathMakalah: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_kelompok")
    val idKelompok: Int,

    @SerializedName("id_mahasiswa")
    val idMahasiswa: String,

    @SerializedName("id_peminatan_individu1")
    val idPeminatanIndividu1: Any,

    @SerializedName("id_peminatan_individu2")
    val idPeminatanIndividu2: Any,

    @SerializedName("id_peminatan_individu3")
    val idPeminatanIndividu3: Any,

    @SerializedName("id_peminatan_individu4")
    val idPeminatanIndividu4: Any,

    @SerializedName("id_siklus")
    val idSiklus: Int,

    @SerializedName("id_topik_individu1")
    val idTopikIndividu1: Any,

    @SerializedName("id_topik_individu2")
    val idTopikIndividu2: Any,

    @SerializedName("id_topik_individu3")
    val idTopikIndividu3: Any,

    @SerializedName("id_topik_individu4")
    val idTopikIndividu4: Any,

    @SerializedName("id_topik_mhs")
    val idTopikMhs: Int,

    @SerializedName("judul_ta_mhs")
    val judulTAMhs: String,

    @SerializedName("link_berkas_expo")
    val linkBerkasExpo: String,

    @SerializedName("link_upload")
    val linkUpload: String,

    @SerializedName("modified_by")
    val modifiedBy: Any,

    @SerializedName("modified_date")
    val modifiedDate: Any,

    @SerializedName("status_individu")
    val statusIndividu: Any,

    @SerializedName("usulan_judul_capstone")
    val usulanJudulCapstone: Any
)

data class Kelompok(
    @SerializedName("created_by")
    val createdBy: Any,

    @SerializedName("created_date")
    val createdDate: Any,

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
    val idDosenPengujiTa1: Any,

    @SerializedName("id_dosen_penguji_ta_2")
    val idDosenPengujiTa2: Any,

    @SerializedName("id_kelompok")
    val idKelompok: Int,

    @SerializedName("id_siklus")
    val idSiklus: Int,

    @SerializedName("id_topik")
    val idTopik: Int,

    @SerializedName("judul_capstone")
    val judulCapstone: String,

    @SerializedName("link_berkas_expo")
    val linkBerkasExpo: String,

    @SerializedName("modified_by")
    val modifiedBy: String,

    @SerializedName("modified_date")
    val modifiedDate: String,

    @SerializedName("nama_topik")
    val namaTopik: String,

    @SerializedName("nomor_kelompok")
    val nomorKelompok: String,

    @SerializedName("status_dosen_pembimbing_1")
    val statusDosenPembimbing1: String,

    @SerializedName("status_dosen_pembimbing_2")
    val statusDosenPembimbing2: String,

    @SerializedName("status_dosen_penguji_1")
    val statusDosenPenguji1: String,

    @SerializedName("status_dosen_penguji_2")
    val statusDosenPenguji2: Any,

    @SerializedName("status_dosen_penguji_ta1")
    val statusDosenPengujiTa1: Any,

    @SerializedName("status_dosen_penguji_ta2")
    val statusDosenPengujiTa2: Any,

    @SerializedName("status_kelompok")
    val statusKelompok: String
)

data class RsExpo(
    @SerializedName("created_by")
    val createdBy: String,

    @SerializedName("created_date")
    val createdDate: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_siklus")
    val idSiklus: Int,

    @SerializedName("modified_by")
    val modifiedBy: String,

    @SerializedName("modified_date")
    val modifiedDate: String,

    @SerializedName("tahun_ajaran")
    val tahunAjaran: String,

    @SerializedName("tanggal_mulai")
    val tanggalMulai: String,

    @SerializedName("tempat")
    val tempat: String,

    @SerializedName("tanggal_selesai")
    val tanggalSelesai: String,

    @SerializedName("hari_expo")
    val hariExpo: String,

    @SerializedName("tanggal_expo")
    val tanggalExpo: String,

    @SerializedName("waktu_expo")
    val waktuExpo: String,

    @SerializedName("hari_batas")
    val hariBatas: String,

    @SerializedName("tanggal_batas")
    val tanggalBatas: String,

    @SerializedName("waktu_batas")
    val waktuBatas: String,


    )