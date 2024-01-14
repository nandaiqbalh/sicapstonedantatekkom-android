package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.ActivityTestUploadBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.filesaya.FileSayaViewModel
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

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
	}

	private fun checkIndex() {
		setLoading(true)

		fileSayaViewModel.getUserId().observe(this) { userId ->
			if (userId != null) {
				fileSayaViewModel.getApiToken().observe(this) { apiToken ->
					if (apiToken != null) {
						// Both userId and apiToken are available now
						fileSayaViewModel.getFileIndex(FileIndexRemoteRequestBody(userId = userId.toString(), apiToken = apiToken.toString()))
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

					if (getFileIndexResult.payload!!.data!!.fileMhs == null){
						binding.tvHasilFileIndex.text = "Hasil: Anda belum memiliki kelompok"
					}else{
						binding.tvHasilFileIndex.text = "Hasil: Judul capstone anda adalah ${getFileIndexResult.payload.data!!.fileMhs!!.judulCapstone.toString()}"

					}
				}

				else -> {}
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