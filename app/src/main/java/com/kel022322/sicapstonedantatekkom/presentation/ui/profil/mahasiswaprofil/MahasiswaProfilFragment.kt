package com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.request.PhotoProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaProfilBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
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

	private val profileViewModel: ProfileSayaViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	private val REQUEST_CODE_PERMISSION = 3
	private val MAX_FILE_SIZE = 3 * 1024 * 1024 // 1MB

	private val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			handleGaleriImage(result.data)
		}
	}

	private val cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			handleCameraImage(result.data)
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
			val alertDialogBuilder = AlertDialog.Builder(requireContext())
			alertDialogBuilder.setTitle("Konfirmasi")
			alertDialogBuilder.setMessage("Apakah anda yakin untuk keluar?")
			alertDialogBuilder.setPositiveButton("Ya") { dialog, _ ->
				setLoading(true)

				showSnackbar("Berhasil keluar!")

				dialog.dismiss()
			}
			alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
				dialog.dismiss()
			}
			val alertDialog = alertDialogBuilder.create()
			alertDialog.show()
		}

		// update photo profile button
		binding.btnEditPhotoProfile.setOnClickListener {
			showTakePhotoOptions()
		}
	}

	private fun getProfile() {
		setLoading(true)

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						profileViewModel.getMahasiswaProfile(
							ProfileRemoteRequestBody(
								userId.toString(), it
							)
						)
						profileViewModel.getPhotoProfile(
							PhotoProfileRemoteRequestBody(
								userId.toString(), it
							)
						)
					}
				}
			}
		}

		profileViewModel.getProfileResult.observe(viewLifecycleOwner) { getProfileResult ->

			when (getProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = getProfileResult.payload?.message
					showSnackbar(message ?: "Terjadi kesalahan!")
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getProfileResult.payload?.message
					Log.d("Result message", message.toString())

					if (getProfileResult.payload?.data != null) {
						val dataUser = getProfileResult.payload.data

						Log.d("DATA USER", dataUser.toString())
						// set binding
						with(binding) {

							tvNamaUser.text = dataUser.userName
							tvNimUser.text = dataUser.nomorInduk

							// form
							edtNamaLengkapPengguna.setTextOrHint(
								dataUser.userName, R.string.tv_hint_nama_lengkap
							)
							edtNimPengguna.setTextOrHint(dataUser.nomorInduk, R.string.tv_hint_nim)
							edtEmailPengguna.setTextOrHint(
								dataUser.userEmail, R.string.tv_hint_email
							)
							edtNoTelpPengguna.setTextOrHint(
								dataUser.noTelp, R.string.tv_hint_no_telp
							)

						}
					} else {
						showSnackbar(message ?: "Terjadi kesalahan!")
					}
				}

				else -> {}
			}
		}

		profileViewModel.getPhotoProfileResult.observe(viewLifecycleOwner) { getPhotoProfileResult ->
			when (getPhotoProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = getPhotoProfileResult.payload?.message

					showSnackbar(message ?: "Terjadi kesalahan!")
				}

				is Resource.Success -> {
					setLoading(false)
					val message = getPhotoProfileResult.payload?.message

					Log.d("Success message", message.toString())

					if (getPhotoProfileResult.payload?.data != null) {
						// set binding
						with(binding) {
							val base64Image = getPhotoProfileResult.payload.data.toString()

							profileViewModel.setPhotoProfile(base64Image)

							if (base64Image != "null") {
								// Decode base64 string to byte array
								val decodedBytes = decodeBase64ToBitmap(base64Image)

								GlideApp.with(requireContext()).asBitmap().load(decodedBytes)
									.into(ivProfilephoto)
							}

						}
					}
				}

				else -> {}
			}
		}
	}

	private fun simpanProfil() {

		if (validateFormSimpanProfil()) {
			val alertDialogBuilder = AlertDialog.Builder(requireContext())
			alertDialogBuilder.setTitle("Konfirmasi")
			alertDialogBuilder.setMessage("Apakah anda yakin untuk mengubah profil anda?")
			alertDialogBuilder.setPositiveButton("Ya") { dialog, _ ->
				setLoading(true)

				doNetworkingUpdateProfile()

				dialog.dismiss()
			}
			alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
				dialog.dismiss()
			}
			val alertDialog = alertDialogBuilder.create()
			alertDialog.show()
		}
	}

	private fun ubahPassword() {
		if (validateFormUbahPassword()) {
			val alertDialogBuilder = AlertDialog.Builder(requireContext())
			alertDialogBuilder.setTitle("Konfirmasi")
			alertDialogBuilder.setMessage("Apakah anda yakin untuk mengubah password anda?")
			alertDialogBuilder.setPositiveButton("Ya") { dialog, _ ->
				setLoading(true)

				doNetworkingUbahPassword()

				dialog.dismiss()
			}
			alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
				dialog.dismiss()
			}
			val alertDialog = alertDialogBuilder.create()
			alertDialog.show()
		}
	}

	// bind input menjadi dalam bentuk request body
	private fun doNetworkingUpdateProfile() {

		val namaPenggunaEntered = binding.edtNamaLengkapPengguna.text.toString().trim()
		val emailPenggunaEntered = binding.edtEmailPengguna.text.toString().trim()
		val noTelpPenggunaEntered = binding.edtNoTelpPengguna.text.toString().trim()

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						profileViewModel.updateMahasiswaProfile(
							UpdateProfileRemoteRequestBody(
								userName = namaPenggunaEntered,
								userEmail = emailPenggunaEntered,
								noTelp = noTelpPenggunaEntered,
								sks = null,
								ipk = null,
								userId = userId,
								userImage = null,
								apiToken = it,
								jenisKelamin = null,
								angkatan = null,
								alamat = null,

								)
						)
					}
				}
			}
		}

		profileViewModel.updateProfileResult.observe(viewLifecycleOwner){updateProfileResult ->
			when (updateProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = updateProfileResult.payload?.message
					showSnackbar(message ?: "Terjadi kesalahan!")

				}

				is Resource.Success -> {
					setLoading(false)

					val message = updateProfileResult.payload?.message
					Log.d("Result message", message.toString())

					showSnackbar(message ?: "Berhasil!")

					if (updateProfileResult.payload?.data != null) {
						val dataUser = updateProfileResult.payload.data
						// set binding
						with(binding) {

							tvNamaUser.text = dataUser.userName
							tvNimUser.text = dataUser.nomorInduk

							// form
							edtNamaLengkapPengguna.setTextOrHint(
								dataUser.userName, R.string.tv_hint_nama_lengkap
							)
							edtNimPengguna.setTextOrHint(dataUser.nomorInduk, R.string.tv_hint_nim)
							edtEmailPengguna.setTextOrHint(
								dataUser.userEmail, R.string.tv_hint_email
							)
							edtNoTelpPengguna.setTextOrHint(
								dataUser.noTelp, R.string.tv_hint_no_telp
							)

							profileViewModel.setUsername(dataUser.userName.toString())
						}
					}
				}

				else -> {}
			}
		}

	}

	// validate form simpanProfil
	private fun validateFormSimpanProfil(): Boolean {
		val namaPenggunaEntered = binding.edtNamaLengkapPengguna.text.toString()
		val emailPenggunaEntered = binding.edtEmailPengguna.text.toString()
		val noTelpPenggunaEntered = binding.edtNoTelpPengguna.text.toString()

		var isFormValid = true

		// Validate name
		if (namaPenggunaEntered.isEmpty()) {
			isFormValid = false
			binding.tilNamaPengguna.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilNamaPengguna.error = null
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

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						profileViewModel.updatePasswordProfile(
							UpdatePasswordRemoteRequestBody(
								userId = userId,
								apiToken = it,
								currentPassword = currentPassword,
								newPassword = newPassword,
								repeatNewPassword = newPasswordKonfirmation
								)
						)
					}
				}
			}
		}

		profileViewModel.updatePasswordResult.observe(viewLifecycleOwner){updateProfileResult ->
			when (updateProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = updateProfileResult.payload?.message
					showSnackbar(message ?: "Terjadi kesalahan!")

				}

				is Resource.Success -> {
					setLoading(false)

					val message = updateProfileResult.payload?.message
					Log.d("Result message", message.toString())

					if (message =="Password baru berhasil disimpan."){
						showSnackbar("Password berhasil diubah, silahkan masuk kembali.")

					}

				}

				else -> {}
			}
		}

	}
	private fun validateFormUbahPassword(): Boolean {
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
	private fun showTakePhotoOptions() {
		val options = arrayOf("Buka kamera", "Pilih dari galeri")
		val builder = AlertDialog.Builder(requireContext())
		builder.setTitle("Pilih opsi")
		builder.setItems(options) { _, which ->
			when (which) {
				0 -> checkCameraPermission()
				1 -> checkGalleryPermission()
			}
		}
		builder.show()
	}

	private fun checkCameraPermission() {
		if (isPermissionGranted(
				Manifest.permission.CAMERA,
				arrayOf(
					Manifest.permission.CAMERA,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE
				),
				REQUEST_CODE_PERMISSION
			)
		) {
			openCamera()
		}
	}

	private fun openCamera() {
		val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		cameraResult.launch(cameraIntent)
	}

	private fun handleCameraImage(intent: Intent?) {
		val photo = intent?.extras?.get("data") as Bitmap

		saveSelectedImage(photo)
	}

	private fun checkGalleryPermission() {
		if (isPermissionGranted(
				Manifest.permission.READ_EXTERNAL_STORAGE,
				arrayOf(
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE
				),
				REQUEST_CODE_PERMISSION
			)
		) {
			openGallery()
		}
	}

	private fun openGallery() {
		val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		galleryResult.launch(galleryIntent)
	}

	private fun handleGaleriImage(intent: Intent?) {
		val selectedImageUri = intent?.data
		val photo = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)

		saveSelectedImage(photo)

	}

	private fun saveSelectedImage(photo: Bitmap){
		val fileSize = calculateFileSize(photo)

		if (fileSize <= MAX_FILE_SIZE) {
			val squarePhoto = cropToSquare(photo) // Crop the photo to a square

			val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
			val imageFileName = "JPEG_${timeStamp}_"
			val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
			val file = File.createTempFile(imageFileName, ".jpg", storageDir)

			FileOutputStream(file).use { output ->
				squarePhoto.compress(Bitmap.CompressFormat.JPEG, 100, output)
			}

			val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
			val photoPart = MultipartBody.Part.createFormData("user_img", file.name, requestBody)

			profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
				userId?.let {
					profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
						apiToken?.let {
							// Kirim token dan photoPart ke fungsi updatePhotoProfile
							profileViewModel.updatePhotoProfile(userId, it, photoPart)
						}
					}
				}
			}

			profileViewModel.updatePhotoProfileResult.observe(viewLifecycleOwner) { updatePhotoProfileResult ->
				when (updatePhotoProfileResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						val message = updatePhotoProfileResult.payload?.message
						showSnackbar(message ?: "Terjadi kesalahan!")
					}

					is Resource.Success -> {
						setLoading(false)
						val message = updatePhotoProfileResult.payload?.message
						Log.d("Result Upload", message.toString())

						if (updatePhotoProfileResult.payload?.data != null) {

							restartFragment()

							showSnackbar(message ?: "Berhasil!")
						} else {
							showSnackbar(message ?: "Terjadi kesalahan!")
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
		request: Int
	): Boolean {
		val permissionCheck = ActivityCompat.checkSelfPermission(requireContext(), permission)
		return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
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
				val uri = Uri.fromParts("package",requireContext().packageName, null)
				intent.data = uri
				startActivity(intent)
			}.setNegativeButton("Batalkan") { dialog, _ -> dialog.cancel() }.show()
	}

	private fun cropToSquare(bitmap: Bitmap): Bitmap {
		val dimension = bitmap.width.coerceAtMost(bitmap.height)
		return Bitmap.createBitmap(bitmap, 0, 0, dimension, dimension)
	}

	private fun decodeBase64ToBitmap(base64: String): Bitmap {
		val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
	}

	private fun showSnackbar(message: String) {

		val currentFragment = this@MahasiswaProfilFragment
		if (currentFragment.isVisible){
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content),
				message,
				"OK"
			) {
				customSnackbar.dismissSnackbar()
				if (message == "Berhasil keluar!" || message == "Gagal! Anda telah masuk melalui perangkat lain." || message == "Pengguna tidak ditemukan!" || message == "Akses tidak sah!" || message == "Sesi anda telah berakhir, silahkan masuk terlebih dahulu.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				} else if (message == "null" || message.equals(null) || message == "Terjadi kesalahan!") {
					restartFragment()
				} else if (message == "Password berhasil diubah, silahkan masuk kembali."){

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				}
			}
		}

	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaProfilFragment

		// Check if the fragment is currently visible
		if (currentFragment.isVisible) {
			// Detach fragment
			val ftDetach = parentFragmentManager.beginTransaction()
			ftDetach.detach(currentFragment)
			ftDetach.commit()

			// Attach fragment
			val ftAttach = parentFragmentManager.beginTransaction()
			ftAttach.attach(currentFragment)
			ftAttach.commit()
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}