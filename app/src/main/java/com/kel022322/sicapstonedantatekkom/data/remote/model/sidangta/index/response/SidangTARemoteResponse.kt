package com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response

import com.google.gson.annotations.SerializedName

data class SidangTARemoteResponse(
	@SerializedName("data") val data: Data?,

	@SerializedName("status") val status: String?,

	@SerializedName("success") val success: Boolean?,
)

data class Data(
	@SerializedName("kelompok") val kelompok: Kelompok?,
	@SerializedName("periode") val periode: Periode?,
	@SerializedName("rsSidang") val rsSidang: RsSidang?,
	@SerializedName("status_pendaftaran") val statusPendaftaran: StatusPendaftaran?,
)

data class Kelompok(
	@SerializedName("id_kelompok") val idKelompok: Int?,
	@SerializedName("id") val id: Int?,
	@SerializedName("id_siklus") val idSiklus: Int?,
	@SerializedName("nomor_kelompok") val nomorKelompok: Int?,
	@SerializedName("id_topik") val idTopik: Int?,
	@SerializedName("status_kelompok") val statusKelompok: String?,
	@SerializedName("judul_capstone") val judulCapstone: String?,
	@SerializedName("id_dosen_pembimbing_1") val idDosenPembimbing1: String?,
	@SerializedName("status_dosen_pembimbing_1") val statusDosenPembimbing1: String?,
	@SerializedName("id_dosen_pembimbing_2") val idDosenPembimbing2: String?,
	@SerializedName("status_dosen_pembimbing_2") val statusDosenPembimbing2: String?,
	@SerializedName("id_dosen_penguji_1") val idDosenPenguji1: String?,
	@SerializedName("status_dosen_penguji_1") val statusDosenPenguji1: String?,
	@SerializedName("id_dosen_penguji_2") val idDosenPenguji2: String?,
	@SerializedName("status_dosen_penguji_2") val statusDosenPenguji2: String?,
	@SerializedName("id_dosen_penguji_ta_1") val idDosenPengujiTa1: String?,
	@SerializedName("status_dosen_penguji_ta1") val statusDosenPengujiTa1: String?,
	@SerializedName("id_dosen_penguji_ta_2") val idDosenPengujiTa2: String?,
	@SerializedName("status_dosen_penguji_ta2") val statusDosenPengujiTa2: String?,
	@SerializedName("file_name_c100") val fileNameC100: String?,
	@SerializedName("file_path_c100") val filePathC100: String?,
	@SerializedName("file_name_c200") val fileNameC200: String?,
	@SerializedName("file_path_c200") val filePathC200: String?,
	@SerializedName("file_name_c300") val fileNameC300: String?,
	@SerializedName("file_path_c300") val filePathC300: String?,
	@SerializedName("file_name_c400") val fileNameC400: String?,
	@SerializedName("file_path_c400") val filePathC400: String?,
	@SerializedName("file_name_c500") val fileNameC500: String?,
	@SerializedName("file_path_c500") val filePathC500: String?,
	@SerializedName("created_by") val createdBy: String?,
	@SerializedName("created_date") val createdDate: String?,
	@SerializedName("modified_by") val modifiedBy: String?,
	@SerializedName("modified_date") val modifiedDate: String?,
	@SerializedName("nama_topik") val namaTopik: String?,
)

data class Periode(
	@SerializedName("created_by") val createdBy: String?,
	@SerializedName("created_date") val createdDate: String?,
	@SerializedName("id") val id: Int,
	@SerializedName("modified_by") val modifiedBy: String?,
	@SerializedName("modified_date") val modifiedDate: String?,
	@SerializedName("nama_periode") val namaPeriode: String?,
	@SerializedName("tanggal_mulai") val tanggalMulai: String?,
	@SerializedName("tanggal_selesai") val tanggalSelesai: String?,
	@SerializedName("hari_batas") val hariBatas: String?,
	@SerializedName("tanggal_batas") val tanggalBatas: String?,
	@SerializedName("waktu_batas") val waktuBatas: String?,

	)

data class RsSidang(
	@SerializedName("alamat") val alamat: String?,

	@SerializedName("angkatan") val angkatan: Int?,

	@SerializedName("created_by") val createdBy: String?,

	@SerializedName("created_date") val createdDate: String?,

	@SerializedName("hari_sidang") val hariSidang: String?,

	@SerializedName("id") val id: Int?,

	@SerializedName("id_dosen_penguji_ta1") val idDosenPengujiTa1: String?,

	@SerializedName("id_dosen_penguji_ta2") val idDosenPengujiTa2: String?,

	@SerializedName("id_kelompok_mhs") val idKelompokMhs: String?,

	@SerializedName("id_mahasiswa") val idMahasiswa: String?,

	@SerializedName("id_ruangan") val idRuangan: String?,

	@SerializedName("ipk") val ipk: String?,

	@SerializedName("jenis_kelamin") val jenisKelamin: String?,

	@SerializedName("judul_ta_mhs") val judulTaMhs: String?,

	@SerializedName("link_upload") val linkUpload: String?,

	@SerializedName("modified_by") val modifiedBy: String?,

	@SerializedName("modified_date") val modifiedDate: String?,

	@SerializedName("nama_ruang") val namaRuang: String?,

	@SerializedName("no_telp") val noTelp: String?,

	@SerializedName("nomor_induk") val nomorInduk: String?,

	@SerializedName("role_id") val roleId: String?,

	@SerializedName("sks") val sks: Int?,

	@SerializedName("status_dosen_penguji_ta1") val statusDosenPengujiTa1: String?,

	@SerializedName("status_dosen_penguji_ta2") val statusDosenPengujiTa2: String?,

	@SerializedName("status_individu") val statusIndividu: String?,

	@SerializedName("tanggal_sidang") val tanggalSidang: String?,

	@SerializedName("user_active") val userActive: String?,

	@SerializedName("user_email") val userEmail: String?,

	@SerializedName("user_id") val userId: String?,

	@SerializedName("user_img_name") val userImgName: String?,

	@SerializedName("user_img_path") val userImgPath: String?,

	@SerializedName("user_name") val userName: String?,

	@SerializedName("user_password") val userPassword: String?,

	@SerializedName("waktu") val waktu: String?,

	@SerializedName("waktu_sidang") val waktuSidang: String?,
)

data class StatusPendaftaran(
	@SerializedName("created_by") val createdBy: String?,
	@SerializedName("created_date") val createdDate: String?,
	@SerializedName("id") val id: Int?,
	@SerializedName("id_mahasiswa") val idMahasiswa: String?,
	@SerializedName("modified_by") val modifiedBy: String?,
	@SerializedName("modified_date") val modifiedDate: String?,
	@SerializedName("status") val status: String,
)
