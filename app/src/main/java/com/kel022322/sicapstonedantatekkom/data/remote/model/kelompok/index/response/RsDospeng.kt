package com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response

import com.google.gson.annotations.SerializedName

data class RsDospeng(
    @SerializedName("user_id") val userId: String,
    @SerializedName("role_id") val roleId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_email") val userEmail: String?,
    @SerializedName("user_password") val userPassword: String,
    @SerializedName("user_active") val userActive: String,
    @SerializedName("user_img_path") val userImgPath: String?,
    @SerializedName("user_img_name") val userImgName: String?,
    @SerializedName("nomor_induk") val nomorInduk: String,
    @SerializedName("no_telp") val noTelp: String?,
    @SerializedName("angkatan") val angkatan: Int?,
    @SerializedName("ipk") val ipk: String?,
    @SerializedName("sks") val sks: Int?,
    @SerializedName("jenis_kelamin") val jenisKelamin: String?,
    @SerializedName("alamat") val alamat: String?,
    @SerializedName("created_by") val createdBy: String,
    @SerializedName("created_date") val createdDate: String,
    @SerializedName("modified_by") val modifiedBy: String,
    @SerializedName("modified_date") val modifiedDate: String,
    @SerializedName("user_img_url") val userImgUrl: String,

    )