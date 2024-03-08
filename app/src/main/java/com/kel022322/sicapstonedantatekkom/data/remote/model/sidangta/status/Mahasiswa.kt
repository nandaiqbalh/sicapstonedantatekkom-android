package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status

import com.google.gson.annotations.SerializedName

data class Mahasiswa(
    @SerializedName("created_by") val createdBy: String,
    @SerializedName("created_date") val createdDate: String,
    @SerializedName("file_name_laporan_ta") val fileNameLaporanTa: String,
    @SerializedName("file_name_makalah") val fileNameMakalah: String,
    @SerializedName("file_path_laporan_ta") val filePathLaporanTa: String,
    @SerializedName("file_path_makalah") val filePathMakalah: String,
    @SerializedName("id") val id: Int,
    @SerializedName("id_dosen_penguji_ta1") val idDosenPengujiTa1: String,
    @SerializedName("id_dosen_penguji_ta2") val idDosenPengujiTa2: String,
    @SerializedName("id_kelompok") val idKelompok: Int,
    @SerializedName("id_mahasiswa") val idMahasiswa: String,
    @SerializedName("id_peminatan_individu1") val idPeminatanIndividu1: String,
    @SerializedName("id_peminatan_individu2") val idPeminatanIndividu2: String,
    @SerializedName("id_peminatan_individu3") val idPeminatanIndividu3: String,
    @SerializedName("id_peminatan_individu4") val idPeminatanIndividu4: String,
    @SerializedName("id_siklus") val idSiklus: Int,
    @SerializedName("id_topik_individu1") val idTopikIndividu1: String,
    @SerializedName("id_topik_individu2") val idTopikIndividu2: String,
    @SerializedName("id_topik_individu3") val idTopikIndividu3: String,
    @SerializedName("id_topik_individu4") val idTopikIndividu4: String,
    @SerializedName("id_topik_mhs") val idTopikMhs: Int,
    @SerializedName("judul_ta_mhs") val judulTaMhs: String,
    @SerializedName("link_upload") val linkUpload: String,
    @SerializedName("modified_by") val modifiedBy: String,
    @SerializedName("modified_date") val modifiedDate: String,
    @SerializedName("status_dosen_penguji_ta1") val statusDosenPengujiTa1: String,
    @SerializedName("status_dosen_penguji_ta2") val statusDosenPengujiTa2: String,
    @SerializedName("status_individu") val statusIndividu: String,
    @SerializedName("usulan_judul_capstone") val usulanJudulCapstone: String
)
