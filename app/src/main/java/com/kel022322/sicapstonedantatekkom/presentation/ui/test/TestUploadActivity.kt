package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kel022322.sicapstonedantatekkom.BuildConfig
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
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

						// Assuming `fileMhs.fileNameMakalah` is the URL you want to open
						val url = "${BuildConfig.BASE_URL}${fileMhs.filePathMakalah.toString()}/${fileMhs.fileNameMakalah.toString()}"

						binding.tvMakalahUrl.setOnClickListener {
							// Create an Intent with ACTION_VIEW
							val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

							// Check if there's an app that can handle this intent
							if (browserIntent.resolveActivity(packageManager) != null) {
								// Start the browser activity
								startActivity(browserIntent)
							} else {
								// Handle the case where there is no app to handle the intent
								Toast.makeText(this, "No app to handle the link.", Toast.LENGTH_SHORT).show()
							}
						}
					}
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


	private fun setLoading(isLoading: Boolean) {
		binding.pbTes.isVisible = isLoading
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}