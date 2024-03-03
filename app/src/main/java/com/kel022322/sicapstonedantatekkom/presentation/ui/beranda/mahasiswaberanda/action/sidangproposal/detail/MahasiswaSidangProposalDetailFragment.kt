package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangproposal.detail

import android.annotation.SuppressLint
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
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response.SidangProposalByKelompokResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaSidangProposalDetailBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangproposal.SidangProposalViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.KelompokIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunAnggotaKelompokAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDosbingAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDospengAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaSidangProposalDetailFragment : Fragment() {

	private var _binding: FragmentMahasiswaSidangProposalDetailBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()
	private val sidangProposalViewModel: SidangProposalViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaSidangProposalDetailBinding.inflate(layoutInflater, container, false)
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
	@SuppressLint("SetTextI18n")
	private fun setCardSidangProposal(getSidangProposalByKelompokResult: Resource<SidangProposalByKelompokResponse>) {

		val data = getSidangProposalByKelompokResult.payload?.data

		if (data != null) {

			//  kelompok sudah valid
			with(binding) {

				val dataKelompok = data.kelompok
				// card kelompok
				tvValueStatusKelompok.text = data.statusKelompok
				tvValueRuangSidang.text = data.namaRuang
				tvValueHariSidang.text = "${data.hariSidang}, ${data.tanggalSidang}"
				tvValueWaktuSidang.text = "${data.waktuSidang} WIB"

				tvValueJudul.text = dataKelompok.judulCapstone


			}

		} else {
			//  kelompok belum valid
			with(binding) {
				// card kelompok
				"Belum valid!".also { tvValueStatusKelompok.text = it }

			}

		}

	}


	private fun setCardKelompok(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		val data = getKelompokSayaResult.payload?.data

		//  kelompok sudah valid
		with(binding) {

			// card anggota kelompok
			val dataAnggotaKelompok = data?.rsMahasiswa
			val akunAnggotaKelompokAdapter = AkunAnggotaKelompokAdapter()

			akunAnggotaKelompokAdapter.setList(dataAnggotaKelompok)

			rvAkunAnggotaSidangProposalSidangProposalDetail.layoutManager = LinearLayoutManager(
				requireContext(),
				LinearLayoutManager.VERTICAL,
				false
			)
			rvAkunAnggotaSidangProposalSidangProposalDetail.adapter = akunAnggotaKelompokAdapter

			// navigate to detail if necessary
			akunAnggotaKelompokAdapter.setOnItemClickCallback(object :
				AkunAnggotaKelompokAdapter.OnItemClickCallBack {
				override fun onItemClicked(mahasiswaId: String) {
				}
			})

			// dosbing
			val dataDosbing = data?.rsDosbing
			// card dosbing
			val akunDosbingAdapter = AkunDosbingAdapter()

			akunDosbingAdapter.setList(dataDosbing)

			rvAkunDosbingSidangProposalDetail.layoutManager = LinearLayoutManager(
				requireContext(),
				LinearLayoutManager.VERTICAL,
				false
			)
			rvAkunDosbingSidangProposalDetail.adapter = akunDosbingAdapter

			// navigate to detail if necessary
			akunDosbingAdapter.setOnItemClickCallback(object :
				AkunDosbingAdapter.OnItemClickCallBack {
				override fun onItemClicked(dosbingId: String) {
				}
			})

			// dospeng
			val dataDospeng = data?.rsDospeng
			// card Dospeng
			val akunDospengAdapter = AkunDospengAdapter()

			akunDospengAdapter.setList(dataDospeng)

			rvAkunDospengSidangProposalDetail.layoutManager = LinearLayoutManager(
				requireContext(),
				LinearLayoutManager.VERTICAL,
				false
			)
			rvAkunDospengSidangProposalDetail.adapter = akunDospengAdapter

			// navigate to detail if necessary
			akunDospengAdapter.setOnItemClickCallback(object :
				AkunDospengAdapter.OnItemClickCallBack {
				override fun onItemClicked(dospengId: String) {
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
				sidangProposalViewModel.getSidangProposalByKelompok(apiToken)
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
						setViewVisibility(cvValueSidangProposalDetail, false)
						setViewVisibility(cvValueAnggotaSidangProposalDetail, false)
						setViewVisibility(cvValueDosbingDetail, false)
						setViewVisibility(cvValueDospengDetail, false)
						setViewVisibility(linearLayoutSidangProposal, false)
						setViewVisibility(cvErrorSidangProposalDetail, true)

						setViewVisibility(shimmerFragmentSidangProposalDetail, false)

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
							setViewVisibility(cvValueSidangProposalDetail, true)
							setViewVisibility(cvValueAnggotaSidangProposalDetail, true)
							setViewVisibility(cvValueDosbingDetail, true)
							setViewVisibility(cvValueDospengDetail, true)
							setViewVisibility(linearLayoutSidangProposal, true)
							setViewVisibility(cvErrorSidangProposalDetail, false)

							setViewVisibility(shimmerFragmentSidangProposalDetail, false)

						}
					} else {
						Log.d("Succes, but failed", status.toString())

						with(binding){
							setViewVisibility(cvValueSidangProposalDetail, false)
							setViewVisibility(cvValueAnggotaSidangProposalDetail, false)
							setViewVisibility(cvValueDosbingDetail, false)
							setViewVisibility(cvValueDospengDetail, false)
							setViewVisibility(linearLayoutSidangProposal, false)
							setViewVisibility(cvErrorSidangProposalDetail, true)

							setViewVisibility(shimmerFragmentSidangProposalDetail, false)

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

		sidangProposalViewModel.getSidangProposalByKelompokResult.observe(viewLifecycleOwner) { getSidangProposalByKelompokResult ->
			val resultResponse = getSidangProposalByKelompokResult.payload
			val status = resultResponse?.status

			when (getSidangProposalByKelompokResult) {
				is Resource.Loading -> {
					setLoading(true)

				}

				is Resource.Error -> {
					setLoading(false)

					showSnackbar(status ?: "Terjadi kesalahan!", true)

					Log.d(
						"Error Kelompok Index",
						getSidangProposalByKelompokResult.payload?.status.toString()
					)

				}

				is Resource.Success -> {
					setLoading(false)

					val message = getSidangProposalByKelompokResult.payload
					Log.d("Result success", message.toString())

					if (resultResponse?.success == true && resultResponse.data != null) {

						setCardSidangProposal(getSidangProposalByKelompokResult)

					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(", true)

							actionIfLogoutSucces()
						} else {
							showSnackbar(status ?: "Terjadi kesalahan!", true)
						}
					}

				}

				else -> {}
			}
		}

	}

	private fun showSnackbar(message: String, isRestart: Boolean) {
		val currentFragment = this@MahasiswaSidangProposalDetailFragment
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
		val currentFragment = this@MahasiswaSidangProposalDetailFragment

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
			setShimmerVisibility(shimmerFragmentSidangProposalDetail, isLoading)
			if (isLoading) {
				setViewVisibility(cvValueSidangProposalDetail, false)
				setViewVisibility(cvValueAnggotaSidangProposalDetail, false)
				setViewVisibility(cvValueDosbingDetail, false)
				setViewVisibility(cvValueDospengDetail, false)
				setViewVisibility(linearLayoutSidangProposal, false)
				setViewVisibility(cvErrorSidangProposalDetail, false)

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