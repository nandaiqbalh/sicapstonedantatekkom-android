package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kel022322.sicapstonedantatekkom.databinding.ActivityTestBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login.LoginViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.pengumuman.PengumumanViewModel
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

	private var _binding: ActivityTestBinding? = null
	private val binding get() = _binding!!

	private val viewModel: LoginViewModel by viewModels()
	private val pengumumanViewModel: PengumumanViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityTestBinding.inflate(layoutInflater)
		setContentView(binding.root)

		testAuth()
	}

	private fun testAuth() {
		/*


		binding.btnTestLogin.setOnClickListener {

			setLoading(true)

			viewModel.authLogin(AuthRequestBody("21120120130101", "mahasiswa12"))

			viewModel.authResult.observe(this) { authResult ->

				when (authResult) {
					is Resource.Loading -> setLoading(true)
					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", authResult.payload?.status.toString())
						Log.d("Result message", authResult.payload?.message.toString())
						Log.d("Exception", authResult.exception?.message.toString())

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", authResult.payload?.status.toString())
						Log.d("Result message", authResult.payload?.message.toString())
						Log.d("Result Auth", authResult.payload?.userData.toString())

					}

					else -> {}

				}
			}
		}

		 */

		binding.btnTestLogin.setOnClickListener {

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

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", broadcastResult.payload?.status.toString())
						Log.d("Result message", broadcastResult.payload?.message.toString())
						Log.d("Result Auth", broadcastResult.payload?.data.toString())

					}

					else -> {}

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