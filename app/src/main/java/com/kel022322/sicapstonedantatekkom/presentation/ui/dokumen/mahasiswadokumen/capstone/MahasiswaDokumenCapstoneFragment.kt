package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.capstone

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenCapstoneBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.DokumenViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.KelompokIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MahasiswaDokumenCapstoneFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenCapstoneBinding? = null
	private val binding get() = _binding!!

	private val customSnackbar = CustomSnackbar()

	private val dokumenViewModel: DokumenViewModel by viewModels()
	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()

	private var id: String? = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentMahasiswaDokumenCapstoneBinding.inflate(layoutInflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		checkKelompok()

		checkDokumen()

		btnListener()

		checkAndRequestStoragePermissions()

	}

	private fun checkKelompok() {
		setLoadingKelompok(true)

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.getKelompokSaya(apiToken)
			}
		}

		kelompokViewModel.getKelompokSayaResult.observe(viewLifecycleOwner) { getKelompokSayaResult ->
			val resultResponse = getKelompokSayaResult.payload

			when (getKelompokSayaResult) {
				is Resource.Loading -> {
					setLoadingKelompok(true)
				}

				is Resource.Error -> {
					setLoadingKelompok(false)
					Log.d("Result error", getKelompokSayaResult.toString())

					binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
					binding.tvBelumMemilikiKelompok.text = getString(R.string.tv_terjadi_kesalahan)

					binding.linearLayoutDokumenCapstone.visibility = View.GONE
				}

				is Resource.Success -> {
					setLoadingKelompok(false)

					Log.d("Result success", resultResponse.toString())

					binding.cvBelumMemilikiKelompok.visibility = View.GONE

					binding.linearLayoutDokumenCapstone.visibility = View.VISIBLE

					val dataKelompok = getKelompokSayaResult.payload?.data
					val status = getKelompokSayaResult.payload?.status

					if (getKelompokSayaResult.payload?.success == true) {
						if (dataKelompok?.kelompok != null) {

							if (dataKelompok.kelompok.nomorKelompok != null) {
								binding.cvBelumMemilikiKelompok.visibility = View.GONE

								binding.linearLayoutDokumenCapstone.visibility = View.VISIBLE
							} else {
								binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
								binding.tvBelumMemilikiKelompok.setText(R.string.kelompok_belum_valid)

								binding.linearLayoutDokumenCapstone.visibility = View.GONE
							}

						} else {
							binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
							binding.tvBelumMemilikiKelompok.setText(R.string.belum_memiliki_kelompok)

							binding.linearLayoutDokumenCapstone.visibility = View.GONE
						}
					} else {
						Log.d("Update Succes status, but failed", status.toString())

						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(", true)

							actionIfLogoutSucces()
						} else {
							showSnackbar(status ?: "Terjadi kesalahan!", true)

						}
					}

				}

				else -> {}
			}
		}
	}

	private fun checkDokumen() {

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				dokumenViewModel.getFileIndex(apiToken)
			}
		}


		dokumenViewModel.getFileIndexResult.observe(viewLifecycleOwner) { getFileIndexResult ->

			val resultResponse = getFileIndexResult.payload
			val status = resultResponse?.status
			when (getFileIndexResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					showSnackbar(status ?: "Terjadi kesalahan saat mengakses dokumen :(", false)

				}

				is Resource.Success -> {
					setLoading(false)

					id = getFileIndexResult.payload?.data?.fileMhs?.id.toString()

					if (resultResponse?.success == true) {
						checkFile(getFileIndexResult)

						viewDokumen(getFileIndexResult)
					} else {
						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(", true)

							actionIfLogoutSucces()
						} else {
							showSnackbar(status ?: "Terjadi kesalahan!", true)

						}
					}


				}

				else -> {}
			}
		}

	}

	private fun viewDokumen(getFileIndexResult: Resource<FileIndexRemoteResponse>) {

		val resultResponse = getFileIndexResult.payload

		with(binding) {

			// c100
			btnUnduhC100.setOnClickListener {

				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk mengunduh dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC100?.takeIf { it.isNotBlank() }
						?.let {
							val url =
								if (!it.startsWith("http://") && !it.startsWith("https://")) {
									URLUtil.guessUrl(it)
								} else {
									it
								}

							// Buat Intent untuk membuka tautan di browser
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
							// Gunakan context dari fragment untuk memulai aktivitas
							requireActivity().startActivity(intent)
						}
				}

			}

			// c200
			btnUnduhC200.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk mengunduh dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC200?.takeIf { it.isNotBlank() }
						?.let {
							val url =
								if (!it.startsWith("http://") && !it.startsWith("https://")) {
									URLUtil.guessUrl(it)
								} else {
									it
								}

							// Buat Intent untuk membuka tautan di browser
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
							// Gunakan context dari fragment untuk memulai aktivitas
							requireActivity().startActivity(intent)
						}
				}
			}

			// c300
			btnUnduhC300.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk mengunduh dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC300?.takeIf { it.isNotBlank() }
						?.let {
							val url =
								if (!it.startsWith("http://") && !it.startsWith("https://")) {
									URLUtil.guessUrl(it)
								} else {
									it
								}

							// Buat Intent untuk membuka tautan di browser
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
							// Gunakan context dari fragment untuk memulai aktivitas
							requireActivity().startActivity(intent)
						}
				}
			}

			// c400
			btnUnduhC400.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk mengunduh dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC400?.takeIf { it.isNotBlank() }
						?.let {
							val url =
								if (!it.startsWith("http://") && !it.startsWith("https://")) {
									URLUtil.guessUrl(it)
								} else {
									it
								}

							// Buat Intent untuk membuka tautan di browser
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
							// Gunakan context dari fragment untuk memulai aktivitas
							requireActivity().startActivity(intent)
						}
				}
			}

			// c500
			btnUnduhC500.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk mengunduh dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC500?.takeIf { it.isNotBlank() }
						?.let {
							val url =
								if (!it.startsWith("http://") && !it.startsWith("https://")) {
									URLUtil.guessUrl(it)
								} else {
									it
								}

							// Buat Intent untuk membuka tautan di browser
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
							// Gunakan context dari fragment untuk memulai aktivitas
							requireActivity().startActivity(intent)
						}
				}
			}
		}
	}

	private fun btnListener() {

		binding.tvPilihDokumenC100.setOnClickListener {

			uploadFileC100Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC200.setOnClickListener {

			uploadFileC200Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC300.setOnClickListener {

			uploadFileC300Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC400.setOnClickListener {

			uploadFileC400Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC500.setOnClickListener {

			uploadFileC500Launcher.launch("application/pdf")

		}

	}

	private val uploadFileC100Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc100.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c100Part = MultipartBody.Part.createFormData("c100", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {
								dokumenViewModel.uploadC100Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c100 = c100Part
								)
							}
						}


					dokumenViewModel.uploadC100ProcessResult.observe(viewLifecycleOwner) { uploadC100ProcessResult ->
						when (uploadC100ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC100ProcessResult.payload?.status
								Log.d("HASIL UPLOAD ERROR", status.toString())
								showSnackbar(status ?: "Terjadi kesalahan!", false)
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC100ProcessResult.payload?.status

								if (uploadC100ProcessResult.payload?.success == true && uploadC100ProcessResult.payload.data != null) {
									showSnackbar(status ?: "Berhasil!", true)
									checkDokumen()

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
										showSnackbar("Sesi anda telah berakhir :(", true)

										actionIfLogoutSucces()
									} else {
										showSnackbar(status ?: "Terjadi kesalahan!", true)

									}
								}
							}

							else -> {}
						}
					}

				} catch (e: Exception) {
					e.printStackTrace()
					setLoading(false)
					showSnackbar("Terjadi kesalahan! ${e.message}", false)

				}
			}
		}

	private val uploadFileC200Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc200.pdf")
				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c200Part = MultipartBody.Part.createFormData("c200", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {
								dokumenViewModel.uploadC200Process(
									apiToken = apiToken.toString(),
									idKelompok = id.toString(),
									c200 = c200Part
								)
							}
						}


					dokumenViewModel.uploadC200ProcessResult.observe(viewLifecycleOwner) { uploadC200ProcessResult ->
						when (uploadC200ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC200ProcessResult.payload?.status
								showSnackbar(status ?: "Terjadi kesalahan!", false)
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC200ProcessResult.payload?.status

								Log.d("C200 RESULT", uploadC200ProcessResult.toString())
								if (uploadC200ProcessResult.payload?.success == true && uploadC200ProcessResult.payload.data != null) {
									showSnackbar(status ?: "Berhasil!", true)
									checkDokumen()

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
										showSnackbar("Sesi anda telah berakhir :(", true)

										actionIfLogoutSucces()
									} else {
										showSnackbar(status ?: "Terjadi kesalahan!", true)

									}
								}
							}

							else -> {}
						}
					}

				} catch (e: Exception) {
					e.printStackTrace()
					showSnackbar("Terjadi kesalahan! ${e.message}", false)

					setLoading(false)
				}
			}
		}

	private val uploadFileC300Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc300.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c300Part = MultipartBody.Part.createFormData("c300", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {

								dokumenViewModel.uploadC300Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c300 = c300Part
								)
							}
						}


					dokumenViewModel.uploadC300ProcessResult.observe(viewLifecycleOwner) { uploadC300ProcessResult ->
						when (uploadC300ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC300ProcessResult.payload?.status
								showSnackbar(status ?: "Terjadi kesalahan!", false)
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC300ProcessResult.payload?.status

								if (uploadC300ProcessResult.payload?.success == true && uploadC300ProcessResult.payload.data != null) {
									showSnackbar(status ?: "Berhasil!", true)
									checkDokumen()

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
										showSnackbar("Sesi anda telah berakhir :(", true)

										actionIfLogoutSucces()
									} else {
										showSnackbar(status ?: "Terjadi kesalahan!", true)

									}
								}

							}

							else -> {}
						}
					}

				} catch (e: Exception) {
					e.printStackTrace()
					showSnackbar("Terjadi kesalahan! ${e.message}", false)

					setLoading(false)
				}
			}
		}

	private val uploadFileC400Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc400.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c400Part = MultipartBody.Part.createFormData("c400", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {

								dokumenViewModel.uploadC400Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c400 = c400Part
								)
							}
						}
					dokumenViewModel.uploadC400ProcessResult.observe(viewLifecycleOwner) { uploadC400ProcessResult ->
						when (uploadC400ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC400ProcessResult.payload?.status
								showSnackbar(status ?: "Terjadi kesalahan!", false)
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC400ProcessResult.payload?.status

								if (uploadC400ProcessResult.payload?.success == true && uploadC400ProcessResult.payload.data != null) {
									showSnackbar(status ?: "Berhasil!", true)
									checkDokumen()

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
										showSnackbar("Sesi anda telah berakhir :(", true)

										actionIfLogoutSucces()
									} else {
										showSnackbar(status ?: "Terjadi kesalahan!", true)

									}
								}

							}

							else -> {}
						}
					}

				} catch (e: Exception) {
					e.printStackTrace()
					showSnackbar("Terjadi kesalahan! ${e.message}", false)

					setLoading(false)
				}
			}
		}

	private val uploadFileC500Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc500.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c500Part = MultipartBody.Part.createFormData("c500", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {

								dokumenViewModel.uploadC500Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c500 = c500Part
								)
							}
						}

					dokumenViewModel.uploadC500ProcessResult.observe(viewLifecycleOwner) { uploadC500ProcessResult ->
						when (uploadC500ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC500ProcessResult.payload?.status
								showSnackbar(status ?: "Terjadi kesalahan!", false)
							}

							is Resource.Success -> {
								setLoading(false)
								val status = uploadC500ProcessResult.payload?.status

								if (uploadC500ProcessResult.payload?.success == true && uploadC500ProcessResult.payload.data != null) {
									showSnackbar(status ?: "Berhasil!", true)
									checkDokumen()

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
										showSnackbar("Sesi anda telah berakhir :(", true)

										actionIfLogoutSucces()
									} else {
										showSnackbar(status ?: "Terjadi kesalahan!", true)

									}
								}


							}

							else -> {}
						}
					}

				} catch (e: Exception) {
					e.printStackTrace()
					showSnackbar("Terjadi kesalahan! ${e.message}", false)

					setLoading(false)
				}
			}
		}

	@SuppressLint("SetTextI18n")
	private fun checkFile(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		setViewAppearance(
			binding.btnUnduhC100,
			binding.tvNamaDokumenC100,
			fileMhs?.fileNameC100,
			binding.garisVertical,
			false
		)

		setViewAppearance(
			binding.btnUnduhC200,
			binding.tvNamaDokumenC200,
			fileMhs?.fileNameC200,
			binding.garisVerticalC200,
			false
		)

		setViewAppearance(
			binding.btnUnduhC300,
			binding.tvNamaDokumenC300,
			fileMhs?.fileNameC300,
			binding.garisVerticalC300,
			false
		)

		setViewAppearance(
			binding.btnUnduhC400,
			binding.tvNamaDokumenC400,
			fileMhs?.fileNameC400,
			binding.garisVerticalC400,
			false
		)

		setViewAppearance(
			binding.btnUnduhC500,
			binding.tvNamaDokumenC500,
			fileMhs?.fileNameC500,
			binding.garisVerticalC500,
			false
		)
	}

	private fun setViewAppearance(
		button: MaterialButton,
		textView: TextView,
		fileName: String?,
		garisVertical: View,
		disabled: Boolean,
	) {
		if (fileName == null) {
			button.isEnabled = false

			// Set button appearance when disabled
			button.setBackgroundColor(
				ContextCompat.getColor(
					requireContext(), R.color.tabUnselected
				)
			)
			button.strokeWidth = 2
			button.strokeColor = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.tabUnselected)
			)
			button.setTextColor(ContextCompat.getColor(requireContext(), R.color.White))
		} else {
			button.setBackgroundColor(
				ContextCompat.getColor(
					requireContext(), R.color.RoyalBlue
				)
			)
			button.strokeWidth = 2
			button.strokeColor = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.RoyalBlue)
			)

			textView.text = fileName
			button.isEnabled = !disabled
			garisVertical.backgroundTintList = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.RoyalBlue)
			)
		}
	}

	private fun checkAndRequestStoragePermissions() {
		val writePermission = ContextCompat.checkSelfPermission(
			requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
		)
		val readPermission = ContextCompat.checkSelfPermission(
			requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
		)

		val permissionsToRequest = mutableListOf<String>()

		if (writePermission != PackageManager.PERMISSION_GRANTED) {
			permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
		}
		if (readPermission != PackageManager.PERMISSION_GRANTED) {
			permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
		}

		if (permissionsToRequest.isNotEmpty()) {
			requestPermissions(
				permissionsToRequest.toTypedArray(), STORAGE_PERMISSION_REQUEST_CODE
			)
		}
	}

	// Metode ini akan dipanggil setelah pengguna memberikan atau menolak izin
	@Deprecated("Deprecated in Java")
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray,
	) {
		when (requestCode) {
			STORAGE_PERMISSION_REQUEST_CODE -> {
				// Memeriksa apakah izin diberikan atau tidak
				if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
					// Izin diberikan, Anda dapat melanjutkan dengan operasi yang membutuhkan izin
					// Contoh: Membaca atau menulis file
				} else {
					// Izin ditolak, Anda dapat memberikan informasi atau mengambil tindakan tambahan

					showPermissionDeniedDialog()
				}
			}

			else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

	private fun showSnackbar(status: String, isRestart: Boolean) {
		val currentFragment = this@MahasiswaDokumenCapstoneFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), status, "OK"
			) {
				customSnackbar.dismissSnackbar()
				if (isRestart) {
					restartFragment()
				}
			}
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

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDokumenCapstone, isLoading)

		}
	}

	private fun setLoadingKelompok(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDokumenCapstone, isLoading)
			binding.linearLayoutDokumenCapstone.visibility =
				if (isLoading) View.GONE else View.VISIBLE

		}
	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaDokumenCapstoneFragment

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

	private fun setShimmerVisibility(shimmerView: View, isLoading: Boolean) {
		shimmerView.visibility = if (isLoading) View.VISIBLE else View.GONE
		(shimmerView as? ShimmerFrameLayout)?.run {
			if (isLoading) startShimmer() else stopShimmer()
		}
	}

	private fun showCustomAlertDialog(
		title: String,
		status: String,
		positiveAction: () -> Unit,
	) {
		val builder = AlertDialog.Builder(requireContext()).create()
		val view = layoutInflater.inflate(R.layout.dialog_custom_alert_dialog, null)
		builder.setView(view)
		builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

		val buttonYes = view.findViewById<Button>(R.id.btn_alert_yes)
		val buttonNo = view.findViewById<Button>(R.id.btn_alert_no)
		val alertTitle = view.findViewById<TextView>(R.id.tv_alert_title)
		val alertstatus = view.findViewById<TextView>(R.id.tv_alert_message)

		alertTitle.text = title
		alertstatus.text = status

		buttonYes.setOnClickListener {
			positiveAction.invoke()
			builder.dismiss()
		}

		buttonNo.setOnClickListener {
			builder.dismiss()
		}

		builder.setCanceledOnTouchOutside(true)
		builder.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null

	}

	companion object {
		const val STORAGE_PERMISSION_REQUEST_CODE = 100
	}

}