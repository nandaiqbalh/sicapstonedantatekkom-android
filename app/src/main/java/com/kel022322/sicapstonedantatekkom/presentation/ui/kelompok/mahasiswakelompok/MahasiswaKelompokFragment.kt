package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.FragmentDaftarCapstonePageAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDosbingAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaKelompokFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	private var fragmentPageAdapter: FragmentDaftarCapstonePageAdapter? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaKelompokBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setToolbar()

		setViewPager()

		checkKelompok()

		buttonActionListener()
	}

	private fun buttonActionListener() {

		with(binding) {

			btnSelengkapnyaKelompok.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaKelompokDetailFragment)
			}

		}
	}
	private fun checkKelompok() {
		setLoading(true)

		// get status kelompok
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.getKelompokSaya(apiToken)

			}
		}

		kelompokViewModel.getKelompokSayaResult.observe(viewLifecycleOwner) { getKelompokSayaResult ->
			val resultResponse = getKelompokSayaResult.payload
			val status = resultResponse?.status

			when (getKelompokSayaResult) {
				is Resource.Loading -> {
					setLoading(true)

				}

				is Resource.Error -> {
					setLoading(false)

					showSnackbar(status ?: "Terjadi kesalahan!", true)

					Log.d("Error Kelompok Index", getKelompokSayaResult.payload?.status.toString())

					// set view condition
					with(binding){
						setViewVisibility(cvValueKelompok, false)
						setViewVisibility(cvValueDosbing, false)
						setViewVisibility(cvValueKelompok, false)
						setViewVisibility(linearLayoutDaftarCapstone, false)
						setViewVisibility(cvErrorKelompok, true)

						setViewVisibility(shimmerFragmentKelompok, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())

					if (resultResponse?.success == true && resultResponse.data != null) {

						setCardKelompok(getKelompokSayaResult)

						val dataKelompok = resultResponse.data

						// set conditional value if kelompok mahasiswa already apply for kelompok
						if (dataKelompok.kelompok == null) {

							// (kelompok still null) set conditionally view
							with(binding){
								setViewVisibility(cvValueKelompok, false)
								setViewVisibility(cvValueDosbing, false)
								setViewVisibility(linearLayoutDaftarCapstone, true)
								setViewVisibility(cvErrorKelompok, false)

								setViewVisibility(shimmerFragmentKelompok, false)

							}

						} else {
							// if already have kelompok
							with(binding){
								setViewVisibility(cvValueKelompok, true)
								setViewVisibility(cvValueDosbing, true)
								setViewVisibility(linearLayoutDaftarCapstone, false)
								setViewVisibility(cvErrorKelompok, false)

								setViewVisibility(shimmerFragmentKelompok, false)

							}

						}
					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(", false)

							actionIfLogoutSucces()
						} else {
							showSnackbar(status ?: "Terjadi kesalahan!", false)

						}
					}

				}

				else -> {}
			}
		}
	}

	private fun setToolbar() {
		setLoading(true)

		// set username
		kelompokViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			setLoading(false)

			if (username != null && username != "") {
				binding.tvNamaUserKelompok.text = username
			}
		}

		// set photo profile
		kelompokViewModel.getPhotoProfile().observe(viewLifecycleOwner) { photoProfile ->
			setLoading(false)

			if (photoProfile != null && photoProfile != "") {
				GlideApp.with(this@MahasiswaKelompokFragment).asBitmap().load(photoProfile)
					.into(binding.ivHomeProfilephotoKelompok)
			}
		}

	}

	private fun setCardKelompok(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		val data = getKelompokSayaResult.payload?.data

		if (data?.kelompok?.idKelompok != null) {

			//  kelompok sudah valid
			with(binding) {

				val dataKelompok = data.kelompok
				// card kelompok
				tvValueStatusKelompok.text = dataKelompok.statusKelompok
				tvValueNomorKelompok.text = dataKelompok.nomorKelompok.toString()
				tvValueTopik.text = dataKelompok.namaTopik
				tvValueJudul.text = dataKelompok.judulCapstone

				val dataDosbing = data.rsDosbing
				// card dosbing
				val akunDosbingAdapter = AkunDosbingAdapter()

				akunDosbingAdapter.setList(dataDosbing)

				binding.rvAkunDosbingKelompok.layoutManager = LinearLayoutManager(
					requireContext(),
					LinearLayoutManager.VERTICAL,
					false
				)
				rvAkunDosbingKelompok.adapter = akunDosbingAdapter

				// navigate to detail if necessary
				akunDosbingAdapter.setOnItemClickCallback(object :
					AkunDosbingAdapter.OnItemClickCallBack {
					override fun onItemClicked(dosbingId: String) {
					}
				})
			}

		} else {
			//  kelompok belum valid
			with(binding) {
				// card kelompok
				"Belum valid!".also { tvValueStatusKelompok.text = it }

				btnSelengkapnyaKelompok.visibility = View.GONE

				tvErrorKelompokDosbingEmpty.visibility = View.VISIBLE
			}

		}

	}


	private fun setViewPager() {

		fragmentPageAdapter = FragmentDaftarCapstonePageAdapter(childFragmentManager, lifecycle)

		with(binding) {
			tabLayoutDaftarCapstone.addTab(tabLayoutDaftarCapstone.newTab().setText("Individu"))
			tabLayoutDaftarCapstone.addTab(
				tabLayoutDaftarCapstone.newTab().setText("Kelompok")
			)

			viewPagerDaftarCapstone.adapter = fragmentPageAdapter

			tabLayoutDaftarCapstone.addOnTabSelectedListener(object :
				TabLayout.OnTabSelectedListener {
				override fun onTabSelected(tab: TabLayout.Tab?) {
					viewPagerDaftarCapstone.currentItem = tab!!.position

				}

				override fun onTabUnselected(tab: TabLayout.Tab?) {


				}

				override fun onTabReselected(tab: TabLayout.Tab?) {
				}
			})

			viewPagerDaftarCapstone.registerOnPageChangeCallback(object :
				ViewPager2.OnPageChangeCallback() {
				override fun onPageSelected(position: Int) {
					super.onPageSelected(position)
					tabLayoutDaftarCapstone.selectTab(tabLayoutDaftarCapstone.getTabAt(position))
				}
			})


		}
	}


	private fun showSnackbar(message: String, isRestart: Boolean) {
		val currentFragment = this@MahasiswaKelompokFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), message, "OK"
			) {
				customSnackbar.dismissSnackbar()
				if (isRestart) {
					restartFragment()
				}
			}
		}
	}
	private fun restartFragment() {
		val currentFragment = this@MahasiswaKelompokFragment

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


	private fun actionIfLogoutSucces() {
		// set auth data store
		kelompokViewModel.setApiToken("")
		kelompokViewModel.setUserId("")
		kelompokViewModel.setUsername("")
		kelompokViewModel.setStatusAuth(false)

		val intent = Intent(requireContext(), SplashscreenActivity::class.java)
		requireContext().startActivity(intent)
		requireActivity().finishAffinity()
	}

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerBerandaNamauser, isLoading)
			setShimmerVisibility(shimmerIvHomeProfilephoto, isLoading)
			setShimmerVisibility(shimmerFragmentKelompok, isLoading)

			tvNamaUserKelompok.visibility = if (isLoading) View.GONE else View.VISIBLE
			ivHomeProfilephotoKelompok.visibility = if (isLoading) View.GONE else View.VISIBLE

			if (isLoading) {
				cvValueKelompok.visibility = View.GONE
				cvValueDosbing.visibility = View.GONE
				cvErrorKelompok.visibility = View.GONE
				linearLayoutDaftarCapstone.visibility = View.GONE
			}
		}


	}

	private fun setShimmerVisibility(shimmerView: View, isLoading: Boolean) {
		shimmerView.visibility = if (isLoading) View.VISIBLE else View.GONE
		(shimmerView as? ShimmerFrameLayout)?.run {
			if (isLoading) startShimmer() else stopShimmer()
		}
	}

	private fun setViewVisibility(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.GONE
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}