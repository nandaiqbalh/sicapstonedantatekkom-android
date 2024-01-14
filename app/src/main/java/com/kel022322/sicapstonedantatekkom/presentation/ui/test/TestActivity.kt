package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request.AuthLogoutRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.ActivityTestBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login.LoginViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.logout.LogoutViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.pengumuman.PengumumanViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompoksaya.KelompokSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.profilsaya.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

	private var _binding: ActivityTestBinding? = null
	private val binding get() = _binding!!

	private val viewModel: LoginViewModel by viewModels()
	private val logoutViewModel: LogoutViewModel by viewModels()
	private val pengumumanViewModel: PengumumanViewModel by viewModels()
	private val profileViewModel: ProfileSayaViewModel by viewModels()
	private val kelompokSayaViewModel: KelompokSayaViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityTestBinding.inflate(layoutInflater)
		setContentView(binding.root)

		testAPI()
	}

	private fun testAPI() {

		binding.btnTestLogin.setOnClickListener {

			setLoading(true)

			viewModel.authLogin(AuthLoginRequestBody("21120120130101", "mahasiswa123"))

			viewModel.authResult.observe(this) { authResult ->

				when (authResult) {
					is Resource.Loading -> setLoading(true)
					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", authResult.payload?.status.toString())
						Log.d("Result message", authResult.payload?.message.toString())
						Log.d("Exception", authResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${authResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", authResult.payload?.status.toString())
						Log.d("Result message", authResult.payload?.message.toString())
						Log.d("Result Auth", authResult.payload?.userData.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${authResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

						// Access user_id and api_token from userData
						val userId = authResult.payload?.userData?.userId.toString()
						val apiToken = authResult.payload?.userData?.apiToken

						Log.d("Result Auth", "user_id: $userId, api_token: $apiToken")

						Toast.makeText(
							this@TestActivity,
							"Result: ${authResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

						viewModel.setStatusAuth(true)
						// Call the function to setApiToken with the obtained apiToken
						viewModel.setUserId(userId)
						viewModel.setApiToken(apiToken ?: "")
					}

					else -> {}

				}
			}
		}

		binding.btnTestLogout.setOnClickListener {
			setLoading(true)

			var userId = ""
			logoutViewModel.getUserId().observe(this) { userIdd ->

				if (userIdd != null) {
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			logoutViewModel.getApiToken().observe(this) { apiTokenn ->

				if (apiTokenn != null) {
					apiToken = apiTokenn.toString()
				}
			}


			logoutViewModel.authLogout(AuthLogoutRequestBody(userId, apiToken))

			logoutViewModel.logoutResult.observe(this) { logoutResult ->
				when (logoutResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", logoutResult.payload?.status.toString())
						Log.d("Result message", logoutResult.payload?.message.toString())
						Log.d("Exception", logoutResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${logoutResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", logoutResult.payload?.status.toString())
						Log.d("Result message", logoutResult.payload?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${logoutResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

						logoutViewModel.setApiToken("")
						logoutViewModel.setUserId("")
						logoutViewModel.setStatusAuth(false)
					}

					else -> {}
				}
			}
		}

		binding.btnTestBroadcast.setOnClickListener {

			setLoading(true)

			pengumumanViewModel.getBroadcast()

			pengumumanViewModel.broadcastResult.observe(this) { broadcastResult ->

				when (broadcastResult) {
					is Resource.Loading -> setLoading(true)
					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", broadcastResult.payload?.status.toString())
						Log.d("Result message", broadcastResult.payload?.message.toString())
						Log.d("Exception", broadcastResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${broadcastResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", broadcastResult.payload?.status.toString())
						Log.d("Result message", broadcastResult.payload?.message.toString())
						Log.d("Result Auth", broadcastResult.payload?.data.toString())

						Toast.makeText(
							this@TestActivity,
							"Result: ${broadcastResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()
					}

					else -> {}

				}
			}
		}

		binding.btnTestBroadcastDetail.setOnClickListener {
			setLoading(true)

			pengumumanViewModel.getBroadcastDetail("1")

			pengumumanViewModel.broadcastDetailResult.observe(this) { broadcastDetailResult ->

				when (broadcastDetailResult) {
					is Resource.Loading -> setLoading(true)
					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", broadcastDetailResult.payload?.status.toString())
						Log.d("Result message", broadcastDetailResult.payload?.message.toString())
						Log.d("Exception", broadcastDetailResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${broadcastDetailResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", broadcastDetailResult.payload?.status.toString())
						Log.d("Result message", broadcastDetailResult.payload?.message.toString())
						Log.d("Result Auth", broadcastDetailResult.payload?.data.toString())

						Toast.makeText(
							this@TestActivity,
							"Result: ${broadcastDetailResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()
					}

					else -> {}

				}
			}
		}

		binding.btnTestGetProfile.setOnClickListener {

			var userId = ""
			profileViewModel.getUserId().observe(this) { userIdd ->

				if (userIdd != null) {
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			profileViewModel.getApiToken().observe(this) { apiTokenn ->

				if (apiTokenn != null) {
					apiToken = apiTokenn.toString()
				}
			}

			profileViewModel.getMahasiswaProfile(ProfileRemoteRequestBody(userId, apiToken))

			profileViewModel.getProfileResult.observe(this) { getProfileResult ->

				when (getProfileResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", getProfileResult.payload?.status.toString())
						Log.d("Result message", getProfileResult.payload?.message.toString())
						Log.d("Exception", getProfileResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${getProfileResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", getProfileResult.payload?.status.toString())
						Log.d("Result message", getProfileResult.payload?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${getProfileResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()
					}

					else -> {}
				}
			}
		}

		binding.btnTestUpdateProfile.setOnClickListener {

			var userId = ""
			profileViewModel.getUserId().observe(this) { userIdd ->

				if (userIdd != null) {
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			profileViewModel.getApiToken().observe(this) { apiTokenn ->

				if (apiTokenn != null) {
					apiToken = apiTokenn.toString()
				}
			}

			profileViewModel.updateMahasiswaProfile(
				UpdateProfileRemoteRequestBody(
					userId = userId,
					apiToken = apiToken,
					userName = "Mahasiswa Client",
					noTelp = "0831111111",
					userEmail = "userclient@gmail.com",
					alamat = "Alamat client",
					angkatan = "2020",
					ipk = "3.1",
					sks = "140",
					jenisKelamin = null,
					userImage = null
				)
			)

			profileViewModel.updateProfileResult.observe(this) { updateProfileResult ->

				when (updateProfileResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", updateProfileResult.payload?.status.toString())
						Log.d("Result message", updateProfileResult.payload?.message.toString())
						Log.d("Exception", updateProfileResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${updateProfileResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", updateProfileResult.payload?.status.toString())
						Log.d("Result message", updateProfileResult.payload?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${updateProfileResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()
					}

					else -> {}
				}
			}
		}

		binding.btnTestUpdatePassword.setOnClickListener {
			var userId = ""
			profileViewModel.getUserId().observe(this) { userIdd ->

				if (userIdd != null) {
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			profileViewModel.getApiToken().observe(this) { apiTokenn ->

				if (apiTokenn != null) {
					apiToken = apiTokenn.toString()
				}
			}

			profileViewModel.updatePasswordProfile(
				UpdatePasswordRemoteRequestBody(
					userId = userId,
					apiToken = apiToken,
					currentPassword = "mahasiswa1234",
					newPassword = "mahasiswa123",
					repeatNewPassword = "mahasiswa123"
				)
			)

			profileViewModel.updatePasswordResult.observe(this) { updatePasswordResult ->

				when (updatePasswordResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", updatePasswordResult.payload?.status.toString())
						Log.d("Result message", updatePasswordResult.payload?.message.toString())
						Log.d("Exception", updatePasswordResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${updatePasswordResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", updatePasswordResult.payload?.status.toString())
						Log.d("Result message", updatePasswordResult.payload?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${updatePasswordResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()
					}

					else -> {}
				}
			}
		}

		binding.btnTestGetKelompok.setOnClickListener {
			setLoading(true)

			var userId = ""
			kelompokSayaViewModel.getUserId().observe(this) { userIdd ->

				if (userIdd != null) {
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			kelompokSayaViewModel.getApiToken().observe(this) { apiTokenn ->

				if (apiTokenn != null) {
					apiToken = apiTokenn.toString()
				}
			}

			kelompokSayaViewModel.getKelompokSaya(
				KelompokSayaRemoteRequestBody(
					userId = userId,
					apiToken = apiToken
				)
			)

			kelompokSayaViewModel.getKelompokSayaResult.observe(this) { kelompokSayaResult ->

				when (kelompokSayaResult){
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", kelompokSayaResult.payload?.status.toString())
						Log.d("Result message", kelompokSayaResult.payload?.message.toString())
						Log.d("Exception", kelompokSayaResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${kelompokSayaResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", kelompokSayaResult.payload?.status.toString())
						Log.d("Result message", kelompokSayaResult.payload?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${kelompokSayaResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

						val kelompok = kelompokSayaResult.payload?.data?.kelompok

						binding.tvHasilKelompok.text = when {
							kelompok == null -> "Hasil: Belum daftar kelompok"
							kelompok.idKelompok != null -> "Hasil: Kelompok sudah ada. Id = ${kelompok.idKelompok}"
							else -> "Hasil: Belum dikelompokkan"
						}
					}

					else -> {}
				}
			}
		}

		binding.btnTestAddKelompokIndividu.setOnClickListener {
			setLoading(true)

			var userId = ""
			kelompokSayaViewModel.getUserId().observe(this) { userIdd ->

				if (userIdd != null) {
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			kelompokSayaViewModel.getApiToken().observe(this) { apiTokenn ->

				if (apiTokenn != null) {
					apiToken = apiTokenn.toString()
				}
			}

			kelompokSayaViewModel.addKelompokIndividu(
				AddKelompokIndividuRemoteRequestBody(
					userId = userId,
					apiToken = apiToken,
					idSiklus = "7",
					email = "example@email.com",
					angkatan = "2022",
					jenisKelamin = "Laki-laki",
					ipk = "3.5",
					sks = "120",
					noTelp = "1234567890",
					alamat = "123 Main Street",
					s = "1",
					e = "2",
					c = "3",
					m = "4"
				)
			)

			kelompokSayaViewModel.addKelompokIndividuResult.observe(this) { addKelompokIndividuResult ->

				when (addKelompokIndividuResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", addKelompokIndividuResult.payload?.status.toString())
						Log.d("Result message", addKelompokIndividuResult.payload?.message.toString())
						Log.d("Exception", addKelompokIndividuResult.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${addKelompokIndividuResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", addKelompokIndividuResult.payload?.status.toString())
						Log.d("Result message", addKelompokIndividuResult.payload?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${addKelompokIndividuResult.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()
					}

					else -> {}
				}
			}

		}

		binding.btnTestAddKelompokPunyaKelompok.setOnClickListener {
			setLoading(true)

			var userId = ""
			kelompokSayaViewModel.getUserId().observe(this) { userIdd ->

				if (userIdd != null) {
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			kelompokSayaViewModel.getApiToken().observe(this) { apiTokenn ->

				if (apiTokenn != null) {
					apiToken = apiTokenn.toString()
				}
			}

			kelompokSayaViewModel.addKelompokPunyaKelompok(
				AddKelompokPunyaKelompokRemoteRequestBody(
					userId = userId,
					apiToken = apiToken,
					idSiklus = "7",
					email = "example@email.com",
					angkatan = "2022",
					judulCapstone = "My Capstone Project",
					idTopik = "1",
					dosbingSatu = "16862144396919",
					dosbingDua = "16862150681435",
					alamat = "123 Main Street",

					angkatanSatu = "2022",
					emailSatu = "member1@email.com",
					jenisKelaminSatu = "Laki-laki",
					ipkSatu = "3.5",
					sksSatu = "120",
					noTelpSatu = "1234567890",
					alamatSatu = "456 Second Street",

					angkatanDua = "2022",
					emailDua = "member2@email.com",
					jenisKelaminDua = "Perempuan",
					ipkDua = "3.8",
					sksDua = "130",
					noTelpDua = "9876543210",
					alamatDua = "789 Third Street",

					angkatanTiga = "2022",
					emailTiga = "member3@email.com",
					jenisKelaminTiga = "Perempuan",
					ipkTiga = "3.2",
					sksTiga = "110",
					noTelpTiga = "5551234567",
					alamatTiga = "101 Fourth Street",

					namaSatu = "17025616857273",
					namaDua = "17025617220255",
					namaTiga = "17025617299185"
				)
			)

			kelompokSayaViewModel.addKelompokPunyaKelompok.observe(this) { addKelompokPunyaKelompok ->

				when (addKelompokPunyaKelompok) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", addKelompokPunyaKelompok.payload?.status.toString())
						Log.d("Result message", addKelompokPunyaKelompok.payload?.message.toString())
						Log.d("Exception", addKelompokPunyaKelompok.exception?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${addKelompokPunyaKelompok.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", addKelompokPunyaKelompok.payload?.status.toString())
						Log.d("Result message", addKelompokPunyaKelompok.payload?.message.toString())
						Toast.makeText(
							this@TestActivity,
							"Result: ${addKelompokPunyaKelompok.payload?.message.toString()}",
							Toast.LENGTH_SHORT
						).show()
					}

					else -> {}
				}
			}

		}

		binding.btnTestUpload.setOnClickListener {
			val intent = Intent(this@TestActivity, TestUploadActivity::class.java)

			startActivity(intent)
		}
	}

	private fun setLoading(isLoading: Boolean) {
		binding.pbTes.isVisible = isLoading
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}