package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response

import com.google.gson.annotations.SerializedName

data class RsSidang(
    @SerializedName("alamat")
    val alamat: String?,

    @SerializedName("angkatan")
    val angkatan: Int?,

    @SerializedName("created_by")
    val createdBy: String?,

    @SerializedName("created_date")
    val createdDate: String?,

    @SerializedName("hari_sidang")
    val hariSidang: String?,

    @SerializedName("id")
    val id: Int?,

    @SerializedName("id_dosen_penguji_ta1")
    val idDosenPengujiTa1: String?,

    @SerializedName("id_dosen_penguji_ta2")
    val idDosenPengujiTa2: String?,

    @SerializedName("id_kelompok_mhs")
    val idKelompokMhs: String?,

    @SerializedName("id_mahasiswa")
    val idMahasiswa: String?,

    @SerializedName("id_ruangan")
    val idRuangan: String?,

    @SerializedName("ipk")
    val ipk: String?,

    @SerializedName("jenis_kelamin")
    val jenisKelamin: String?,

    @SerializedName("judul_ta_mhs")
    val judulTaMhs: String?,

    @SerializedName("link_upload")
    val linkUpload: String?,

    @SerializedName("modified_by")
    val modifiedBy: String?,

    @SerializedName("modified_date")
    val modifiedDate: String?,

    @SerializedName("nama_ruang")
    val namaRuang: String?,

    @SerializedName("no_telp")
    val noTelp: String?,

    @SerializedName("nomor_induk")
    val nomorInduk: String?,

    @SerializedName("role_id")
    val roleId: String?,

    @SerializedName("sks")
    val sks: Int?,

    @SerializedName("status_dosen_penguji_ta1")
    val statusDosenPengujiTa1: String?,

    @SerializedName("status_dosen_penguji_ta2")
    val statusDosenPengujiTa2: String?,

    @SerializedName("status_individu")
    val statusIndividu: String?,

    @SerializedName("tanggal_sidang")
    val tanggalSidang: String?,

    @SerializedName("user_active")
    val userActive: String?,

    @SerializedName("user_email")
    val userEmail: String?,

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("user_img_name")
    val userImgName: String?,

    @SerializedName("user_img_path")
    val userImgPath: String?,

    @SerializedName("user_name")
    val userName: String?,

    @SerializedName("user_password")
    val userPassword: String?,

    @SerializedName("waktu")
    val waktu: String?,

    @SerializedName("waktu_sidang")
    val waktuSidang: String?,
)