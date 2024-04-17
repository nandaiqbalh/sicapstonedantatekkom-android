package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.detailkelompok

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

			btnUbahKelompok.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaKelompokDetailFragment_to_editInformasiKelompokFragment)
			}

		}
	}

	private fun setCardKelompok(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		val data = getKelompokSayaResult.payload?.data

		//  kelompok sudah valid
		with(binding) {
			val dataKelompok = data?.kelompok
			// card kelompok
			tvValueNomorKelompokDetail.text = dataKelompok?.nomorKelompok ?: "-"
			tvValueTopikDetail.text = dataKelompok?.namaTopik ?: "-"
			tvValueJudulDetail.text = dataKelompok?.judulCapstone ?: "-"

			val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
			val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
			val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

		// Kemudian dalam bagian pengaturan warna teks
			with(binding) {
				tvValueStatusKelompokDetail.text = data?.kelompok?.statusKelompok ?: "Belum Mendaftar Capstone!"

				when (data?.kelompok?.statusKelompok) {
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
						tvValueStatusKelompokDetail.setTextColor(colorRed)
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
						tvValueStatusKelompokDetail.setTextColor(colorOrange)
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
						tvValueStatusKelompokDetail.setTextColor(colorGreen)
					}
					else -> {
						tvValueStatusKelompokDetail.setTextColor(colorRed)
					}
				}
			}


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