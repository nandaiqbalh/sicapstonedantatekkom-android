package com.kel022322.sicapstonedantatekkom.data.remote.service

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request.AuthLogoutRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.BroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

	@POST("capstone_team/public/api/v1/auth/login")
	suspend fun authLogin(
		@Body authLoginRequestBody: AuthLoginRequestBody
	): AuthLoginRemoteResponse

	@POST("capstone_team/public/api/v1/auth/logout")
	suspend fun authLogout(
		@Body authLogoutRequestBody: AuthLogoutRequestBody
	): AuthLogoutRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/broadcast")
	suspend fun getBroadcast(
	): BroadcastRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/broadcast/{id}")
	suspend fun getBroadcastDetail(
		@Path("id") id: String
	): BroadcastDetailRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/profile")
	suspend fun getMahasiswaProfile(
		@Body profileRemoteRequestBody: ProfileRemoteRequestBody
	): ProfileRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/profile/editProcess")
	suspend fun updateMahasiswaProfile(
		@Body updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody
	) : UpdateProfileRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/profile/editPassword")
	suspend fun updatePasswordProfile(
		@Body updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody
	) : UpdatePasswordRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/kelompok")
	suspend fun getKelompokSaya(
		@Body kelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody
	) : KelompokSayaRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/kelompok/add-kelompok-process")
	suspend fun addKelompokIndividu(
		@Body addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody
	) : AddKelompokIndividuRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/kelompok/add-punya-kelompok-process")
	suspend fun addKelompokPunyaKelompok(
		@Body addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody
	) : AddKelompokPunyaKelompokRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/upload-file")
	suspend fun getFileIndex(
		@Body fileIndexRemoteRequestBody: FileIndexRemoteRequestBody
	) : FileIndexRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-makalah-process")
	suspend fun uploadMakalahProcess(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Query("id_mahasiswa") idMahasiswa: String,
		@Part makalah: MultipartBody.Part
	): UploadMakalahProcessRemoteResponse
}