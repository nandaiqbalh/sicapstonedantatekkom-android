package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.request.ViewPdfRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.ActivityTestUploadBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.DokumenViewModel
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class TestUploadActivity : AppCompatActivity() {

	private var _binding: ActivityTestUploadBinding? = null
	private val binding get() = _binding!!

	private val dokumenViewModel: DokumenViewModel by viewModels()

	private var id: String? = ""
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityTestUploadBinding.inflate(layoutInflater)
		setContentView(binding.root)

		checkIndex()

		btnListenerTestApi()
	}

	private fun checkIndex() {
		setLoading(true)

		dokumenViewModel.getUserId().observe(this) { userId ->
			if (userId != null) {
				dokumenViewModel.getApiToken().observe(this) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now
						dokumenViewModel.getFileIndex(
							FileIndexRemoteRequestBody(
								userId = userId.toString(), apiToken = apiToken.toString()
							)
						)
					}
				}
			}
		}

		dokumenViewModel.getFileIndexResult.observe(this) { getFileIndexResult ->

			when (getFileIndexResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", getFileIndexResult.payload?.status.toString())
					Log.d("Result message", getFileIndexResult.payload?.message.toString())
					Log.d("Exception", getFileIndexResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${getFileIndexResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()

				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", getFileIndexResult.payload?.status.toString())
					Log.d("Result message", getFileIndexResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${getFileIndexResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()

					Log.d("FILE NAMEEE", getFileIndexResult.payload?.data?.fileMhs?.fileNameC100.toString())
					Log.d("FILE NAMEEE", getFileIndexResult.payload?.data?.fileMhs?.fileNameC200.toString())

					id = getFileIndexResult.payload?.data?.fileMhs?.id.toString()

					viewPdf(getFileIndexResult)

				}

				else -> {}
			}
		}

	}

	private fun btnListenerTestApi() {
		binding.btnUploadMakalah.setOnClickListener {
			setLoading(true)
			uploadFileMakalahLauncher.launch("application/pdf")
		}

		binding.btnUploadLaporan.setOnClickListener {
			setLoading(true)
			uploadFileLaporanLauncher.launch("application/pdf")
		}

		binding.btnUploadC100.setOnClickListener {
			setLoading(true)
			uploadFileC100Launcher.launch("application/pdf")

		}

		binding.btnUploadC200.setOnClickListener {
			setLoading(true)
			uploadFileC200Launcher.launch("application/pdf")

		}

		binding.btnUploadC300.setOnClickListener {
			setLoading(true)
			uploadFileC300Launcher.launch("application/pdf")

		}

		binding.btnUploadC400.setOnClickListener {
			setLoading(true)
			uploadFileC400Launcher.launch("application/pdf")

		}

		binding.btnUploadC500.setOnClickListener {
			setLoading(true)
			uploadFileC500Launcher.launch("application/pdf")

		}

		resultUploadObserver()
	}

	private fun resultUploadObserver() {
		dokumenViewModel.uploadMakalahProcessResult.observe(this) { uploadMakalahProcessResult ->
			when (uploadMakalahProcessResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", uploadMakalahProcessResult.payload?.status.toString())
					Log.d("Result message", uploadMakalahProcessResult.payload?.message.toString())
					Log.d("Exception", uploadMakalahProcessResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadMakalahProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", uploadMakalahProcessResult.payload?.status.toString())
					Log.d("Result message", uploadMakalahProcessResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadMakalahProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				else -> {}
			}
		}

		dokumenViewModel.uploadLaporanProcessResult.observe(this) { uploadLaporanProcessResult ->
			when (uploadLaporanProcessResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", uploadLaporanProcessResult.payload?.status.toString())
					Log.d("Result message", uploadLaporanProcessResult.payload?.message.toString())
					Log.d("Exception", uploadLaporanProcessResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadLaporanProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", uploadLaporanProcessResult.payload?.status.toString())
					Log.d("Result message", uploadLaporanProcessResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadLaporanProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				else -> {}
			}
		}

		dokumenViewModel.uploadC100ProcessResult.observe(this) { uploadC100ProcessResult ->
			when (uploadC100ProcessResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", uploadC100ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC100ProcessResult.payload?.message.toString())
					Log.d("Exception", uploadC100ProcessResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC100ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", uploadC100ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC100ProcessResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC100ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				else -> {}
			}
		}

		dokumenViewModel.uploadC200ProcessResult.observe(this) { uploadC200ProcessResult ->
			when (uploadC200ProcessResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", uploadC200ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC200ProcessResult.payload?.message.toString())
					Log.d("Exception", uploadC200ProcessResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC200ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", uploadC200ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC200ProcessResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC200ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				else -> {}
			}
		}

		dokumenViewModel.uploadC300ProcessResult.observe(this) { uploadC300ProcessResult ->
			when (uploadC300ProcessResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", uploadC300ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC300ProcessResult.payload?.message.toString())
					Log.d("Exception", uploadC300ProcessResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC300ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", uploadC300ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC300ProcessResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC300ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				else -> {}
			}
		}

		dokumenViewModel.uploadC400ProcessResult.observe(this) { uploadC400ProcessResult ->
			when (uploadC400ProcessResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", uploadC400ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC400ProcessResult.payload?.message.toString())
					Log.d("Exception", uploadC400ProcessResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC400ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", uploadC400ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC400ProcessResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC400ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				else -> {}
			}
		}

		dokumenViewModel.uploadC500ProcessResult.observe(this) { uploadC500ProcessResult ->
			when (uploadC500ProcessResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					Log.d("Result status", uploadC500ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC500ProcessResult.payload?.message.toString())
					Log.d("Exception", uploadC500ProcessResult.exception?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC500ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				is Resource.Success -> {
					setLoading(false)
					Log.d("Result status", uploadC500ProcessResult.payload?.status.toString())
					Log.d("Result message", uploadC500ProcessResult.payload?.message.toString())
					Toast.makeText(
						this@TestUploadActivity,
						"Result: ${uploadC500ProcessResult.payload?.message.toString()}",
						Toast.LENGTH_SHORT
					).show()
				}

				else -> {}
			}
		}
	}

	private val uploadFileMakalahLauncher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = applicationContext.contentResolver
				val inputStream = contentResolver.openInputStream(it)
				val file = File(cacheDir, "temp.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val makalahPart =
						MultipartBody.Part.createFormData("makalah", file.name, requestBody)

					dokumenViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
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

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@TestUploadActivity,
						"Error uploading file: ${e.message}",
						Toast.LENGTH_SHORT
					).show()
					setLoading(false)
				}
			}
		}

	private val uploadFileLaporanLauncher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = applicationContext.contentResolver
				val inputStream = contentResolver.openInputStream(it)
				val file = File(cacheDir, "temp.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val laporanPart =
						MultipartBody.Part.createFormData("laporan_ta", file.name, requestBody)

					dokumenViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
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

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@TestUploadActivity,
						"Error uploading file: ${e.message}",
						Toast.LENGTH_SHORT
					).show()
					setLoading(false)
				}
			}
		}

	private val uploadFileC100Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = applicationContext.contentResolver
				val inputStream = contentResolver.openInputStream(it)
				val file = File(cacheDir, "tempc100.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c100Part = MultipartBody.Part.createFormData("c100", file.name, requestBody)

					dokumenViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
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

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@TestUploadActivity,
						"Error uploading file: ${e.message}",
						Toast.LENGTH_SHORT
					).show()
					setLoading(false)
				}
			}
		}

	private val uploadFileC200Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = applicationContext.contentResolver
				val inputStream = contentResolver.openInputStream(it)
				val file = File(cacheDir, "tempc200.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c200Part = MultipartBody.Part.createFormData("c200", file.name, requestBody)

					dokumenViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
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

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@TestUploadActivity,
						"Error uploading file: ${e.message}",
						Toast.LENGTH_SHORT
					).show()
					setLoading(false)
				}
			}
		}

	private val uploadFileC300Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = applicationContext.contentResolver
				val inputStream = contentResolver.openInputStream(it)
				val file = File(cacheDir, "tempc300.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c300Part = MultipartBody.Part.createFormData("c300", file.name, requestBody)

					dokumenViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
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

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@TestUploadActivity,
						"Error uploading file: ${e.message}",
						Toast.LENGTH_SHORT
					).show()
					setLoading(false)
				}
			}
		}

	private val uploadFileC400Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = applicationContext.contentResolver
				val inputStream = contentResolver.openInputStream(it)
				val file = File(cacheDir, "tempc400.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c400Part = MultipartBody.Part.createFormData("c400", file.name, requestBody)

					dokumenViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
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

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@TestUploadActivity,
						"Error uploading file: ${e.message}",
						Toast.LENGTH_SHORT
					).show()
					setLoading(false)
				}
			}
		}

	private val uploadFileC500Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = applicationContext.contentResolver
				val inputStream = contentResolver.openInputStream(it)
				val file = File(cacheDir, "tempc500.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c500Part = MultipartBody.Part.createFormData("c500", file.name, requestBody)

					dokumenViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							dokumenViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
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

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@TestUploadActivity,
						"Error uploading file: ${e.message}",
						Toast.LENGTH_SHORT
					).show()
					setLoading(false)
				}
			}
		}

	@SuppressLint("SetTextI18n")
	private fun viewPdf(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		if (getFileIndexResult.payload!!.data!!.fileMhs == null) {
			binding.tvHasilFileIndex.text = "Hasil: Anda belum memiliki kelompok"
		} else {
			binding.tvHasilFileIndex.text =
				"Hasil: Judul capstone anda adalah ${getFileIndexResult.payload.data!!.fileMhs!!.judulCapstone.toString()}"

		}

		checkFileMakalah(getFileIndexResult)
		checkFileLaporan(getFileIndexResult)
		checkFileC100(getFileIndexResult)
		checkFileC200(getFileIndexResult)
		checkFileC300(getFileIndexResult)
		checkFileC400(getFileIndexResult)
		checkFileC500(getFileIndexResult)

	}

	private fun checkFileMakalah(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameMakalah == null) {
			binding.tvMakalahUrl.text = "Hasil: Anda belum upload makalah"
		} else {
			binding.tvMakalahUrl.text = "Tekan untuk unduh makalah"

			binding.tvMakalahUrl.setOnClickListener {

				dokumenViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						dokumenViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathMakalah.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

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

				dokumenViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Log.d(
								"Exception", viewPdfResult.exception?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: ${viewPdfResult.payload?.message.toString()}",
								Toast.LENGTH_SHORT
							).show()

						}

						is Resource.Success -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: Makalah berhasil diunduh!",
								Toast.LENGTH_SHORT
							).show()

							// Check if the app has the WRITE_EXTERNAL_STORAGE permission
							if (ContextCompat.checkSelfPermission(
									this@TestUploadActivity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE
								) != PackageManager.PERMISSION_GRANTED
							) {
								// Request the permission if it's not granted
								ActivityCompat.requestPermissions(
									this@TestUploadActivity,
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
								val fileName = fileMhs.fileNameMakalah
								val downloadDir = Environment.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS
								)
								val filePath = File(downloadDir, fileName)

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()
							}

						}

						else -> {}
					}
				}
			}
		}
	}

	private fun checkFileLaporan(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameLaporanTa == null) {
			binding.tvLaporanUrl.text = "Hasil: Anda belum upload laporan"
		} else {
			binding.tvLaporanUrl.text = "Tekan untuk unduh laporan"

			binding.tvLaporanUrl.setOnClickListener {

				dokumenViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						dokumenViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathLaporanTa.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

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

				dokumenViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Log.d(
								"Exception", viewPdfResult.exception?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: ${viewPdfResult.payload?.message.toString()}",
								Toast.LENGTH_SHORT
							).show()

						}

						is Resource.Success -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: Laporan berhasil diunduh!",
								Toast.LENGTH_SHORT
							).show()

							// Check if the app has the WRITE_EXTERNAL_STORAGE permission
							if (ContextCompat.checkSelfPermission(
									this@TestUploadActivity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE
								) != PackageManager.PERMISSION_GRANTED
							) {
								// Request the permission if it's not granted
								ActivityCompat.requestPermissions(
									this@TestUploadActivity,
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
								val fileName = fileMhs.fileNameLaporanTa
								val downloadDir = Environment.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS
								)
								val filePath = File(downloadDir, fileName.toString())

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()
							}

						}

						else -> {}
					}
				}
			}
		}
	}

	@SuppressLint("SetTextI18n")
	private fun checkFileC100(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameC100 == null) {
			binding.tvC100Url.text = "Hasil: Anda belum upload c100"
		} else {
			binding.tvC100Url.text = "Tekan untuk unduh laporan"

			binding.tvC100Url.setOnClickListener {

				dokumenViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						dokumenViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathC100.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

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

				dokumenViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Log.d(
								"Exception", viewPdfResult.exception?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: ${viewPdfResult.payload?.message.toString()}",
								Toast.LENGTH_SHORT
							).show()

						}

						is Resource.Success -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: PDF berhasil diunduh!",
								Toast.LENGTH_SHORT
							).show()

							// Check if the app has the WRITE_EXTERNAL_STORAGE permission
							if (ContextCompat.checkSelfPermission(
									this@TestUploadActivity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE
								) != PackageManager.PERMISSION_GRANTED
							) {
								// Request the permission if it's not granted
								ActivityCompat.requestPermissions(
									this@TestUploadActivity,
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
								val fileName = fileMhs.fileNameC100
								val downloadDir = Environment.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS
								)
								val filePath = File(downloadDir, fileName.toString())

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()
							}

						}

						else -> {}
					}
				}
			}
		}
	}


	@SuppressLint("SetTextI18n")
	private fun checkFileC200(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameC200 == null) {
			binding.tvC200Url.text = "Hasil: Anda belum upload c200"
		} else {
			binding.tvC200Url.text = "Tekan untuk unduh C200"

			binding.tvC200Url.setOnClickListener {

				dokumenViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						dokumenViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathC200.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

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

				dokumenViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Log.d(
								"Exception", viewPdfResult.exception?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: ${viewPdfResult.payload?.message.toString()}",
								Toast.LENGTH_SHORT
							).show()

						}

						is Resource.Success -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: C200 berhasil diunduh!",
								Toast.LENGTH_SHORT
							).show()

							// Check if the app has the WRITE_EXTERNAL_STORAGE permission
							if (ContextCompat.checkSelfPermission(
									this@TestUploadActivity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE
								) != PackageManager.PERMISSION_GRANTED
							) {
								// Request the permission if it's not granted
								ActivityCompat.requestPermissions(
									this@TestUploadActivity,
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
								val fileName = fileMhs.fileNameC200
								val downloadDir = Environment.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS
								)
								val filePath = File(downloadDir, fileName.toString())

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()
							}

						}

						else -> {}
					}
				}
			}
		}
	}


	@SuppressLint("SetTextI18n")
	private fun checkFileC300(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameC300 == null) {
			binding.tvC300Url.text = "Hasil: Anda belum upload c300"
		} else {
			binding.tvC300Url.text = "Tekan untuk unduh C300"

			binding.tvC300Url.setOnClickListener {

				dokumenViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						dokumenViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathC300.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

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

				dokumenViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Log.d(
								"Exception", viewPdfResult.exception?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: ${viewPdfResult.payload?.message.toString()}",
								Toast.LENGTH_SHORT
							).show()

						}

						is Resource.Success -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: C300 berhasil diunduh!",
								Toast.LENGTH_SHORT
							).show()

							// Check if the app has the WRITE_EXTERNAL_STORAGE permission
							if (ContextCompat.checkSelfPermission(
									this@TestUploadActivity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE
								) != PackageManager.PERMISSION_GRANTED
							) {
								// Request the permission if it's not granted
								ActivityCompat.requestPermissions(
									this@TestUploadActivity,
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
								val fileName = fileMhs.fileNameC300
								val downloadDir = Environment.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS
								)
								val filePath = File(downloadDir, fileName.toString())

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()
							}

						}

						else -> {}
					}
				}
			}
		}
	}


	@SuppressLint("SetTextI18n")
	private fun checkFileC400(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameC400 == null) {
			binding.tvC400Url.text = "Hasil: Anda belum upload c400"
		} else {
			binding.tvC400Url.text = "Tekan untuk unduh C400"

			binding.tvC400Url.setOnClickListener {

				dokumenViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						dokumenViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathC400.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

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

				dokumenViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Log.d(
								"Exception", viewPdfResult.exception?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: ${viewPdfResult.payload?.message.toString()}",
								Toast.LENGTH_SHORT
							).show()

						}

						is Resource.Success -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: C400 berhasil diunduh!",
								Toast.LENGTH_SHORT
							).show()

							// Check if the app has the WRITE_EXTERNAL_STORAGE permission
							if (ContextCompat.checkSelfPermission(
									this@TestUploadActivity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE
								) != PackageManager.PERMISSION_GRANTED
							) {
								// Request the permission if it's not granted
								ActivityCompat.requestPermissions(
									this@TestUploadActivity,
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
								val fileName = fileMhs.fileNameC400
								val downloadDir = Environment.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS
								)
								val filePath = File(downloadDir, fileName.toString())

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()
							}

						}

						else -> {}
					}
				}
			}
		}
	}


	@SuppressLint("SetTextI18n")
	private fun checkFileC500(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameC500 == null) {
			binding.tvC500Url.text = "Hasil: Anda belum upload c500"
		} else {
			binding.tvC500Url.text = "Tekan untuk unduh C500"

			binding.tvC500Url.setOnClickListener {

				dokumenViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						dokumenViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathC500.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

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

				dokumenViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Log.d(
								"Exception", viewPdfResult.exception?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: ${viewPdfResult.payload?.message.toString()}",
								Toast.LENGTH_SHORT
							).show()

						}

						is Resource.Success -> {
							setLoading(false)
							Log.d(
								"Result status", viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message", viewPdfResult.payload?.message.toString()
							)
							Toast.makeText(
								this@TestUploadActivity,
								"Result: C500 berhasil diunduh!",
								Toast.LENGTH_SHORT
							).show()

							// Check if the app has the WRITE_EXTERNAL_STORAGE permission
							if (ContextCompat.checkSelfPermission(
									this@TestUploadActivity,
									Manifest.permission.WRITE_EXTERNAL_STORAGE
								) != PackageManager.PERMISSION_GRANTED
							) {
								// Request the permission if it's not granted
								ActivityCompat.requestPermissions(
									this@TestUploadActivity,
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
								val fileName = fileMhs.fileNameC500
								val downloadDir = Environment.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS
								)
								val filePath = File(downloadDir, fileName.toString())

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()
							}

						}

						else -> {}
					}
				}
			}
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