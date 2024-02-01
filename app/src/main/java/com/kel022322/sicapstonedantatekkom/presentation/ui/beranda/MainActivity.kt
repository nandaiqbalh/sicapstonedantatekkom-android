package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var navController: NavController
	private lateinit var bottomNav: BottomNavigationView

	private var _binding: ActivityMainBinding? = null
	private val binding get() = _binding!!

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		supportActionBar?.hide()

		val navHostFragment = supportFragmentManager
			.findFragmentById(R.id.fragmentContainerViewHome) as NavHostFragment

		navController = navHostFragment.navController

		bottomNav = binding.bottomNavigationUser

		val appBarConfiguration = AppBarConfiguration.Builder(
			R.id.mahasiswaBerandaFragment,
			R.id.mahasiswaKelompokFragment,
			R.id.mahasiswaDokumenFragment,
			R.id.mahasiswaProfilFragment
		).build()

		bottomNav.itemIconTintList = null

		setupActionBarWithNavController(navController, appBarConfiguration)
		bottomNav.setupWithNavController(navController)

		navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mahasiswaPengumumanFragment -> {
                    hideBottomNav(true)
                }
	            R.id.mahasiswaSidangProposalFragment -> {
		            hideBottomNav(true)
	            }
	            R.id.mahasiswaExpoFragment -> {
		            hideBottomNav(true)
	            }
	            R.id.mahasiswaSidangTugasAkhirFragment -> {
		            hideBottomNav(true)
	            }
	            R.id.mahasiswaDetailPengumumanFragment -> {
		            hideBottomNav(true)
	            }
                else -> hideBottomNav(false)
            }
		}
	}

	private fun hideBottomNav(hide: Boolean) {
		if (hide) {
			binding.bottomNavigationUser.visibility = View.GONE
		} else {
			binding.bottomNavigationUser.visibility = View.VISIBLE
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

}