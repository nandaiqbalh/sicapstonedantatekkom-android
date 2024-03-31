package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangproposal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response.SidangProposalByKelompokResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaSidangProposalBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaSidangProposalFragment : Fragment() {


	private var _binding: FragmentMahasiswaSidangProposalBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val sidangProposalViewModel: SidangProposalViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaSidangProposalBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setButtonListener()

		getSidangByKelompok()
	}

	private fun setButtonListener() {

		binding.ivCircleBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		binding.icBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	@SuppressLint("SetTextI18n")
	private fun getSidangByKelompok() {
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let { sidangProposalViewModel.getSidangProposalByKelompok(it) }
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

					Log.d(
						"Error proposal",
						getSidangProposalByKelompokResult.payload?.status.toString()
					)

					// set view condition
					with(binding) {
						setViewVisibility(cvValueSidangProposal, false)
						setViewVisibility(linearLayoutSidangProposal, false)
						setViewVisibility(cvErrorSidangProposal, true)

						setViewVisibility(shimmerFragmentSidangProposal, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getSidangProposalByKelompokResult.payload
					Log.d("Result proposal", message.toString())

					val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
					val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
					val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

					with(binding){
						tvValueStatusKelompok.text = resultResponse?.data?.kelompok?.statusKelompok ?: "Belum Mendaftar Capstone!"
						tvValueStatusKelompok.setTextColor(colorRed)

						when (resultResponse?.data?.kelompok?.statusKelompok) {
							"Menunggu Penetapan Kelompok!",
							"Menunggu Penetapan Dosbing!",
							"Menunggu Persetujuan Anggota!",
							"Menunggu Persetujuan Dosbing!",
							"Menunggu Persetujuan Penguji!",
							"Menunggu Validasi Kelompok!",
							"Menunggu Validasi Expo!" -> {
								tvValueStatusKelompok.setTextColor(colorOrange)
							}
							"Validasi Kelompok Berhasil!",
							"C100 Telah Disetujui!",
							"Penguji Proposal Ditetapkan!",
							"Dijadwalkan Sidang Proposal!",
							"C200 Telah Disetujui!",
							"C300 Telah Disetujui!",
							"C400 Telah Disetujui!",
							"C500 Telah Disetujui!",
							"Validasi Expo Berhasil!",
							"Lulus Expo Project!",
							"Lulus Capstone Project!" -> {
								tvValueStatusKelompok.setTextColor(colorGreen)
							}
							else -> {
								tvValueStatusKelompok.setTextColor(colorRed)
							}
						}
					}
					if (resultResponse?.success == true) {

						setCardSidangProposal(getSidangProposalByKelompokResult)

						// if already have kelompok
						with(binding) {
							setViewVisibility(cvValueSidangProposal, true)
							setViewVisibility(linearLayoutSidangProposal, true)
							setViewVisibility(cvErrorSidangProposal, false)

							setViewVisibility(shimmerFragmentSidangProposal, false)
						}

					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {

							actionIfLogoutSucces()
						} else {
							setViewVisibility(binding.cvErrorSidangProposal, true)
							binding.tvErrorSidangProposal.text = status ?: "Mohon periksa kembali koneksi internet Anda!"
						}

					}

				}

				else -> {}
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
				tvValueStatusKelompok.text = data.statusKelompok ?: "-"
				tvValueRuangSidang.text = data.namaRuang ?: "-"
				tvValueHariSidang.text = "${data.hariSidang}, ${data.tanggalSidang}"
				tvValueWaktuSidang.text = "${data.waktuSidang} WIB"

				tvValueJudul.text = dataKelompok?.judulCapstone ?: "-"

				btnSelengkapnyaSidangProposal.setOnClickListener {
					findNavController().navigate(R.id.action_mahasiswaSidangProposalFragment_to_mahasiswaSidangProposalDetailFragment)
				}
			}

		} else {
			//  kelompok belum valid
			with(binding) {
				// card kelompok
				"Belum valid!".also { tvValueStatusKelompok.text = it }

				btnSelengkapnyaSidangProposal.visibility = View.GONE

			}

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

	private fun setViewVisibility(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.GONE
	}

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerFragmentSidangProposal, isLoading)

			if (isLoading) {
				cvValueSidangProposal.visibility = View.GONE
				cvErrorSidangProposal.visibility = View.GONE
				linearLayoutSidangProposal.visibility = View.GONE

			}
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