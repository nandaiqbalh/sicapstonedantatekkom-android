package com.kel022322.sicapstonedantatekkom.data.remote.service

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request.BroadcastDetailRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c100.response.UploadC100ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response.UploadC200ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response.UploadC300ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response.UploadC400ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response.UploadC500ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response.UploadLaporanProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response.SidangProposalByKelompokResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.siklus.response.SiklusRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response.TopikRemoteResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {

	@POST("capstone_team/public/api/v1/auth/login")
	suspend fun authLogin(
		@Body authLoginRequestBody: AuthLoginRequestBody
	): AuthLoginRemoteResponse

	@GET("capstone_team/public/api/v1/auth/logout")
	suspend fun authLogout(
		@Header("Authorization") apiToken: String,
	): AuthLogoutRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/broadcast")
	suspend fun getBroadcast(
	): BroadcastPaginateRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/broadcast-home")
	suspend fun getBroadcastHome(
	): BroadcastPaginateRemoteResponse


	@POST("capstone_team/public/api/v1/mahasiswa/detail-broadcast")
	suspend fun getBroadcastDetail(
		@Body broadcastDetailRemoteRequestBody: BroadcastDetailRemoteRequestBody
	): BroadcastDetailRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/profile")
	suspend fun getMahasiswaProfile(
		@Header("Authorization") apiToken: String,
		): ProfileRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/profile/editProcess")
	suspend fun updateMahasiswaProfile(
		@Header("Authorization") apiToken: String,
		@Body updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody
	) : UpdateProfileRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/profile/editPassword")
	suspend fun updatePasswordProfile(
		@Header("Authorization") apiToken: String,
		@Body updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody
	) : UpdatePasswordRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/profile/editPhotoProcess")
	suspend fun updatePhotoProfile(
		@Header("Authorization") apiToken: String,
		@Part user_img: MultipartBody.Part
	): UpdateProfileRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/topik")
	suspend fun getTopikCapstone(
		@Header("Authorization") apiToken: String,
	) : TopikRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/siklus")
	suspend fun getSiklusCapstone(
		@Header("Authorization") apiToken: String,
	) : SiklusRemoteResponse


	@GET("capstone_team/public/api/v1/mahasiswa/kelompok")
	suspend fun getKelompokSaya(
		@Header("Authorization") apiToken: String,
	) : KelompokSayaRemoteResponse


	@POST("capstone_team/public/api/v1/mahasiswa/kelompok/add-kelompok-process")
	suspend fun addKelompokIndividu(
		@Header("Authorization") apiToken: String,
		@Body addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody
	) : AddKelompokIndividuRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/kelompok/add-punya-kelompok-process")
	suspend fun addKelompokPunyaKelompok(
		@Header("Authorization") apiToken: String,
		@Body addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody
	) : AddKelompokPunyaKelompokRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/data-dosen")
	suspend fun getDataDosen(
		@Header("Authorization") apiToken: String,
	) : DosenRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/data-mahasiswa")
	suspend fun getDataMahasiswa(
		@Header("Authorization") apiToken: String,
	) : MahasiswaIndexRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/upload-file")
	suspend fun getFileIndex(
		@Header("Authorization") apiToken: String,
	) : FileIndexRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-makalah-process")
	suspend fun uploadMakalahProcess(
		@Header("Authorization") apiToken: String,
		@Part makalah: MultipartBody.Part
	): UploadMakalahProcessRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-laporan-process")
	suspend fun uploadLaporanProcess(
		@Header("Authorization") apiToken: String,
		@Part laporan_ta: MultipartBody.Part
	): UploadLaporanProcessRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c100-process")
	suspend fun uploadC100Process(
		@Header("Authorization") apiToken: String,
		@Query("id_kelompok") idKelompok: String,
		@Part c100: MultipartBody.Part
	): UploadC100ProcessRemoteResponse
	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c200-process")
	suspend fun uploadC200Process(
		@Header("Authorization") apiToken: String,
		@Query("id_kelompok") idKelompok: String,
		@Part c200: MultipartBody.Part
	): UploadC200ProcessRemoteResponse
	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c300-process")
	suspend fun uploadC300Process(
		@Header("Authorization") apiToken: String,
		@Query("id_kelompok") idKelompok: String,
		@Part c300: MultipartBody.Part
	): UploadC300ProcessRemoteResponse
	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c400-process")
	suspend fun uploadC400Process(
		@Header("Authorization") apiToken: String,
		@Query("id_kelompok") idKelompok: String,
		@Part c400: MultipartBody.Part
	): UploadC400ProcessRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c500-process")
	suspend fun uploadC500Process(
		@Header("Authorization") apiToken: String,
		@Query("id_kelompok") idKelompok: String,
		@Part c500: MultipartBody.Part
	): UploadC500ProcessRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/sidang-proposal-kelompok")
	suspend fun getSidangProposalByKelompok(
		@Header("Authorization") apiToken: String,
	) : SidangProposalByKelompokResponse

}