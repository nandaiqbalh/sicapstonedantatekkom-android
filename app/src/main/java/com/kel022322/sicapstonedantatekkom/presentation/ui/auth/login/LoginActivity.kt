package com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.ActivityLoginBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.MainActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.CustomToast
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

	private var _binding: ActivityLoginBinding? = null
	private val binding get() = _binding!!

	private val loginViewModel: LoginViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()
	private val customToast = CustomToast()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		requestWindowFeature(Window.FEATURE_NO_TITLE)
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN)

		_binding = ActivityLoginBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// check apakah user sudah login
		isAlreadyLogin()

		// try to login
		buttonListener()

	}

	private fun buttonListener() {
		binding.btnLogin.setOnClickListener {

			if (validateForm()) {

				setLoading(true)

				val enteredIdPengguna = binding.edtIdPengguna.text.toString().trim()
				val enteredPassword = binding.edtPassword.text.toString().trim()

				loginViewModel.authLogin(
					AuthLoginRequestBody(
						nomorInduk = enteredIdPengguna,
						password = enteredPassword
					)
				)

				loginViewModel.authResult.observe(this) { authResult ->

					when (authResult) {
						is Resource.Loading -> setLoading(true)
						is Resource.Error -> {
							setLoading(false)
							Log.d("Result message", authResult.payload?.message.toString())

							customSnackbar.showSnackbarWithAction(findViewById(android.R.id.content),
								authResult.payload?.message ?: "Terjadi kesalahan!",
								"OK"
							) {
								customSnackbar.dismissSnackbar()
							}
						}

						is Resource.Success -> {
							setLoading(false)
							Log.d("Result message", authResult.payload?.message.toString())

							customSnackbar.showSnackbarWithAction(findViewById(android.R.id.content),
								authResult.payload?.message ?: "Autentikasi berhasil!",
								 "OK"
							) {
								customSnackbar.dismissSnackbar()
							}

							if (authResult.payload!!.status == true) {
								val userId = authResult.payload.userData?.userId.toString()
								val apiToken = authResult.payload.userData?.apiToken
								val username = authResult.payload.userData?.userName

								Log.d("Result Auth", "user_id: $userId, api_token: $apiToken")

								loginViewModel.setStatusAuth(true)
								// Call the function to setApiToken with the obtained apiToken
								loginViewModel.setUserId(userId)
								loginViewModel.setApiToken(apiToken ?: "")
								loginViewModel.setUsername(username ?: "Nama Lengkap Mahasiswa")

							}
						}

						else -> {}

					}
				}
			}
		}
	}

	private fun isAlreadyLogin() {
		loginViewModel.getStatusAuth().observe(this@LoginActivity) { isOnboardingCompleted ->
			if (isOnboardingCompleted == true) {
				val intent = Intent(this@LoginActivity, MainActivity::class.java)
				startActivity(intent)
				finishAffinity()
			}

		}
	}

	// validasi form
	private fun validateForm(): Boolean {
		val idPengguna = binding.edtIdPengguna.text.toString()
		val password = binding.edtPassword.text.toString()

		var isFormValid = true

		if (idPengguna.isEmpty()) {
			isFormValid = false
			binding.tilIdPengguna.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilIdPengguna.error = null
		}

		if (password.isEmpty()) {
			isFormValid = false
			binding.tilPassword.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilPassword.error = null
		}

		return isFormValid
	}

	private fun setLoading(isLoading: Boolean) {
		if (isLoading) {
			binding.pbLogin.visibility = View.VISIBLE
			binding.overlayLayout.visibility = View.VISIBLE
			window.setFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			)
		} else {
			binding.pbLogin.visibility = View.GONE
			binding.overlayLayout.visibility = View.GONE
			window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
		}
	}


	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}