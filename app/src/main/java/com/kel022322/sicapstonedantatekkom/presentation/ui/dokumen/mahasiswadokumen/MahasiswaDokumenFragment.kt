package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.adapter.FragmentPageAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
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


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}