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

					val data = resultResponse?.data
					// Kemudian dalam bagian pengaturan warna teks
					with(binding) {
						tvValueStatusKelompok.text = data?.kelompok?.statusSidangProposal ?: "Belum dijadwalkan sidang proposal!"

						when (data?.kelompok?.statusSidangProposal) {
							in listOf(
								"Dosbing Tidak Setuju!",
								"Penguji Tidak Setuju!",
								"C100 Tidak Disetujui Dosbing 1!",
								"C100 Tidak Disetujui Dosbing 2!",
								"C200 Tidak Disetujui Dosbing 1!",
								"C200 Tidak Disetujui Dosbing 2!",
								"C300 Tidak Disetujui Dosbing 1!",
								"C300 Tidak Disetujui Dosbing 2!",
								"C400 Tidak Disetujui Dosbing 1!",
								"C400 Tidak Disetujui Dosbing 2!",
								"C500 Tidak Disetujui Dosbing 1!",
								"C500 Tidak Disetujui Dosbing 2!",
								"Laporan TA Tidak Disetujui Dosbing 1!",
								"Laporan TA Tidak Disetujui Dosbing 2!",
								"Makalah TA Tidak Disetujui Dosbing 1!",
								"Makalah TA Tidak Disetujui Dosbing 2!",
								"Kelompok Tidak Disetujui Expo!",
								"Laporan TA Tidak Disetujui!",
								"Makalah TA Tidak Disetujui!",
								"Belum Mendaftar Sidang TA!",
								"Gagal Expo Project!"
							) -> {
								tvValueStatusKelompok.setTextColor(colorRed)
							}
							in listOf(
								"Menunggu Penetapan Kelompok!",
								"Menunggu Persetujuan Dosbing!",
								"C100 Menunggu Persetujuan Dosbing 1!",
								"C100 Menunggu Persetujuan Dosbing 2!",
								"C200 Menunggu Persetujuan Dosbing 1!",
								"C200 Menunggu Persetujuan Dosbing 2!",
								"C300 Menunggu Persetujuan Dosbing 1!",
								"C300 Menunggu Persetujuan Dosbing 2!",
								"C400 Menunggu Persetujuan Dosbing 1!",
								"C400 Menunggu Persetujuan Dosbing 2!",
								"C500 Menunggu Persetujuan Dosbing 1!",
								"C500 Menunggu Persetujuan Dosbing 2!",
								"Laporan TA Menunggu Persetujuan Dosbing 1!",
								"Laporan TA Menunggu Persetujuan Dosbing 2!",
								"Makalah TA Menunggu Persetujuan Dosbing 1!",
								"Makalah TA Menunggu Persetujuan Dosbing 2!",
								"Menunggu Persetujuan Anggota!",
								"Didaftarkan!",
								"Menunggu Penetapan Dosbing!",
								"Menunggu Persetujuan Tim Capstone!",
								"Menunggu Persetujuan C100!",
								"Menunggu Persetujuan C200!",
								"Menunggu Persetujuan C300!",
								"Menunggu Persetujuan C400!",
								"Menunggu Persetujuan C500!",
								"Menunggu Persetujuan Expo!",
								"Menunggu Persetujuan Laporan TA!",
								"Menunggu Persetujuan Makalah TA!",
								"Menunggu Persetujuan Penguji!",
								"Menunggu Persetujuan Pembimbing!",
								"Menunggu Penjadwalan Sidang TA!"
							) -> {
								tvValueStatusKelompok.setTextColor(colorOrange)
							}
							in listOf(
								"Menyetujui Kelompok!",
								"Dosbing Setuju!",
								"Kelompok Diplot Tim Capstone!",
								"Dosbing Diplot Tim Capstone!",
								"Kelompok Telah Disetujui!",
								"C100 Telah Disetujui!",
								"Penguji Proposal Ditetapkan!",
								"Pembimbing Setuju!",
								"Penguji Setuju!",
								"Dijadwalkan Sidang Proposal!",
								"Lulus Sidang Proposal!",
								"C200 Telah Disetujui!",
								"C300 Telah Disetujui!",
								"C400 Telah Disetujui!",
								"C500 Telah Disetujui!",
								"Kelompok Disetujui Expo!",
								"Lulus Expo Project!",
								"Laporan TA Telah Disetujui!",
								"Makalah TA Telah Disetujui!",
								"Penguji TA Setuju!",
								"Telah Dijadwalkan Sidang TA!",
								"Lulus Sidang TA!"
							) -> {
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
				tvValueStatusKelompok.text = data.kelompok?.statusSidangProposal ?: "-"
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