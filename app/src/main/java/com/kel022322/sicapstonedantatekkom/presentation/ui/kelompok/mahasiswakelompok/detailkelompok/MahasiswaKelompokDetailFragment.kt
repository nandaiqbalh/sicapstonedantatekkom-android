package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.detailkelompok

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
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokDetailBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.KelompokIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunAnggotaKelompokAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDosbingAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaKelompokDetailFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokDetailBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaKelompokDetailBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		checkKelompok()

		setActionListener()
	}

	// set button action listener
	private fun setActionListener() {

		with(binding) {

			ivCircleBackArrow.setOnClickListener {
				findNavController().popBackStack()
			}

			icBackArrow.setOnClickListener {
				findNavController().popBackStack()
			}

		}
	}

	private fun setCardKelompok(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		val data = getKelompokSayaResult.payload?.data

		//  kelompok sudah valid
		with(binding) {
			val dataKelompok = data?.kelompok
			// card kelompok
			tvValueStatusKelompokDetail.text = dataKelompok?.statusKelompok
			tvValueNomorKelompokDetail.text = dataKelompok?.nomorKelompok.toString()
			tvValueTopikDetail.text = dataKelompok?.namaTopik
			tvValueJudulDetail.text = dataKelompok?.judulCapstone

			// card anggota kelompok
			val dataAnggotaKelompok = data?.rsMahasiswa
			// card dosbing
			val akunAnggotaKelompokAdapter = AkunAnggotaKelompokAdapter()

			akunAnggotaKelompokAdapter.setList(dataAnggotaKelompok)

			rvAkunAnggotaKelompokKelompokDetail.layoutManager = LinearLayoutManager(
				requireContext(),
				LinearLayoutManager.VERTICAL,
				false
			)
			rvAkunAnggotaKelompokKelompokDetail.adapter = akunAnggotaKelompokAdapter

			// navigate to detail if necessary
			akunAnggotaKelompokAdapter.setOnItemClickCallback(object :
				AkunAnggotaKelompokAdapter.OnItemClickCallBack {
				override fun onItemClicked(mahasiswaId: String) {
				}
			})

			val dataDosbing = data?.rsDosbing
			// card dosbing
			val akunDosbingAdapter = AkunDosbingAdapter()

			akunDosbingAdapter.setList(dataDosbing)

			rvAkunDosbingKelompokDetail.layoutManager = LinearLayoutManager(
				requireContext(),
				LinearLayoutManager.VERTICAL,
				false
			)
			rvAkunDosbingKelompokDetail.adapter = akunDosbingAdapter

			// navigate to detail if necessary
			akunDosbingAdapter.setOnItemClickCallback(object :
				AkunDosbingAdapter.OnItemClickCallBack {
				override fun onItemClicked(dosbingId: String) {
				}
			})
		}


	}

	private fun checkKelompok() {
		setLoading(true)

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.getKelompokSaya(
					apiToken
				)
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

					Log.d("Error Kelompok Detail", getKelompokSayaResult.payload?.status.toString())

					// set view condition
					with(binding) {
						setViewVisibility(cvValueKelompokDetail, false)
						setViewVisibility(cvValueDosbingDetail, false)
						setViewVisibility(cvValueAnggotaKelompokDetail, false)
						setViewVisibility(cvErrorKelompokDetail, true)

						setViewVisibility(shimmerFragmentKelompokDetail, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())
					if (resultResponse?.success == true && resultResponse.data != null) {
						setCardKelompok(getKelompokSayaResult)
						// if already have kelompok
						with(binding) {
							setViewVisibility(cvValueKelompokDetail, true)
							setViewVisibility(cvValueDosbingDetail, true)
							setViewVisibility(cvValueAnggotaKelompokDetail, true)
							setViewVisibility(cvErrorKelompokDetail, false)

							setViewVisibility(shimmerFragmentKelompokDetail, false)
						}
					} else {
						Log.d("Succes, but failed", status.toString())

						with(binding){
							setViewVisibility(cvValueKelompokDetail, false)
							setViewVisibility(cvValueDosbingDetail, false)
							setViewVisibility(cvValueAnggotaKelompokDetail, false)
							setViewVisibility(cvErrorKelompokDetail, true)

							setViewVisibility(shimmerFragmentKelompokDetail, false)
						}

						if (status == "Authorization Token not found" ||  status == "Token is Expired" || status == "Token is Invalid") {
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

	private fun showSnackbar(message: String, isRestart: Boolean) {
		val currentFragment = this@MahasiswaKelompokDetailFragment
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
		val currentFragment = this@MahasiswaKelompokDetailFragment

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
		userViewModel.setApiToken("")
		userViewModel.setUserId("")
		userViewModel.setUsername("")
		userViewModel.setStatusAuth(false)

		val intent = Intent(requireContext(), SplashscreenActivity::class.java)
		requireContext().startActivity(intent)
		requireActivity().finishAffinity()
	}

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerFragmentKelompokDetail, isLoading)
			if (isLoading) {
				cvValueKelompokDetail.visibility = View.GONE
				cvValueAnggotaKelompokDetail.visibility = View.GONE
				cvValueDosbingDetail.visibility = View.GONE
				cvErrorKelompokDetail.visibility = View.GONE

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