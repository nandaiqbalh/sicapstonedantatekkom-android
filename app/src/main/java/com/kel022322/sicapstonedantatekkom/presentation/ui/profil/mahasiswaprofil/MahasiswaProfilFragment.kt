package com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaProfilBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login.LoginActivity
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel.ProfileIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel.ProfilePasswordViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel.ProfileUpdateViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.EditTextHelper.Companion.setTextOrHint
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MahasiswaProfilFragment : Fragment() {

	private var _binding: FragmentMahasiswaProfilBinding? = null
	private val binding get() = _binding!!

	private val profileIndexViewModel: ProfileIndexViewModel by viewModels()
	private val profileUpdateViewModel: ProfileUpdateViewModel by viewModels()
	private val profilePasswordViewModel: ProfilePasswordViewModel by viewModels()
	private val userViewModel: UserViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	private val REQUEST_CODE_PERMISSION = 3
	private val MAX_FILE_SIZE = 3 * 1024 * 1024 // 1MB

	private val galleryResult =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
			if (result.resultCode == Activity.RESULT_OK) {
				handleGaleriImage(result.data)
			}
		}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaProfilBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// get profile
		getProfile()

		// button listener
		doButtonListener()

	}

	private fun doButtonListener() {

		// simpan profil button
		binding.btnSimpanProfil.setOnClickListener {
			simpanProfil()
		}

		// ubah password button
		binding.btnUbahPassword.setOnClickListener {
			ubahPassword()
		}

		// logout button
		binding.btnLogout.setOnClickListener {

			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin untuk keluar?"
			) {
				setLoading(true)

				userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						userViewModel.authLogout(apiToken)
						Log.d("API TOKEN", apiToken)
					}
				}

				userViewModel.logoutResult.observe(viewLifecycleOwner) { logoutResult ->
					when (logoutResult) {
						is Resource.Loading -> setLoading(true)
						is Resource.Error -> {
							setLoading(false)
							Log.d("Logout error", logoutResult.payload?.status.toString())
							showSnackbar("Gagal keluar!")
						}

						is Resource.Success -> {
							setLoading(false)

							val status = logoutResult.payload?.status

							val loginResult = logoutResult.payload

							if (loginResult?.success == true) {
								Log.d("Logout success", status.toString())

								showSnackbar(logoutResult.payload.status ?: "Berhasil keluar!")

								actionIfLogoutSucces()

							} else {
								// if the success is false, then just show the snackbar
								if (status == "Token is Expired" || status == "Token is Invalid") {
									showSnackbar("Sesi anda telah berakhir :(")

									actionIfLogoutSucces()
								} else {
									showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

								}
							}
						}

						else -> {}

					}
				}
			}
		}

		// update photo profile button
		binding.btnEditPhotoProfile.setOnClickListener {
			checkGalleryPermission()
		}
	}

	private fun getProfile() {
		setLoading(true)

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				profileIndexViewModel.getMahasiswaProfile(apiToken)
			}
		}

		profileIndexViewModel.getProfileResult.observe(viewLifecycleOwner) { getProfileResult ->

			val resultResponse = getProfileResult.payload
			val status = resultResponse?.status

			when (getProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					Log.d("Error Profile Index", getProfileResult.payload?.status.toString())

					setLoading(false)

					with(binding){
						cvErrorProfil.visibility = View.VISIBLE
						tvErrorProfil.text = status ?: "Mohon periksa kembali koneksi internet Anda!"

						lineaerLayoutProfil.visibility = View.GONE
					}

					setProfileImageAndNamAndNim()

				}

				is Resource.Success -> {
					setLoading(false)

					if (resultResponse?.success == true && resultResponse.data != null) {
						Log.d("Succes status", status.toString())

						// set binding
						with(binding) {

							tvNamaUser.text = resultResponse.data.userName
							tvNimUser.text = resultResponse.data.nomorInduk

							// form
							edtNamaLengkapPengguna.setTextOrHint(
								resultResponse.data.userName, R.string.tv_hint_nama_lengkap
							)
							edtNimPengguna.setTextOrHint(
								resultResponse.data.nomorInduk,
								R.string.tv_hint_nim
							)
							edtEmailPengguna.setTextOrHint(
								resultResponse.data.userEmail, R.string.tv_hint_email
							)
							edtNoTelpPengguna.setTextOrHint(
								resultResponse.data.noTelp, R.string.tv_hint_no_telp
							)

							edtJenisKelaminIndividu.setTextOrHint(
								resultResponse.data.jenisKelamin, R.string.tv_hint_jenis_kelamin
							)

							GlideApp.with(this@MahasiswaProfilFragment).asBitmap()
								.load(resultResponse.data.userImgUrl).into(ivProfilephoto)

							userViewModel.setPhotoProfile(resultResponse.data.userImgUrl.toString())
							userViewModel.setUsername(resultResponse.data.userName.toString())

						}
					} else {
						setProfileImageAndNamAndNim()

						if (status == "Token is Expired" || status == "Token is Invalid") {
							with(binding){
								cvErrorProfil.visibility = View.VISIBLE
								tvErrorProfil.text = status

								lineaerLayoutProfil.visibility = View.GONE
							}

							actionIfLogoutSucces()
						} else {
							with(binding){
								cvErrorProfil.visibility = View.VISIBLE
								tvErrorProfil.text = status ?: "Mohon periksa kembali koneksi internet Anda!"

								lineaerLayoutProfil.visibility = View.GONE
							}
						}

					}
				}

				else -> {}
			}
		}

	}

	private fun setProfileImageAndNamAndNim() {
		setLoading(true)

		// set username
		userViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			setLoading(false)

			if (username != null && username != "") {
				binding.tvNamaUser.text = username
			}
		}

		// set photo profile
		userViewModel.getPhotoProfile().observe(viewLifecycleOwner) { photoProfile ->
			setLoading(false)

			if (photoProfile != null && photoProfile != "") {
				GlideApp.with(this@MahasiswaProfilFragment).asBitmap().load(photoProfile)
					.into(binding.ivProfilephoto)
			}
		}

		userViewModel.getNIM().observe(viewLifecycleOwner) { nomorInduk ->
			setLoading(false)

			if (nomorInduk != null && nomorInduk != "") {
				binding.tvNimUser.text = nomorInduk
			}
		}

	}

	private fun simpanProfil() {
		if (isFormProfilValid()) {

			showCustomAlertDialog(
				title = "Konfirmasi",
				message = "Apakah anda yakin untuk mengubah profil anda?"
			) {
				setLoading(true)

				// Assuming doNetworkingUpdateProfile() is a function that performs the update profile operation
				doNetworkingUpdateProfile()
			}

		}
	}

	private fun ubahPassword() {
		if (isFormPasswordValid()) {
			showCustomAlertDialog(
				title = "Konfirmasi",
				message = "Apakah anda yakin untuk mengubah password anda?"
			) {
				setLoading(true)

				doNetworkingUbahPassword()

			}
		}
	}

	private fun doNetworkingUpdateProfile() {

		val namaPenggunaEntered = binding.edtNamaLengkapPengguna.text.toString().trim()
		val emailPenggunaEntered = binding.edtEmailPengguna.text.toString().trim()
		val noTelpPenggunaEntered = binding.edtNoTelpPengguna.text.toString().trim()
		val jenisKelaminPenggunaEntered = binding.edtJenisKelaminIndividu.text.toString().trim()

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				profileUpdateViewModel.updateMahasiswaProfile(
					apiToken,
					UpdateProfileRemoteRequestBody(
						userName = namaPenggunaEntered,
						userEmail = emailPenggunaEntered,
						noTelp = noTelpPenggunaEntered,
						jenisKelamin = jenisKelaminPenggunaEntered
					)
				)
			}
		}
		profileUpdateViewModel.updateProfileResult.observe(viewLifecycleOwner) { updateProfileResult ->
			val resultResponse = updateProfileResult.payload

			when (updateProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					Log.d("Update Error Profile", updateProfileResult.payload?.status.toString())

					setLoading(false)

					val status = resultResponse?.status
					showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

				}

				is Resource.Success -> {
					setLoading(false)

					val status = updateProfileResult.payload?.status

					if (resultResponse?.success == true && resultResponse.data != null) {
						Log.d("Update Succes status", status.toString())

						showSnackbar(status ?: "Berhasil memperbaharui profil!")
						// set binding

						findNavController().navigate(R.id.action_mahasiswaProfilFragment_to_mahasiswaBerandaFragment)

					} else {
						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						} else {
							showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

						}
					}
				}

				else -> {}
			}
		}
	}

	// validate form simpanProfil
	private fun isFormProfilValid(): Boolean {
		val namaPenggunaEntered = binding.edtNamaLengkapPengguna.text.toString()
		val emailPenggunaEntered = binding.edtEmailPengguna.text.toString()
		val noTelpPenggunaEntered = binding.edtNoTelpPengguna.text.toString()
		val jenisKelaminPenggunaEntered = binding.edtJenisKelaminIndividu.text.toString()

		var isFormValid = true

		// Validate name
		if (namaPenggunaEntered.isEmpty()) {
			isFormValid = false
			binding.tilNamaPengguna.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilNamaPengguna.error = null
		}

		// Validate name
		if (jenisKelaminPenggunaEntered.isEmpty()) {
			isFormValid = false
			binding.tilJenisKelaminIndividu.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilJenisKelaminIndividu.error = null
		}

		// Validate email
		if (emailPenggunaEntered.isEmpty()) {
			isFormValid = false
			binding.tilEmailPengguna.error = getString(R.string.tv_error_input_blank)
		} else if (!isValidEmail(emailPenggunaEntered)) {
			binding.tilEmailPengguna.error = getString(R.string.tv_error_email_invalid)
			isFormValid = false
		} else {
			binding.tilEmailPengguna.error = null
		}

		// Validate phone number
		if (noTelpPenggunaEntered.isEmpty()) {
			isFormValid = false
			binding.tilNoTelpPengguna.error = getString(R.string.tv_error_input_blank)
		} else if (!isValidPhoneNumber(noTelpPenggunaEntered)) {
			binding.tilNoTelpPengguna.error = getString(R.string.tv_error_phone_invalid)
			isFormValid = false

		} else {
			binding.edtNoTelpPengguna.error = null
		}

		return isFormValid
	}

	private fun doNetworkingUbahPassword() {

		val currentPassword = binding.edtCurrentPassword.text.toString().trim()
		val newPassword = binding.edtNewPassword.text.toString().trim()
		val newPasswordKonfirmation = binding.edtKonfirmasiPasswordBaru.text.toString().trim()

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				profilePasswordViewModel.updatePasswordProfile(
					apiToken,
					UpdatePasswordRemoteRequestBody(
						currentPassword = currentPassword,
						newPassword = newPassword,
						repeatNewPassword = newPasswordKonfirmation
					)
				)
			}
		}

		profilePasswordViewModel.updatePasswordResult.observe(viewLifecycleOwner) { updateProfileResult ->
			val resultResponse = updateProfileResult.payload

			when (updateProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					Log.d("Password Error Password", updateProfileResult.payload?.status.toString())

					setLoading(false)

					val status = resultResponse?.status
					showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

				}

				is Resource.Success -> {
					setLoading(false)

					val status = updateProfileResult.payload?.status

					if (resultResponse?.success == true && resultResponse.data != null) {
						Log.d("Password Succes status", status.toString())
						showSnackbar(resultResponse.status ?: "Berhasil memperbaharui password!")

						actionIfLogoutSucces()

						val intent = Intent(requireContext(), LoginActivity::class.java)
						startActivity(intent)
						requireActivity().finishAffinity()


					} else {
						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						} else {
							showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

						}
					}

				}

				else -> {}
			}
		}

	}

	private fun isFormPasswordValid(): Boolean {
		val currentPassword = binding.edtCurrentPassword.text.toString().trim()
		val newPassword = binding.edtNewPassword.text.toString().trim()
		val newPasswordKonfirmation = binding.edtKonfirmasiPasswordBaru.text.toString().trim()

		var isFormValid = true

		// Validate current password
		if (currentPassword.isEmpty()) {
			isFormValid = false
			binding.tilCurrentPassword.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilCurrentPassword.error = null
		}

		// Validate new password
		if (newPassword.isEmpty()) {
			isFormValid = false
			binding.tilNewPassword.error = getString(R.string.tv_error_input_blank)
		} else if (newPassword.length < 8) {
			isFormValid = false
			binding.tilNewPassword.error = getString(R.string.tv_error_password_minimum_length)
		} else {
			binding.tilNewPassword.error = null
		}

		// Validate password confirmation
		if (newPasswordKonfirmation.isEmpty()) {
			isFormValid = false
			binding.tilKonfirmasiPasswordBaru.error = getString(R.string.tv_error_input_blank)
		} else if (newPassword != newPasswordKonfirmation) {
			isFormValid = false
			binding.tilKonfirmasiPasswordBaru.error = getString(R.string.tv_error_password_mismatch)
		} else {
			binding.tilKonfirmasiPasswordBaru.error = null
		}

		return isFormValid
	}

	// update photo profile

	private fun checkGalleryPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			// For Android 13 and higher
			if (isPermissionGranted(
					Manifest.permission.READ_MEDIA_IMAGES, arrayOf(
						Manifest.permission.READ_MEDIA_IMAGES,
						Manifest.permission.READ_MEDIA_VIDEO
					), REQUEST_CODE_PERMISSION
				)
			) {
				openGallery()
			}
		} else {
			// For Android 12 and lower
			if (isPermissionGranted(
					Manifest.permission.READ_EXTERNAL_STORAGE, arrayOf(
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE
					), REQUEST_CODE_PERMISSION
				)
			) {
				openGallery()
			}
		}
	}


	private fun openGallery() {
		val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		galleryResult.launch(galleryIntent)
	}

	private fun handleGaleriImage(intent: Intent?) {
		val selectedImageUri = intent?.data
		val photo =
			MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)

		saveSelectedImage(photo)

	}

	private fun saveSelectedImage(photo: Bitmap) {
		val fileSize = calculateFileSize(photo)

		if (fileSize <= MAX_FILE_SIZE) {
			val squarePhoto = cropToSquare(photo) // Crop the photo to a square

			val timeStamp: String =
				SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
			val imageFileName = "JPEG_${timeStamp}_"
			val storageDir: File? =
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
			val file = File.createTempFile(imageFileName, ".jpg", storageDir)

			FileOutputStream(file).use { output ->
				squarePhoto.compress(Bitmap.CompressFormat.JPEG, 100, output)
			}

			val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
			val photoPart = MultipartBody.Part.createFormData("user_img", file.name, requestBody)

			userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
				apiToken?.let {
					// Kirim token dan photoPart ke fungsi updatePhotoProfile
					profileUpdateViewModel.updatePhotoProfile(apiToken, photoPart)
				}
			}

			profileUpdateViewModel.updatePhotoProfileResult.observe(viewLifecycleOwner) { updatePhotoProfileResult ->
				val resultResponse = updatePhotoProfileResult.payload

				when (updatePhotoProfileResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						Log.d("Photo Error Profile", updatePhotoProfileResult.payload?.status.toString())

						setLoading(false)

						val status = resultResponse?.status
						showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

					}

					is Resource.Success -> {
						setLoading(false)

						val status = updatePhotoProfileResult.payload?.status

						if (resultResponse?.success == true) {
							Log.d("Photo Succes status", status.toString())

							showSnackbar(status ?: "Berhasil memperbaharui foto profil!")
							findNavController().navigate(R.id.action_mahasiswaProfilFragment_to_mahasiswaBerandaFragment)

						} else {

							if (status == "Token is Expired" || status == "Token is Invalid") {
								showSnackbar("Sesi anda telah berakhir :(")

								actionIfLogoutSucces()
							} else {
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

							}
						}
					}

					else -> {}
				}
			}

		} else {
			showSnackbar("Gagal! Ukuran foto melebihi 3MB!")
		}
	}


	private fun calculateFileSize(bitmap: Bitmap): Long {
		val stream = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
		val byteArray = stream.toByteArray()
		return byteArray.size.toLong()
	}

	private fun isPermissionGranted(
		permission: String,
		permissions: Array<String>,
		request: Int,
	): Boolean {
		val permissionCheck = ActivityCompat.checkSelfPermission(requireContext(), permission)
		return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(
					requireActivity(), permission
				)
			) {
				showPermissionDeniedDialog()
			} else {
				ActivityCompat.requestPermissions(requireActivity(), permissions, request)
			}
			false
		} else {
			true
		}
	}

	private fun showPermissionDeniedDialog() {
		AlertDialog.Builder(requireContext()).setTitle("Izin ditolak")
			.setMessage("Izin ditolak, silahkan izinkan melalui pengaturan aplikasi!")
			.setPositiveButton("App Settings") { _, _ ->
				val intent = Intent()
				intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
				val uri = Uri.fromParts("package", requireContext().packageName, null)
				intent.data = uri
				startActivity(intent)
			}.setNegativeButton("Batalkan") { dialog, _ -> dialog.cancel() }.show()
	}

	private fun cropToSquare(bitmap: Bitmap): Bitmap {
		val dimension = bitmap.width.coerceAtMost(bitmap.height)
		return Bitmap.createBitmap(bitmap, 0, 0, dimension, dimension)
	}

	private fun showCustomAlertDialog(
		title: String,
		message: String,
		positiveAction: () -> Unit,
	) {
		val builder = AlertDialog.Builder(requireContext()).create()
		val view = layoutInflater.inflate(R.layout.dialog_custom_alert_dialog, null)
		builder.setView(view)
		builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

		val buttonYes = view.findViewById<Button>(R.id.btn_alert_yes)
		val buttonNo = view.findViewById<Button>(R.id.btn_alert_no)
		val alertTitle = view.findViewById<TextView>(R.id.tv_alert_title)
		val alertMessage = view.findViewById<TextView>(R.id.tv_alert_message)

		alertTitle.text = title
		alertMessage.text = message

		buttonYes.setOnClickListener {
			positiveAction.invoke()
			builder.dismiss()
		}

		buttonNo.setOnClickListener {
			builder.dismiss()
		}

		builder.setCanceledOnTouchOutside(false)
		builder.show()
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaProfilFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), message, "OK"
			) {
				customSnackbar.dismissSnackbar()
			}
		}
	}

	private fun isValidEmail(email: String): Boolean {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
	}

	private fun isValidPhoneNumber(phoneNumber: String): Boolean {
		// For simplicity, let's assume a valid phone number is a 10 to 13-digit number
		return phoneNumber.length in 10..13 && phoneNumber.all { it.isDigit() }
	}

	// set loading and shimmer
	private fun setLoading(isLoading: Boolean) {
		if (isLoading) {
			binding.shimmerProfilMahasiswa.visibility = View.VISIBLE
			binding.shimmerProfilMahasiswa.startShimmer()

			binding.constraintProfilMahasiswa.visibility = View.GONE
		} else {
			binding.shimmerProfilMahasiswa.visibility = View.GONE
			binding.shimmerProfilMahasiswa.stopShimmer()

			binding.constraintProfilMahasiswa.visibility = View.VISIBLE
		}
	}

	private fun actionIfLogoutSucces() {
		// set auth data store
		userViewModel.setApiToken("")
		userViewModel.setUserId("")
		userViewModel.setUsername("")
		userViewModel.setStatusAuth(false)

		val intent = Intent(requireContext(), SplashscreenActivity::class.java)
		requireContext().startActivity(intent)
		requireActivity().finishAffinity()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}