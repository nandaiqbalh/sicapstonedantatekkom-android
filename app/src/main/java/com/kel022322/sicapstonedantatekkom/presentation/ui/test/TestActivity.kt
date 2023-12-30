package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.request.AuthRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.ActivityTestBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

	private var _binding: ActivityTestBinding? = null
	private val binding get() = _binding!!

	private val viewModel: LoginViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityTestBinding.inflate(layoutInflater)
		setContentView(binding.root)

		testAuth()
	}

	private fun testAuth() {
		binding.btnTestLogin.setOnClickListener {
			viewModel.authLogin(AuthRequestBody("21120120130101", "mahasiswa123"))

			viewModel.authResult.observe(this){ authResult ->

				Log.d("Result Auth", authResult.payload?.userData.toString())

			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}