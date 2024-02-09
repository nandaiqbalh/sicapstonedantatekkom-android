package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.tugasakhir

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
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.request.ViewPdfRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenTugasAkhirBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.DokumenViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.KelompokSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
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
class MahasiswaDokumenTugasAkhirFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenTugasAkhirBinding? = null
	private val binding get() = _binding!!

	private val customSnackbar = CustomSnackbar()

	private val dokumenViewModel: DokumenViewModel by viewModels()
	private val profileViewModel: ProfileSayaViewModel by viewModels()
	private val kelompokViewModel: KelompokSayaViewModel by viewModels()

	private var id: String? = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding =
			FragmentMahasiswaDokumenTugasAkhirBinding.inflate(layoutInflater, container, false)
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
		setLoading(true)

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						kelompokViewModel.getKelompokSaya(
							KelompokSayaRemoteRequestBody(
								userId,
								apiToken
							)
						)

					}
				}
			}
		}

		kelompokViewModel.getKelompokSayaResult.observe(viewLifecycleOwner) { getKelompokSayaResult ->

			when (getKelompokSayaResult) {
				is Resource.Loading -> {
					setLoading(true)

					binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.GONE

				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result error", getKelompokSayaResult.toString())

					binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.VISIBLE
					binding.tvBelumMemilikiKelompokTugasAkhir.text =
						getString(R.string.tv_terjadi_kesalahan)

					binding.linearLayoutDokumenTugasAkhir.visibility = View.GONE
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())

					val dataKelompok = getKelompokSayaResult.payload?.data

					if (dataKelompok?.kelompok != null) {

						if (dataKelompok.kelompok.idKelompok != null) {
							binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.GONE

							binding.linearLayoutDokumenTugasAkhir.visibility = View.VISIBLE
						} else {
							binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.VISIBLE
							binding.tvBelumMemilikiKelompokTugasAkhir.setText(R.string.kelompok_belum_valid)

							binding.linearLayoutDokumenTugasAkhir.visibility = View.GONE
						}

					} else {
						binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.VISIBLE
						binding.tvBelumMemilikiKelompokTugasAkhir.setText(R.string.belum_memiliki_kelompok)

						binding.linearLayoutDokumenTugasAkhir.visibility = View.GONE
					}
				}

				else -> {}
			}
		}
	}

	private fun checkDokumen() {
		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						dokumenViewModel.getFileIndex(FileIndexRemoteRequestBody(userId, apiToken))

					}
				}
			}
		}

		dokumenViewModel.getFileIndexResult.observe(viewLifecycleOwner) { getFileIndexResult ->

			when (getFileIndexResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					showSnackbar("Terjadi kesalahan dalam mengakses dokumen!")

				}

				is Resource.Success -> {
					setLoading(false)

					val message = getFileIndexResult.payload?.message
					if (message == "Gagal! Anda telah masuk melalui perangkat lain.") {
						showSnackbar(message = message)
						return@observe
					}

					if (getFileIndexResult.payload?.status == false) {
						showSnackbar("Terjadi kesalahan dalam mengakses dokumen!")
						return@observe
					}



					id = getFileIndexResult.payload?.data?.fileMhs?.id.toString()

					viewPdf(getFileIndexResult)

					checkFile(getFileIndexResult)

				}

				else -> {}
			}
		}

	}

	private fun btnListener() {

		binding.tvPilihDokumenLaporanTa.setOnClickListener {

			setLoading(true)
			uploadFileLaporanLauncher.launch("application/pdf")

		}

		binding.tvPilihDokumenMakalahTa.setOnClickListener {

			setLoading(true)
			uploadFileMakalahLauncher.launch("application/pdf")

		}


	}

	private val uploadFileLaporanLauncher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "laporan_ta.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val laporanPart =
						MultipartBody.Part.createFormData("laporan_ta", file.name, requestBody)

					dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(viewLifecycleOwner) { apiToken ->
									apiToken?.let {

										dokumenViewModel.uploadLaporanProcess(
											userId = userId.toString(),
											apiToken = apiToken.toString(),
											idMahasiswa = userId.toString(),
											laporan_ta = laporanPart
										)
									}
								}
						}
					}

					dokumenViewModel.uploadLaporanProcessResult.observe(viewLifecycleOwner) { uploadLaporanProcessResult ->
						when (uploadLaporanProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val message = uploadLaporanProcessResult.payload?.message
								Log.d("HASIL UPLOAD ERROR", message.toString())
								showSnackbar(message ?: "Terjadi kesalahan!")
							}

							is Resource.Success -> {
								setLoading(false)

								val message = uploadLaporanProcessResult.payload?.message

								if (uploadLaporanProcessResult.payload?.status == true) {
									showSnackbar(message ?: "Berhasil!")
									checkDokumen()

								} else {
									showSnackbar("Gagal menggunggah dokument.")
								}

								Log.d("HASIL UPLOAD BERHASIL", message.toString())

							}

							else -> {}
						}
					}

				} catch (e: Exception) {
					e.printStackTrace()
					setLoading(false)
					showSnackbar("Terjadi kesalahan! ${e.message}")

				}
			}
		}

	private val uploadFileMakalahLauncher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "makalah.pdf")
				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val makalahPart =
						MultipartBody.Part.createFormData("makalah", file.name, requestBody)

					dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(viewLifecycleOwner) { apiToken ->
									apiToken?.let {

										dokumenViewModel.uploadMakalahProcess(
											userId = userId.toString(),
											apiToken = apiToken.toString(),
											idMahasiswa = userId.toString(),
											makalah = makalahPart
										)
									}
								}
						}

					}

					dokumenViewModel.uploadMakalahProcessResult.observe(viewLifecycleOwner) { uploadMakalahProcessResult ->
						when (uploadMakalahProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val message = uploadMakalahProcessResult.payload?.message
								showSnackbar(message ?: "Terjadi kesalahan!")
							}

							is Resource.Success -> {
								setLoading(false)

								val message = uploadMakalahProcessResult.payload?.message

								if (uploadMakalahProcessResult.payload?.status == true) {
									showSnackbar(message ?: "Berhasil!")
									checkDokumen()

								} else {
									showSnackbar("Gagal menggunggah dokument.")
								}
							}

							else -> {}
						}
					}

				} catch (e: Exception) {
					e.printStackTrace()
					showSnackbar("Terjadi kesalahan! ${e.message}")

					setLoading(false)
				}
			}
		}

	@SuppressLint("SetTextI18n")
	private fun viewPdf(getFileIndexResult: Resource<FileIndexRemoteResponse>) {

		binding.btnUnduhLaporan.setOnClickListener {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin ingin mengunduh dokumen?"
			) { unduhLaporanTa(getFileIndexResult) }
		}

		binding.btnUnduhMakalahTa.setOnClickListener {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin ingin mengunduh dokumen?"
			) { unduhMakalahTa(getFileIndexResult) }
		}

	}

	@SuppressLint("SetTextI18n")
	private fun checkFile(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		setViewAppearance(
			binding.btnUnduhLaporan,
			binding.tvNamaDokumenLaporanTa,
			fileMhs?.fileNameLaporanTa,
			binding.garisVerticalLaporan,
			false
		)

		setViewAppearance(
			binding.btnUnduhMakalahTa,
			binding.tvNamaDokumenMakalahTa,
			fileMhs?.fileNameMakalah,
			binding.garisVerticalMakalah,
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
					requireContext(),
					R.color.tabUnselected
				)
			)
			button.strokeWidth = 2
			button.strokeColor = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.tabUnselected)
			)
			button.setTextColor(ContextCompat.getColor(requireContext(), R.color.White))
		} else {
			textView.text = fileName
			button.isEnabled = !disabled
			garisVertical.backgroundTintList = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.RoyalBlue)
			)
		}
	}

	@SuppressLint("SetTextI18n")
	private fun unduhLaporanTa(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		setLoading(true)

		dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now

						val filePath = fileMhs?.filePathLaporanTa.toString()
						val modifiedPath = filePath.replaceFirst("/", "")

						if (fileMhs != null) {
							dokumenViewModel.viewPdf(
								ViewPdfRemoteRequestBody(
									userId = userId.toString(),
									apiToken = apiToken.toString(),
									filePath = "${modifiedPath}/",
									fileName = fileMhs.fileNameLaporanTa.toString()
								)
							)
						}
					}
				}
			}
		}

		dokumenViewModel.viewPdfResult.observe(viewLifecycleOwner) { viewPdfResult ->

			when (viewPdfResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					showSnackbar("Gagal mengunduh!")
				}

				is Resource.Success -> {
					setLoading(false)

					// Check if the app has the WRITE_EXTERNAL_STORAGE permission
					if (ContextCompat.checkSelfPermission(
							requireContext(),
							Manifest.permission.WRITE_EXTERNAL_STORAGE
						) != PackageManager.PERMISSION_GRANTED
					) {
						// Request the permission if it's not granted
						ActivityCompat.requestPermissions(
							requireActivity(),
							arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
							200
						)
					} else {
						// Permission already granted, proceed with file operations
						val base64Encode = viewPdfResult.payload?.data.toString()

						// Decode base64 string to byte array
						val decodedBytes = android.util.Base64.decode(
							base64Encode, android.util.Base64.DEFAULT
						)

						// Save the file in the Downloads directory
						val fileName = fileMhs?.fileNameLaporanTa
						val downloadDir = Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_DOWNLOADS
						)
						val filePath = File(downloadDir, fileName.toString())

						// Write the byte array to the file
						val fileOutputStream = FileOutputStream(filePath)
						fileOutputStream.write(decodedBytes)
						fileOutputStream.close()


						if (filePath.exists() && filePath.length() > 0) {
							showSnackbar("Berhasil mengunduh!")
						} else {
							showSnackbar("Gagal mengunduh!")
						}
					}

				}

				else -> {}


			}
		}
	}

	@SuppressLint("SetTextI18n")
	private fun unduhMakalahTa(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		setLoading(true)

		dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now

						val filePath = fileMhs?.filePathMakalah.toString()
						val modifiedPath = filePath.replaceFirst("/", "")

						if (fileMhs != null) {
							dokumenViewModel.viewPdf(
								ViewPdfRemoteRequestBody(
									userId = userId.toString(),
									apiToken = apiToken.toString(),
									filePath = "${modifiedPath}/",
									fileName = fileMhs.fileNameMakalah.toString()
								)
							)
						}
					}
				}
			}
		}

		dokumenViewModel.viewPdfResult.observe(viewLifecycleOwner) { viewPdfResult ->

			when (viewPdfResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					showSnackbar("Gagal mengunduh!")

				}

				is Resource.Success -> {
					setLoading(false)

					// Check if the app has the WRITE_EXTERNAL_STORAGE permission
					if (ContextCompat.checkSelfPermission(
							requireContext(),
							Manifest.permission.WRITE_EXTERNAL_STORAGE
						) != PackageManager.PERMISSION_GRANTED
					) {
						// Request the permission if it's not granted
						ActivityCompat.requestPermissions(
							requireActivity(),
							arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
							200
						)
					} else {
						// Permission already granted, proceed with file operations
						val base64Encode = viewPdfResult.payload?.data.toString()

						// Decode base64 string to byte array
						val decodedBytes = android.util.Base64.decode(
							base64Encode, android.util.Base64.DEFAULT
						)

						// Save the file in the Downloads directory
						val fileName = fileMhs?.fileNameMakalah
						val downloadDir = Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_DOWNLOADS
						)
						val filePath = File(downloadDir, fileName.toString())

						// Write the byte array to the file
						val fileOutputStream = FileOutputStream(filePath)
						fileOutputStream.write(decodedBytes)
						fileOutputStream.close()

						if (filePath.exists() && filePath.length() > 0) {
							showSnackbar("Berhasil mengunduh!")
						} else {
							showSnackbar("Gagal mengunduh!")
						}
					}

				}

				else -> {}


			}
		}
	}

	private fun checkAndRequestStoragePermissions() {
		val writePermission = ContextCompat.checkSelfPermission(
			requireContext(),
			Manifest.permission.WRITE_EXTERNAL_STORAGE
		)
		val readPermission = ContextCompat.checkSelfPermission(
			requireContext(),
			Manifest.permission.READ_EXTERNAL_STORAGE
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
				permissionsToRequest.toTypedArray(),
				STORAGE_PERMISSION_REQUEST_CODE
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

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaDokumenTugasAkhirFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content),
				message,
				"OK"
			) {
				customSnackbar.dismissSnackbar()
				if (message == "Berhasil keluar!" || message == "Gagal! Anda telah masuk melalui perangkat lain." || message == "Pengguna tidak ditemukan!" || message == "Akses tidak sah!" || message == "Sesi anda telah berakhir, silahkan masuk terlebih dahulu.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setUsername("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				} else if (message == "null" || message == "Laporan gagal diupload." || message == "Laporan tidak ditemukan." || message == "Gagal menyimpan file." || message == "Dokumen berhasil diunggah." || message.equals(
						null
					) || message == "Terjadi kesalahan!" || message == "Berhasil mengunduh!" || message == "Gagal mengunduh!"
				) {
					restartFragment()
				} else if (message == "Password berhasil diubah, silahkan masuk kembali.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setUsername("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				}
			}
		}

	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaDokumenTugasAkhirFragment

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

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDokumenTugasAkhir, isLoading)

//			linearLayoutDokumenCapstone.visibility = if (isLoading) View.GONE else View.VISIBLE
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