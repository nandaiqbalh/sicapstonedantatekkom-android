package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.adapter.FragmentPageAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MahasiswaDokumenFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenBinding? = null
	private val binding get() = _binding!!

	private val profileViewModel: ProfileSayaViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	private var fragmentPageAdapter: FragmentPageAdapter? = null
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaDokumenBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setToolbar()

		setViewPager()
	}

	private fun setViewPager() {

		fragmentPageAdapter = FragmentPageAdapter(requireFragmentManager(), lifecycle)

		with(binding) {
			tabLayoutDokumenFragment.addTab(tabLayoutDokumenFragment.newTab().setText("Capstone"))
			tabLayoutDokumenFragment.addTab(
				tabLayoutDokumenFragment.newTab().setText("Tugas Akhir")
			)

			viewPagerDokumenFragment.adapter = fragmentPageAdapter

			tabLayoutDokumenFragment.addOnTabSelectedListener(object : OnTabSelectedListener {
				override fun onTabSelected(tab: TabLayout.Tab?) {
					viewPagerDokumenFragment.currentItem = tab!!.position

					val textView = tab.view as? TextView
					textView?.apply {
						setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) // Set desired text size
//						setTextColor(resources.getColor(com.kel022322.sicapstonedantatekkom.R.))
						isAllCaps = false // Set text to uppercase
					}
				}

				override fun onTabUnselected(tab: TabLayout.Tab?) {


				}

				override fun onTabReselected(tab: TabLayout.Tab?) {
				}
			})

			viewPagerDokumenFragment.registerOnPageChangeCallback(object :
				ViewPager2.OnPageChangeCallback() {
				override fun onPageSelected(position: Int) {
					super.onPageSelected(position)
					tabLayoutDokumenFragment.selectTab(tabLayoutDokumenFragment.getTabAt(position))
				}
			})


		}
	}

	private fun setToolbar() {
		profileViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			if (username != null) {
				binding.ivNamaUserDokumen.text = username
			}
		}

		profileViewModel.getPhotoProfile().observe(viewLifecycleOwner) { photoProfile ->

			if (photoProfile != null || photoProfile != "") {
				// Decode base64 string to byte array
				val decodedBytes = decodeBase64ToBitmap(photoProfile.toString())

				GlideApp.with(requireContext()).asBitmap().load(decodedBytes)
					.into(binding.ivHomeProfilephotoDokumen)
			}
		}
	}


	private fun decodeBase64ToBitmap(base64: String): Bitmap {
		val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaDokumenFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content),
				message,
				"OK"
			) {
				customSnackbar.dismissSnackbar()
				if (message == "Berhasil keluar!" || message == "Gagal! Anda telah masuk melalui perangkat lain." || message == "Pengguna tidak ditemukan!" || message == "Akses tidak sah!" || message == "Sesi anda telah berakhir, silahkan masuk terlebih dahulu.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				} else if (message == "null" || message.equals(null) || message == "Terjadi kesalahan!") {
					restartFragment()
				} else if (message == "Password berhasil diubah, silahkan masuk kembali.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				}
			}
		}

	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaDokumenFragment

		// Check if the fragment is currently visible
		if (currentFragment.isVisible) {
			// Detach fragment
			val ftDetach = parentFragmentManager.beginTransaction()
			ftDetach.detach(currentFragment)
			ftDetach.commit()

			// Attach fragment
			val ftAttach = parentFragmentManager.beginTransaction()
			ftAttach.attach(currentFragment)
			ftAttach.commit()
		}
	}

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
//			setShimmerVisibility(shimmerBerandaNamauser, isLoading)
//			setShimmerVisibility(shimmerIvHomeProfilephoto, isLoading)
//			setShimmerVisibility(shimmerCvPengumumanTerbaru, isLoading)
//
//			namauser.visibility = if (isLoading) View.GONE else View.VISIBLE
//			ivHomeProfilephoto.visibility = if (isLoading) View.GONE else View.VISIBLE
//			rvPengumumanTerbaru.visibility = if (isLoading) View.GONE else View.VISIBLE

		}
		if (isLoading) {
//			binding.tvPengumumanTidakDitemukan.visibility = View.GONE
		}
	}

	private fun setShimmerVisibility(shimmerView: View, isLoading: Boolean) {
		shimmerView.visibility = if (isLoading) View.VISIBLE else View.GONE
		(shimmerView as? ShimmerFrameLayout)?.run {
			if (isLoading) startShimmer() else stopShimmer()
		}
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}