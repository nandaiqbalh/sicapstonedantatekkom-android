package com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response

import com.google.gson.annotations.SerializedName

data class DataProfile(
    @SerializedName("alamat")
    val alamat: Any?,

    @SerializedName("angkatan")
    val angkatan: Any?,

    @SerializedName("api_token")
    val apiToken: String?,

    @SerializedName("created_by")
    val createdBy: String?,

    @SerializedName("created_date")
    val createdDate: String?,

    @SerializedName("ipk")
    val ipk: Any?,

    @SerializedName("jenis_kelamin")
    val jenisKelamin: Any?,

    @SerializedName("modified_by")
    val modifiedBy: Any?,

    @SerializedName("modified_date")
    val modifiedDate: Any?,

    @SerializedName("no_telp")
    val noTelp: Any?,

    @SerializedName("nomor_induk")
    val nomorInduk: String?,

    @SerializedName("role_id")
    val roleId: String?,

    @SerializedName("sks")
    val sks: Any?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    @SerializedName("user_active")
    val userActive: String?,

    @SerializedName("user_email")
    val userEmail: Any?,

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("user_img_name")
    val userImgName: Any?,

    @SerializedName("user_img_path")
    val userImgPath: Any?,

    @SerializedName("user_name")
    val userName: String?,

    @SerializedName("user_password")
    val userPassword: String?
)