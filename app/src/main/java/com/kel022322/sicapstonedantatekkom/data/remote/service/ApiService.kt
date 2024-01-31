package com.kel022322.sicapstonedantatekkom.data.remote.service

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request.AuthLogoutRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c100.response.UploadC100ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response.UploadC200ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response.UploadC300ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response.UploadC400ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response.UploadC500ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response.UploadLaporanProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.request.ViewPdfRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.response.ViewPdfRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.request.PhotoProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.response.PhotoProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
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

	@GET("capstone_team/public/api/v1/mahasiswa/broadcast")
	suspend fun getBroadcast(
	): BroadcastPaginateRemoteResponse

	@GET("capstone_team/public/api/v1/mahasiswa/broadcast-home")
	suspend fun getBroadcastHome(
	): BroadcastPaginateRemoteResponse


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

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-laporan-process")
	suspend fun uploadLaporanProcess(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Query("id_mahasiswa") idMahasiswa: String,
		@Part laporan_ta: MultipartBody.Part
	): UploadLaporanProcessRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c100-process")
	suspend fun uploadC100Process(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Query("id") id: String,
		@Part c100: MultipartBody.Part
	): UploadC100ProcessRemoteResponse
	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c200-process")
	suspend fun uploadC200Process(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Query("id") id: String,
		@Part c200: MultipartBody.Part
	): UploadC200ProcessRemoteResponse
	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c300-process")
	suspend fun uploadC300Process(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Query("id") id: String,
		@Part c300: MultipartBody.Part
	): UploadC300ProcessRemoteResponse
	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c400-process")
	suspend fun uploadC400Process(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Query("id") id: String,
		@Part c400: MultipartBody.Part
	): UploadC400ProcessRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/upload-file/upload-c500-process")
	suspend fun uploadC500Process(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Query("id") id: String,
		@Part c500: MultipartBody.Part
	): UploadC500ProcessRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/view-pdf")
	suspend fun viewPdf(
		@Body viewPdfRemoteRequestBody: ViewPdfRemoteRequestBody
	) : ViewPdfRemoteResponse

	@POST("capstone_team/public/api/v1/mahasiswa/profile/img-user")
	suspend fun getPhotoProfile(
		@Body photoProfileRemoteRequestBody: PhotoProfileRemoteRequestBody
	) : PhotoProfileRemoteResponse

	@Multipart
	@POST("capstone_team/public/api/v1/mahasiswa/profile/editPhotoProcess")
	suspend fun updatePhotoProfile(
		@Query("user_id") userId: String,
		@Query("api_token") apiToken: String,
		@Part user_img: MultipartBody.Part
	): UpdateProfileRemoteResponse
}