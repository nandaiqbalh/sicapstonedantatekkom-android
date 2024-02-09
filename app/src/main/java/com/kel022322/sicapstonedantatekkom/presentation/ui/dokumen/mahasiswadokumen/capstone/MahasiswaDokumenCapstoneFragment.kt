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
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenCapstoneBinding
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
class MahasiswaDokumenCapstoneFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenCapstoneBinding? = null
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

					binding.cvBelumMemilikiKelompok.visibility = View.GONE

				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result error", getKelompokSayaResult.toString())

					binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
					binding.tvBelumMemilikiKelompok.text = getString(R.string.tv_terjadi_kesalahan)

					binding.linearLayoutDokumenCapstone.visibility = View.GONE
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())

					val dataKelompok = getKelompokSayaResult.payload?.data

					if (dataKelompok?.kelompok != null) {

						if (dataKelompok.kelompok.idKelompok != null) {
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
					if (message == "Gagal! Anda telah masuk melalui perangkat lain." ){
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

		binding.tvPilihDokumenC100.setOnClickListener {

			setLoading(true)
			uploadFileC100Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC200.setOnClickListener {

			setLoading(true)
			uploadFileC200Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC300.setOnClickListener {

			setLoading(true)
			uploadFileC300Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC400.setOnClickListener {

			setLoading(true)
			uploadFileC400Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC500.setOnClickListener {

			setLoading(true)
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

					dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(viewLifecycleOwner) { apiToken ->
									apiToken?.let {

										dokumenViewModel.uploadC100Process(
											userId = userId.toString(),
											apiToken = apiToken.toString(),
											id = id.toString(),
											c100 = c100Part
										)
									}
								}
						}
					}

					dokumenViewModel.uploadC100ProcessResult.observe(viewLifecycleOwner) { uploadC100ProcessResult ->
						when (uploadC100ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val message = uploadC100ProcessResult.payload?.message
								Log.d("HASIL UPLOAD ERROR", message.toString())
								showSnackbar(message ?: "Terjadi kesalahan!")
							}

							is Resource.Success -> {
								setLoading(false)

								val message = uploadC100ProcessResult.payload?.message

								if (uploadC100ProcessResult.payload?.status == true) {
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

					dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(viewLifecycleOwner) { apiToken ->
									apiToken?.let {

										dokumenViewModel.uploadC200Process(
											userId = userId.toString(),
											apiToken = apiToken.toString(),
											id = id.toString(),
											c200 = c200Part
										)
									}
								}
						}
					}

					dokumenViewModel.uploadC200ProcessResult.observe(viewLifecycleOwner) { uploadC200ProcessResult ->
						when (uploadC200ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val message = uploadC200ProcessResult.payload?.message
								showSnackbar(message ?: "Terjadi kesalahan!")
							}

							is Resource.Success -> {
								setLoading(false)

								val message = uploadC200ProcessResult.payload?.message

								Log.d("C200 RESULT", uploadC200ProcessResult.toString())
								if (uploadC200ProcessResult.payload?.status == true) {
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

					dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(viewLifecycleOwner) { apiToken ->
									apiToken?.let {

										dokumenViewModel.uploadC300Process(
											userId = userId.toString(),
											apiToken = apiToken.toString(),
											id = id.toString(),
											c300 = c300Part
										)
									}
								}
						}
					}

					dokumenViewModel.uploadC300ProcessResult.observe(viewLifecycleOwner) { uploadC300ProcessResult ->
						when (uploadC300ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val message = uploadC300ProcessResult.payload?.message
								showSnackbar(message ?: "Terjadi kesalahan!")
							}

							is Resource.Success -> {
								setLoading(false)

								val message = uploadC300ProcessResult.payload?.message

								if (uploadC300ProcessResult.payload?.status == true) {
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

					dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(viewLifecycleOwner) { apiToken ->
									apiToken?.let {

										dokumenViewModel.uploadC400Process(
											userId = userId.toString(),
											apiToken = apiToken.toString(),
											id = id.toString(),
											c400 = c400Part
										)
									}
								}
						}
					}

					dokumenViewModel.uploadC400ProcessResult.observe(viewLifecycleOwner) { uploadC400ProcessResult ->
						when (uploadC400ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val message = uploadC400ProcessResult.payload?.message
								showSnackbar(message ?: "Terjadi kesalahan!")
							}

							is Resource.Success -> {
								setLoading(false)

								val message = uploadC400ProcessResult.payload?.message

								if (uploadC400ProcessResult.payload?.status == true) {
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

					dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(viewLifecycleOwner) { apiToken ->
									apiToken?.let {

										dokumenViewModel.uploadC500Process(
											userId = userId.toString(),
											apiToken = apiToken.toString(),
											id = id.toString(),
											c500 = c500Part
										)
									}
								}
						}
					}

					dokumenViewModel.uploadC500ProcessResult.observe(viewLifecycleOwner) { uploadC500ProcessResult ->
						when (uploadC500ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val message = uploadC500ProcessResult.payload?.message
								showSnackbar(message ?: "Terjadi kesalahan!")
							}

							is Resource.Success -> {
								setLoading(false)
								val message = uploadC500ProcessResult.payload?.message

								if (uploadC500ProcessResult.payload?.status == true) {
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

		binding.btnUnduhC100.setOnClickListener {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin ingin mengunduh dokumen?"
			) { unduhFileC100(getFileIndexResult) }
		}

		binding.btnUnduhC200.setOnClickListener {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin ingin mengunduh dokumen?"
			) { unduhFileC200(getFileIndexResult) }
		}

		binding.btnUnduhC300.setOnClickListener {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin ingin mengunduh dokumen?"
			) { unduhFileC300(getFileIndexResult) }
		}

		binding.btnUnduhC400.setOnClickListener {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin ingin mengunduh dokumen?"
			) { unduhFileC400(getFileIndexResult) }
		}

		binding.btnUnduhC500.setOnClickListener {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin ingin mengunduh dokumen?"
			) { unduhFileC500(getFileIndexResult) }
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
			button.setBackgroundColor(
				ContextCompat.getColor(
					requireContext(),
					R.color.RoyalBlue
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

	@SuppressLint("SetTextI18n")
	private fun unduhFileC100(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		setLoading(true)

		dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now

						val filePath = fileMhs?.filePathC100.toString()
						val modifiedPath = filePath.replaceFirst("/", "")

						if (fileMhs != null) {
							dokumenViewModel.viewPdf(
								ViewPdfRemoteRequestBody(
									userId = userId.toString(),
									apiToken = apiToken.toString(),
									filePath = "${modifiedPath}/",
									fileName = fileMhs.fileNameC100.toString()
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
						val fileName = fileMhs?.fileNameC100
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
	private fun unduhFileC200(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		setLoading(true)

		dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now

						val filePath = fileMhs?.filePathC200.toString()
						val modifiedPath = filePath.replaceFirst("/", "")

						if (fileMhs != null) {
							dokumenViewModel.viewPdf(
								ViewPdfRemoteRequestBody(
									userId = userId.toString(),
									apiToken = apiToken.toString(),
									filePath = "${modifiedPath}/",
									fileName = fileMhs.fileNameC200.toString()
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
						val fileName = fileMhs?.fileNameC200
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
	private fun unduhFileC300(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs


		setLoading(true)

		dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now

						val filePath = fileMhs?.filePathC300.toString()
						val modifiedPath = filePath.replaceFirst("/", "")

						if (fileMhs != null) {
							dokumenViewModel.viewPdf(
								ViewPdfRemoteRequestBody(
									userId = userId.toString(),
									apiToken = apiToken.toString(),
									filePath = "${modifiedPath}/",
									fileName = fileMhs.fileNameC300.toString()
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
						val fileName = fileMhs?.fileNameC300
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
	private fun unduhFileC400(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs



		setLoading(true)
		dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now

						val filePath = fileMhs?.filePathC400.toString()
						val modifiedPath = filePath.replaceFirst("/", "")

						if (fileMhs != null) {
							dokumenViewModel.viewPdf(
								ViewPdfRemoteRequestBody(
									userId = userId.toString(),
									apiToken = apiToken.toString(),
									filePath = "${modifiedPath}/",
									fileName = fileMhs.fileNameC400.toString()
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
						val fileName = fileMhs?.fileNameC400
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
	private fun unduhFileC500(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs


		setLoading(true)
		dokumenViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now

						val filePath = fileMhs?.filePathC500.toString()
						val modifiedPath = filePath.replaceFirst("/", "")

						if (fileMhs != null) {
							dokumenViewModel.viewPdf(
								ViewPdfRemoteRequestBody(
									userId = userId.toString(),
									apiToken = apiToken.toString(),
									filePath = "${modifiedPath}/",
									fileName = fileMhs.fileNameC500.toString()
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
						val fileName = fileMhs?.fileNameC500
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
		val currentFragment = this@MahasiswaDokumenCapstoneFragment

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

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDokumenCapstone, isLoading)

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