package com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response
import com.google.gson.annotations.SerializedName

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
