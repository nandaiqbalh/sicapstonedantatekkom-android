package com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response

import com.google.gson.annotations.SerializedName

data class MahasiswaIndexRemoteResponse(
    @SerializedName("data") val data: DataMahasiswa?,
    @SerializedName("status") val status: String?,
    @SerializedName("success") val success: Boolean?,
)

data class DataMahasiswa(
    val rs_mahasiswa: List<RsMahasiswa>
)

data class RsMahasiswa(
    @SerializedName("alamat") val alamat: String?,
    @SerializedName("angkatan") val angkatan: String?,
    @SerializedName("api_token") val apiToken: String?,
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("ipk") val ipk: String?,
    @SerializedName("jenis_kelamin") val jenisKelamin: String?,
    @SerializedName("modified_by") val modifiedBy: String?,
    @SerializedName("modified_date") val modifiedDate: String?,
    @SerializedName("no_telp") val noTelp: String?,
    @SerializedName("nomor_induk") val nomorInduk: String?,
    @SerializedName("role_id") val roleId: String?,
    @SerializedName("role_name") val roleName: String?,
    @SerializedName("sks") val sks: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("user_active") val userActive: String?,
    @SerializedName("user_email") val userEmail: String?,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("user_img_name") val userImgName: String?,
    @SerializedName("user_img_path") val userImgPath: String?,
    @SerializedName("user_name") val userName: String?,
    @SerializedName("user_password") val userPassword: String?
)