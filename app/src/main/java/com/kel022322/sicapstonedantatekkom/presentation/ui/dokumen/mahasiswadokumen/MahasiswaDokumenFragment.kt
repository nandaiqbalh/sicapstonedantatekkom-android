package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.request.PhotoProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.adapter.FragmentPageAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
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

		fragmentPageAdapter = FragmentPageAdapter(childFragmentManager, lifecycle)

		with(binding) {
			tabLayoutDokumenFragment.addTab(tabLayoutDokumenFragment.newTab().setText("Capstone"))
			tabLayoutDokumenFragment.addTab(
				tabLayoutDokumenFragment.newTab().setText("Tugas Akhir")
			)

			viewPagerDokumenFragment.adapter = fragmentPageAdapter

			tabLayoutDokumenFragment.addOnTabSelectedListener(object : OnTabSelectedListener {
				override fun onTabSelected(tab: TabLayout.Tab?) {
					viewPagerDokumenFragment.currentItem = tab!!.position

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
				binding.tvNamaUserDokumen.text = username
			}
		}

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						profileViewModel.getPhotoProfile(
							PhotoProfileRemoteRequestBody(
								userId.toString(), it
							)
						)
					}
				}
			}
		}

		profileViewModel.getPhotoProfileResult.observe(viewLifecycleOwner) { getPhotoProfileResult ->
			when (getPhotoProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					val message = getPhotoProfileResult.payload?.message

					showSnackbar(message = message ?: "Terjadi kesalahan!")
				}

				is Resource.Success -> {
					setLoading(false)
					val message = getPhotoProfileResult.payload?.message

					Log.d("Success message", message.toString())

					if (getPhotoProfileResult.payload?.data != null) {
						// set binding
						with(binding) {
							val base64Image = getPhotoProfileResult.payload.data.toString()

							profileViewModel.setPhotoProfile(base64Image)

							if (base64Image != "null") {
								// Decode base64 string to byte array
								val decodedBytes = decodeBase64ToBitmap(base64Image)

								GlideApp.with(requireContext()).asBitmap().load(decodedBytes)
									.into(ivHomeProfilephotoDokumen)
							}

						}
					}
				}

				else -> {}
			}
		}
	}


	private fun decodeBase64ToBitmap(base64: String): Bitmap {
		val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaDokumenFragment

		if (currentFragment.isVisible){
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
			setShimmerVisibility(shimmerBerandaNamauser, isLoading)
			setShimmerVisibility(shimmerIvHomeProfilephoto, isLoading)

			tvNamaUserDokumen.visibility = if (isLoading) View.GONE else View.VISIBLE
			ivHomeProfilephotoDokumen.visibility = if (isLoading) View.GONE else View.VISIBLE

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