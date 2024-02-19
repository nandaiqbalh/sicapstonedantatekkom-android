package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_kelompok")
    val idKelompok: Int,
    @SerializedName("judul_capstone")
    val judulCapstone: String,
    @SerializedName("kelompok")
    val kelompok: Kelompok,
    @SerializedName("kode_ruang")
    val kodeRuang: String,
    @SerializedName("modified_by")
    val modifiedBy: String,
    @SerializedName("modified_date")
    val modifiedDate: String,
    @SerializedName("nama_ruang")
    val namaRuang: String,
    @SerializedName("ruangan_id")
    val ruanganId: String,
    @SerializedName("siklus_id")
    val siklusId: Int,
    @SerializedName("status_kelompok")
    val statusKelompok: String,
    @SerializedName("tahun_ajaran")
    val tahunAjaran: String,
    @SerializedName("waktu")
    val waktu: String,
    @SerializedName("hari_sidang")
    val hariSidang: String,
    @SerializedName("tanggal_sidang")
    val tanggalSidang: String,
    @SerializedName("waktu_sidang")
    val waktuSidang: String

)
