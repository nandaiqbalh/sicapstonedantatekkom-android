package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangta.detail

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
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response.SidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaSidangTugasAkhirDetailBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangta.SidangTugasAkhirViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.KelompokIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDosbingAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDospengTaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaSidangTugasAkhirFragmentDetail : Fragment() {

	private var _binding: FragmentMahasiswaSidangTugasAkhirDetailBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()
	private val sidangTaViewModel: SidangTugasAkhirViewModel by viewModels()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaSidangTugasAkhirDetailBinding.inflate(layoutInflater, container, false)
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
	private fun setCardSidangTa(getSidangTaResult: Resource<SidangTARemoteResponse>) {

		val data = getSidangTaResult.payload?.data

		if (data != null) {

			//  kelompok sudah valid
			with(binding) {

				val dataKelompok = data.kelompok
				// card kelompok
				tvValueStatusKelompok.text = data.statusPendaftaran?.status ?: "-"
				tvValueRuangSidang.text = data.rsSidang?.namaRuang ?: "-"
				tvValueHariSidang.text = "${data.rsSidang?.hariSidang}, ${data.rsSidang?.tanggalSidang}"
				tvValueWaktuSidang.text = "${data.rsSidang?.waktuSidang} WIB"

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


	private fun setCardSidangTADetail(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		val data = getKelompokSayaResult.payload?.data

		//  kelompok sudah valid
		with(binding) {

			// dosbing
			val dataDosbing = data?.rsDosbing
			// card dosbing
			val akunDosbingAdapter = AkunDosbingAdapter()

			akunDosbingAdapter.setList(dataDosbing)

			rvAkunDosbingSidangTaDetail.layoutManager = LinearLayoutManager(
				requireContext(),
				LinearLayoutManager.VERTICAL,
				false
			)
			rvAkunDosbingSidangTaDetail.adapter = akunDosbingAdapter

			// navigate to detail if necessary
			akunDosbingAdapter.setOnItemClickCallback(object :
				AkunDosbingAdapter.OnItemClickCallBack {
				override fun onItemClicked(dosbingId: String) {
				}
			})

			// dospeng
			val dataDospeng = data?.rsDospengTa
			// card Dospeng
			val akunDospengAdapter = AkunDospengTaAdapter()

			akunDospengAdapter.setList(dataDospeng)

			rvAkunDospengSidangTaDetail.layoutManager = LinearLayoutManager(
				requireContext(),
				LinearLayoutManager.VERTICAL,
				false
			)
			rvAkunDospengSidangTaDetail.adapter = akunDospengAdapter

			// navigate to detail if necessary
			akunDospengAdapter.setOnItemClickCallback(object :
				AkunDospengTaAdapter.OnItemClickCallBack {
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
				sidangTaViewModel.getSidangTA(apiToken)
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
						setViewVisibility(cvValueSidangTaDetail, false)
						setViewVisibility(cvValueDosbingDetail, false)
						setViewVisibility(cvValueDospengDetail, false)
						setViewVisibility(linearLayoutSidangTa, false)
						setViewVisibility(cvErrorSidangTaDetail, true)

						setViewVisibility(shimmerFragmentSidangTaDetail, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())
					if (resultResponse?.success == true && resultResponse.data != null) {
						setCardSidangTADetail(getKelompokSayaResult)
						// if already have kelompok
						with(binding) {
							setViewVisibility(cvValueSidangTaDetail, true)
							setViewVisibility(cvValueDosbingDetail, true)
							setViewVisibility(cvValueDospengDetail, true)
							setViewVisibility(linearLayoutSidangTa, true)
							setViewVisibility(cvErrorSidangTaDetail, false)

							setViewVisibility(shimmerFragmentSidangTaDetail, false)

						}
					} else {
						Log.d("Succes, but failed", status.toString())

						with(binding){
							setViewVisibility(cvValueSidangTaDetail, false)
							setViewVisibility(cvValueDosbingDetail, false)
							setViewVisibility(cvValueDospengDetail, false)
							setViewVisibility(linearLayoutSidangTa, false)
							setViewVisibility(cvErrorSidangTaDetail, true)

							setViewVisibility(shimmerFragmentSidangTaDetail, false)

						}

						if (status == "Authorization Token not found" ||  status == "Token is Expired" || status == "Token is Invalid") {

							actionIfLogoutSucces()
						}
					}
				}

				else -> {}
			}
		}

		sidangTaViewModel.getSidangTAResult.observe(viewLifecycleOwner) { getSidangTaResult ->
			val resultResponse = getSidangTaResult.payload
			val status = resultResponse?.status

			when (getSidangTaResult) {
				is Resource.Loading -> {
					setLoading(true)

				}

				is Resource.Error -> {
					setLoading(false)
					Log.d(
						"Error Kelompok Index",
						getSidangTaResult.payload?.status.toString()
					)

				}

				is Resource.Success -> {
					setLoading(false)

					val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
					val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
					val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

					with(binding){
						tvValueStatusKelompok.text = resultResponse?.data?.statusPendaftaran?.status ?: "Belum Mendaftar Sidang TA!"
						tvValueStatusKelompok.setTextColor(colorRed)

						when (resultResponse?.data?.statusPendaftaran?.status) {
							"Menunggu Validasi Jadwal!" -> {
								tvValueStatusKelompok.setTextColor(colorOrange)
							}
							"Telah Dijadwalkan Sidang TA!" , "Lulus Sidang TA!"-> {
								tvValueStatusKelompok.setTextColor(colorGreen)
							}
							else -> {
								tvValueStatusKelompok.setTextColor(colorRed)
							}
						}
					}
					val message = getSidangTaResult.payload
					Log.d("Result success", message.toString())

					if (resultResponse?.success == true && resultResponse.data != null) {

						setCardSidangTa(getSidangTaResult)

					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							actionIfLogoutSucces()
						}
					}

				}

				else -> {}
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
			setShimmerVisibility(shimmerFragmentSidangTaDetail, isLoading)
			if (isLoading) {
				setViewVisibility(cvValueSidangTaDetail, false)
				setViewVisibility(cvValueDosbingDetail, false)
				setViewVisibility(cvValueDospengDetail, false)
				setViewVisibility(linearLayoutSidangTa, false)
				setViewVisibility(cvErrorSidangTaDetail, false)

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