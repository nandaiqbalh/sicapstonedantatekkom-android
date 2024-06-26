package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.adapter.FragmentPageAdapter
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MahasiswaDokumenFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()

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

		setButtonListener()
	}

	private fun setButtonListener() {
		with(binding) {
			ivHomeProfilephotoDokumen.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaProfilFragment)
			}
		}
	}


	private fun setViewPager() {

		fragmentPageAdapter = FragmentPageAdapter(childFragmentManager, lifecycle)

		with(binding) {
			tabLayoutDokumenFragment.addTab(
				tabLayoutDokumenFragment.newTab().setText("Capstone")
			)
			tabLayoutDokumenFragment.addTab(
				tabLayoutDokumenFragment.newTab().setText("Tugas Akhir")
			)

			viewPagerDokumenFragment.adapter = fragmentPageAdapter

			tabLayoutDokumenFragment.addOnTabSelectedListener(object :
				OnTabSelectedListener {
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
					tabLayoutDokumenFragment.selectTab(
						tabLayoutDokumenFragment.getTabAt(
							position
						)
					)
				}
			})


		}
	}

	private fun setToolbar() {
		setLoading(true)

		// set username
		userViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			setLoading(false)

			if (username != null && username != "") {
				binding.tvNamaUserDokumen.text = username
			}
		}

		// set photo profile
		userViewModel.getPhotoProfile().observe(viewLifecycleOwner) { photoProfile ->
			setLoading(false)

			if (photoProfile != null && photoProfile != "") {
				GlideApp.with(this@MahasiswaDokumenFragment).asBitmap().load(photoProfile)
					.into(binding.ivHomeProfilephotoDokumen)
			}
		}

	}

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDokumenNamauser, isLoading)
			setShimmerVisibility(shimmerIvDokumenProfilephoto, isLoading)

			tvNamaUserDokumen.visibility = if (isLoading) View.GONE else View.VISIBLE
			ivHomeProfilephotoDokumen.visibility =
				if (isLoading) View.GONE else View.VISIBLE

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