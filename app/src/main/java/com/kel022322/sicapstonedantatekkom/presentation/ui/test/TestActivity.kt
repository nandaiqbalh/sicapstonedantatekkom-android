package com.kel022322.sicapstonedantatekkom.presentation.ui.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request.AuthLogoutRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.ActivityTestBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login.LoginViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.logout.LogoutViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.pengumuman.PengumumanViewModel
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

	private var _binding: ActivityTestBinding? = null
	private val binding get() = _binding!!

	private val viewModel: LoginViewModel by viewModels()
	private val logoutViewModel: LogoutViewModel by viewModels()
	private val pengumumanViewModel: PengumumanViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityTestBinding.inflate(layoutInflater)
		setContentView(binding.root)

		testAPI()
	}

	private fun testAPI() {

		binding.btnTestLogin.setOnClickListener {

			setLoading(true)

			viewModel.authLogin(AuthLoginRequestBody("21120120130101", "mahasiswa123"))

			viewModel.authResult.observe(this) { authResult ->

				when (authResult) {
					is Resource.Loading -> setLoading(true)
					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", authResult.payload?.status.toString())
						Log.d("Result message", authResult.payload?.message.toString())
						Log.d("Exception", authResult.exception?.message.toString())
						Toast.makeText(this@TestActivity, "Result: ${authResult.payload?.message.toString()}", Toast.LENGTH_SHORT).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", authResult.payload?.status.toString())
						Log.d("Result message", authResult.payload?.message.toString())
						Log.d("Result Auth", authResult.payload?.userData.toString())
						Toast.makeText(this@TestActivity, "Result: ${authResult.payload?.message.toString()}", Toast.LENGTH_SHORT).show()

						// Access user_id and api_token from userData
						val userId = authResult.payload?.userData?.userId.toString()
						val apiToken = authResult.payload?.userData?.apiToken

						Log.d("Result Auth", "user_id: $userId, api_token: $apiToken")

						Toast.makeText(this@TestActivity, "Result: ${authResult.payload?.message.toString()}", Toast.LENGTH_SHORT).show()

						viewModel.setStatusAuth(true)
						// Call the function to setApiToken with the obtained apiToken
						viewModel.setUserId(userId)
						viewModel.setApiToken(apiToken ?: "")
					}

					else -> {}

				}
			}
		}

		binding.btnTestLogout.setOnClickListener {
			setLoading(true)

			var userId = ""
			logoutViewModel.getUserId().observe(this){
				userIdd ->

				if (userIdd != null){
					userId = userIdd.toString()
				}

			}

			var apiToken = ""
			logoutViewModel.getApiToken().observe(this){
					apiTokenn ->

				if (apiTokenn != null){
					apiToken = apiTokenn.toString()
				}
			}


			logoutViewModel.authLogout(AuthLogoutRequestBody(userId, apiToken))

			logoutViewModel.logoutResult.observe(this){
				logoutResult ->
				when(logoutResult){
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						setLoading(false)
						Log.d("Result status", logoutResult.payload?.status.toString())
						Log.d("Result message", logoutResult.payload?.message.toString())
						Log.d("Exception", logoutResult.exception?.message.toString())
						Toast.makeText(this@TestActivity, "Result: ${logoutResult.payload?.message.toString()}", Toast.LENGTH_SHORT).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", logoutResult.payload?.status.toString())
						Log.d("Result message", logoutResult.payload?.message.toString())
						Toast.makeText(this@TestActivity, "Result: ${logoutResult.payload?.message.toString()}", Toast.LENGTH_SHORT).show()

						logoutViewModel.setApiToken("")
						logoutViewModel.setUserId("")
						logoutViewModel.setStatusAuth(false)
					}

					else -> {}
				}
			}
		}

		binding.btnTestBroadcast.setOnClickListener {

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
						Toast.makeText(this@TestActivity, "Result: ${broadcastResult.payload?.message.toString()}", Toast.LENGTH_SHORT).show()

					}

					is Resource.Success -> {
						setLoading(false)
						Log.d("Result status", broadcastResult.payload?.status.toString())
						Log.d("Result message", broadcastResult.payload?.message.toString())
						Log.d("Result Auth", broadcastResult.payload?.data.toString())

						Toast.makeText(this@TestActivity, "Result: ${broadcastResult.payload?.message.toString()}", Toast.LENGTH_SHORT).show()
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