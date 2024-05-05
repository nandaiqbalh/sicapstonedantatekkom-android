package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangproposal.detail

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.R
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
				tvValueStatusKelompok.text = data.kelompok?.statusSidangProposal ?: "-"
				tvValueRuangSidang.text = data.namaRuang ?: "-"
				tvValueHariSidang.text = "${data.hariSidang}, ${data.tanggalSidang}"
				tvValueWaktuSidang.text = "${data.waktuSidang} WIB"

				tvValueJudul.text = dataKelompok?.judulCapstone ?: "-"

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

							actionIfLogoutSucces()
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

					Log.d(
						"Error Kelompok Index",
						getSidangProposalByKelompokResult.payload?.status.toString()
					)

				}

				is Resource.Success -> {
					setLoading(false)

					val message = getSidangProposalByKelompokResult.payload
					Log.d("Result success", message.toString())

					val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
					val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
					val colorBlue = ContextCompat.getColor(requireContext(), R.color.StatusBlue)
					val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

					val data = resultResponse?.data
					// Kemudian dalam bagian pengaturan warna teks
					with(binding) {
						tvValueStatusKelompok.text = data?.kelompok?.statusSidangProposal ?: "Belum dijadwalkan sidang proposal!"

						when (data?.kelompok?.statusSidangProposal) {
							in listOf(
								"Dosbing Tidak Setuju",
								"Penguji Tidak Setuju",
								"C100 Tidak Disetujui Dosbing 1",
								"C100 Tidak Disetujui Dosbing 2",
								"Final C100 Tidak Disetujui Dosbing 1",
								"Final C100 Tidak Disetujui Dosbing 2",
								"C200 Tidak Disetujui Dosbing 1",
								"C200 Tidak Disetujui Dosbing 2",
								"C300 Tidak Disetujui Dosbing 1",
								"C300 Tidak Disetujui Dosbing 2",
								"C400 Tidak Disetujui Dosbing 1",
								"C400 Tidak Disetujui Dosbing 2",
								"C500 Tidak Disetujui Dosbing 1",
								"C500 Tidak Disetujui Dosbing 2",
								"Laporan TA Tidak Disetujui Dosbing 1",
								"Laporan TA Tidak Disetujui Dosbing 2",
								"Final Laporan TA Tidak Disetujui Dosbing 1",
								"Final Laporan TA Tidak Disetujui Dosbing 2",
								"Makalah TA Tidak Disetujui Dosbing 1",
								"Makalah TA Tidak Disetujui Dosbing 2",
								"Kelompok Tidak Disetujui Expo",
								"Laporan TA Tidak Disetujui",
								"Final Laporan TA Tidak Disetujui",
								"Makalah TA Tidak Disetujui",
								"Belum Mendaftar Sidang TA",
								"Gagal Expo Project",
								"Pendaftaran Sidang Tidak Disetujui"
							) -> {
								tvValueStatusKelompok.setTextColor(colorRed)
							}
							in listOf(
								"Menunggu Penetapan Kelompok",
								"Menunggu Persetujuan Dosbing",
								"C100 Menunggu Persetujuan Dosbing 1",
								"C100 Menunggu Persetujuan Dosbing 2",
								"Final C100 Menunggu Persetujuan Dosbing 1",
								"Final C100 Menunggu Persetujuan Dosbing 2",
								"C200 Menunggu Persetujuan Dosbing 1",
								"C200 Menunggu Persetujuan Dosbing 2",
								"C300 Menunggu Persetujuan Dosbing 1",
								"C300 Menunggu Persetujuan Dosbing 2",
								"C400 Menunggu Persetujuan Dosbing 1",
								"C400 Menunggu Persetujuan Dosbing 2",
								"C500 Menunggu Persetujuan Dosbing 1",
								"C500 Menunggu Persetujuan Dosbing 2",
								"Laporan TA Menunggu Persetujuan Dosbing 1",
								"Laporan TA Menunggu Persetujuan Dosbing 2",
								"Final Laporan TA Menunggu Persetujuan Dosbing 1",
								"Final Laporan TA Menunggu Persetujuan Dosbing 2",
								"Makalah TA Menunggu Persetujuan Dosbing 1",
								"Makalah TA Menunggu Persetujuan Dosbing 2",
								"Menunggu Persetujuan Anggota",
								"Didaftarkan",
								"Menunggu Penetapan Dosbing",
								"Menunggu Persetujuan Tim Capstone",
								"Menunggu Persetujuan C100",
								"Menunggu Persetujuan Final C100",
								"Menunggu Persetujuan C200",
								"Menunggu Persetujuan C300",
								"Menunggu Persetujuan C400",
								"Menunggu Persetujuan C500",
								"Menunggu Persetujuan Expo",
								"Menunggu Persetujuan Laporan TA",
								"Menunggu Persetujuan Final Laporan TA",
								"Menunggu Persetujuan Makalah TA",
								"Menunggu Persetujuan Penguji",
								"Menunggu Persetujuan Pembimbing",
								"Menunggu Penjadwalan Sidang TA",
								"Menunggu Persetujuan Pendaftaran Sidang"
							) -> {
								tvValueStatusKelompok.setTextColor(colorOrange)
							}
							in listOf(
								"Kelompok Diplot Tim Capstone",
								"Dosbing Diplot Tim Capstone",
								"Dijadwalkan Sidang Proposal",
								"Kelompok Disetujui Expo",
								"Telah Dijadwalkan Sidang TA",
							) -> {
								tvValueStatusKelompok.setTextColor(colorBlue)
							}
							in listOf(
								"Menyetujui Kelompok",
								"Dosbing Setuju",
								"Kelompok Telah Disetujui",
								"C100 Telah Disetujui",
								"Final C100 Telah Disetujui",
								"Penguji Proposal Ditetapkan",
								"Pembimbing Setuju",
								"Penguji Setuju",
								"Lulus Sidang Proposal",
								"C200 Telah Disetujui",
								"C300 Telah Disetujui",
								"C400 Telah Disetujui",
								"C500 Telah Disetujui",
								"Lulus Expo Project",
								"Laporan TA Telah Disetujui",
								"Final Laporan TA Telah Disetujui",
								"Makalah TA Telah Disetujui",
								"Penguji TA Setuju",
								"Lulus Sidang TA",
							) -> {
								tvValueStatusKelompok.setTextColor(colorGreen)
							}
							else -> {
								tvValueStatusKelompok.setTextColor(colorRed)
							}
						}
					}

					if (resultResponse?.success == true && resultResponse.data != null) {

						setCardSidangProposal(getSidangProposalByKelompokResult)

					} else {
						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						}
					}

				}

				else -> {}
			}
		}

	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaSidangProposalDetailFragment
		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), message, "OK"
			) {
				customSnackbar.dismissSnackbar()
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