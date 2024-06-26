package com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kel022322.sicapstonedantatekkom.databinding.ActivitySplashscreenBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login.LoginActivity
import com.kel022322.sicapstonedantatekkom.presentation.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashscreenActivity : AppCompatActivity() {

	private var _binding: ActivitySplashscreenBinding? = null
	private val binding get() = _binding!!

	private val splashscreenViewModel: SplashscreenViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		requestWindowFeature(Window.FEATURE_NO_TITLE)
		window.setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN)

		_binding = ActivitySplashscreenBinding.inflate(layoutInflater)
		setContentView(binding.root)

		doSplashscreen()
	}

	private fun doSplashscreen() {
		splashscreenViewModel.getStatusAuth().observe(this) { statusOnboarding ->
			Handler().postDelayed({
				if (statusOnboarding == true) {
					val intent = Intent(this@SplashscreenActivity, MainActivity::class.java)
					startActivity(intent)
					finish()
				} else {
					val intent = Intent(this@SplashscreenActivity, LoginActivity::class.java)
					startActivity(intent)
					finish()
				}
			}, 3000)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}