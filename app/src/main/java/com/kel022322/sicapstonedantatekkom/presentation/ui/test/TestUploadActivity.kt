
package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import android.Manifest
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
import com.kel022322.sicapstonedantatekkom.presentation.ui.filesaya.FileSayaViewModel
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class TestUploadActivity : AppCompatActivity() {

	private var _binding: ActivityTestUploadBinding? = null
	private val binding get() = _binding!!

	private val fileSayaViewModel: FileSayaViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityTestUploadBinding.inflate(layoutInflater)
		setContentView(binding.root)

		checkIndex()

		btnListenerTestApi()
	}

	private fun checkIndex() {
		setLoading(true)

		fileSayaViewModel.getUserId().observe(this) { userId ->
			if (userId != null) {
				fileSayaViewModel.getApiToken().observe(this) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now
						fileSayaViewModel.getFileIndex(
							FileIndexRemoteRequestBody(
								userId = userId.toString(), apiToken = apiToken.toString()
							)
						)
					}
				}
			}
		}

		fileSayaViewModel.getFileIndexResult.observe(this) { getFileIndexResult ->

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

					viewPdfMakalah(getFileIndexResult)
				}

				else -> {}
			}
		}

	}

	private fun btnListenerTestApi() {
		binding.btnUploadMakalah.setOnClickListener {
			setLoading(true)
			uploadFileLauncher.launch("application/pdf")
		}

		fileSayaViewModel.uploadMakalahProcessResult.observe(this) { uploadMakalahProcessResult ->
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
	}

	private val uploadFileLauncher =
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

					val requestBody =
						RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
					val makalahPart =
						MultipartBody.Part.createFormData("makalah", file.name, requestBody)

					fileSayaViewModel.getUserId().observe(this@TestUploadActivity) { userId ->
						userId?.let {
							fileSayaViewModel.getApiToken()
								.observe(this@TestUploadActivity) { apiToken ->
									apiToken?.let {
										Log.d("VALUEEE UserId", "UserId: $userId")

										fileSayaViewModel.uploadMakalahProcess(
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

	private fun viewPdfMakalah(getFileIndexResult : Resource<FileIndexRemoteResponse>){
		if (getFileIndexResult.payload!!.data!!.fileMhs == null) {
			binding.tvHasilFileIndex.text = "Hasil: Anda belum memiliki kelompok"
		} else {
			binding.tvHasilFileIndex.text =
				"Hasil: Judul capstone anda adalah ${getFileIndexResult.payload.data!!.fileMhs!!.judulCapstone.toString()}"

		}

		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		if (fileMhs?.fileNameMakalah == null) {
			binding.tvMakalahUrl.text = "Hasil: Anda belum upload makalah"
		} else {
			binding.tvMakalahUrl.text = "Tekan untuk melihat makalah"

			binding.tvMakalahUrl.setOnClickListener {

				fileSayaViewModel.getUserId().observe(this) { userId ->
					if (userId != null) {
						fileSayaViewModel.getApiToken().observe(this) { apiToken ->
							if (apiToken != null) {
								// Both userId and apiToken are available now

								val filePath = fileMhs.filePathMakalah.toString()
								val modifiedPath = filePath.replaceFirst("/", "")

								fileSayaViewModel.viewPdf(
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

				fileSayaViewModel.viewPdfResult.observe(this) { viewPdfResult ->

					when (viewPdfResult) {
						is Resource.Loading -> {
							setLoading(true)
						}

						is Resource.Error -> {
							setLoading(false)
							Log.d(
								"Result status",
								viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message",
								viewPdfResult.payload?.message.toString()
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
								"Result status",
								viewPdfResult.payload?.status.toString()
							)
							Log.d(
								"Result message",
								viewPdfResult.payload?.message.toString()
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
									base64Encode,
									android.util.Base64.DEFAULT
								)

								// Save the file in the Downloads directory
								val fileName = fileMhs.fileNameMakalah
								val downloadDir =
									Environment.getExternalStoragePublicDirectory(
										Environment.DIRECTORY_DOWNLOADS
									)
								val filePath = File(downloadDir, fileName)

								// Write the byte array to the file
								val fileOutputStream = FileOutputStream(filePath)
								fileOutputStream.write(decodedBytes)
								fileOutputStream.close()										}

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