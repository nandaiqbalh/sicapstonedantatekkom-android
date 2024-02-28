package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangta

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
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.request.DaftarSidangTARemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response.SidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaSidangTugasAkhirBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaSidangTugasAkhirFragment : Fragment() {

	private var _binding: FragmentMahasiswaSidangTugasAkhirBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val sidangTugasAkhirViewModel: SidangTugasAkhirViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding =
			FragmentMahasiswaSidangTugasAkhirBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setButtonListener()

		getSidangTA()
	}

	private fun setButtonListener() {

		binding.ivCircleBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		binding.icBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		binding.btnSimpanSidangTa.setOnClickListener {
			daftarSidangTA()
		}
	}

	@SuppressLint("SetTextI18n")
	private fun getSidangTA() {
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let { sidangTugasAkhirViewModel.getSidangTA(it) }
		}

		sidangTugasAkhirViewModel.getSidangTAResult.observe(viewLifecycleOwner) { getSidangTAResult ->
			val resultResponse = getSidangTAResult.payload
			val status = resultResponse?.status

			when (getSidangTAResult) {
				is Resource.Loading -> {
					setLoading(true)

				}

				is Resource.Error -> {
					setLoading(false)

					showSnackbar(status ?: "Terjadi kesalahan!", true)

					Log.d(
						"Error SidangTA Index",
						getSidangTAResult.payload?.status.toString()
					)

					// set view condition
					with(binding) {
						setViewVisibility(tvTitleSidangTaTersedia, false)
						setViewVisibility(cvValueSidangTa, false)
						setViewVisibility(cvSidangTaTersedia, false)
						setViewVisibility(tvTitleFormPendaftaranSidangTa, false)
						setViewVisibility(cvDetailFormSidangTa, false)

						setViewVisibility(cvErrorSidangTaFragment, true)

						setViewVisibility(shimmerFragmentSidangTa, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getSidangTAResult.payload
					Log.d("Result success", message.toString())

					if (resultResponse?.success == true) {

						with(binding) {

							if (resultResponse.data?.statusPendaftaran == null) {

								setCardBelumMendaftar(getSidangTAResult)

								setViewVisibility(tvTitleSidangTaTersedia, true)
								setViewVisibility(cvValueSidangTa, false)
								setViewVisibility(cvSidangTaTersedia, true)
								setViewVisibility(tvTitleFormPendaftaranSidangTa, true)
								setViewVisibility(cvDetailFormSidangTa, true)

								setViewVisibility(cvErrorSidangTaFragment, false)

								setViewVisibility(shimmerFragmentSidangTa, false)
							} else {
								setCardSidangProposal(getSidangTAResult)

								setViewVisibility(tvTitleSidangTaTersedia, true)
								setViewVisibility(cvValueSidangTa, true)
								setViewVisibility(cvSidangTaTersedia, false)
								setViewVisibility(tvTitleFormPendaftaranSidangTa, true)
								setViewVisibility(cvDetailFormSidangTa, true)

								setViewVisibility(cvErrorSidangTaFragment, false)

								setViewVisibility(shimmerFragmentSidangTa, false)
							}

						}
					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(", true)

							actionIfLogoutSucces()
						} else {

							with(binding) {
								showSnackbar(status ?: "Terjadi kesalahan!", true)
								setViewVisibility(binding.cvErrorSidangTaFragment, true)
								binding.tvErrorSidangTaFragment.text =
									status ?: "Terjadi kesalahan!"

								setViewVisibility(tvTitleSidangTaTersedia, false)
								setViewVisibility(cvValueSidangTa, false)
								setViewVisibility(cvSidangTaTersedia, false)
								setViewVisibility(tvTitleFormPendaftaranSidangTa, false)
								setViewVisibility(cvDetailFormSidangTa, false)

								setViewVisibility(shimmerFragmentSidangTa, false)

							}

						}
					}

				}

				else -> {}
			}
		}
	}

	private fun daftarSidangTA() {
		if (isFormProfilValid()) {
			setLoading(true)

			val judulTaEntered = binding.edtJudulTugasAkhir.text.toString().trim()
			val linkPendukungEntered = binding.edtLinkPendukungSidangTa.text.toString().trim()

			userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
				apiToken?.let {
					sidangTugasAkhirViewModel.daftarSidangTA(
						it, DaftarSidangTARemoteRequestBody(
							judulTaMhs = judulTaEntered,
							linkUpload = linkPendukungEntered
						)
					)
				}
			}

			sidangTugasAkhirViewModel.daftarSidangTAResult.observe(viewLifecycleOwner) { daftarSidangTAResult ->
				val resultResponse = daftarSidangTAResult.payload

				when (daftarSidangTAResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						Log.d(
							"Daftar SidangTA Error",
							daftarSidangTAResult.payload?.status.toString()
						)

						setLoading(false)

						val status = resultResponse?.status
						showSnackbar(status ?: "Terjadi kesalahan!", false)

					}

					is Resource.Success -> {
						setLoading(false)

						val status = daftarSidangTAResult.payload?.status

						if (resultResponse?.success == true && resultResponse.data != null) {
							Log.d("Daftar TA Succes status", status.toString())
							showSnackbar(
								resultResponse.status ?: "Berhasil mendaftar sidang tugas akhir!",
								true
							)

							findNavController().navigate(R.id.action_mahasiswaSidangTugasAkhirFragment_to_mahasiswaBerandaFragment)
						} else {
							Log.d("Daftar TA Succes status, but failed", status.toString())

							if (status == "Token is Expired" || status == "Token is Invalid") {
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


	}

	@SuppressLint("SetTextI18n")
	private fun setCardBelumMendaftar(getSidangTAResult: Resource<SidangTARemoteResponse>) {

		val data = getSidangTAResult.payload?.data

		if (data != null) {

			//  SidangTA sudah valid
			with(binding) {

				tvValueNamaPeriode.text = data.periode?.namaPeriode
				tvValueBatasPendaftaran.text =
					"${data.periode?.hariBatas}, ${data.periode?.tanggalBatas}"
			}

		} else {
			//  status belum valid
			with(binding) {
				"Belum mendaftar!".also { tvValueStatusPendaftaran.text = it }
			}
		}
	}

	@SuppressLint("SetTextI18n")
	private fun setCardSidangProposal(getSidangTAResult: Resource<SidangTARemoteResponse>) {

		val data = getSidangTAResult.payload?.data

		if (data != null) {

			//  SidangTA sudah valid
			with(binding) {
				if (data.rsSidang == null) {
					tvValueStatusIndividu.text = data.statusPendaftaran?.status
					tvValueHariSidang.text = getSidangTAResult.payload.status

					btnSelengkapnyaSidangTa.visibility = View.GONE
				} else {
					tvValueStatusIndividu.text = data.rsSidang.statusIndividu
					tvValueHariSidang.text =
						"${data.rsSidang.hariSidang}, ${data.rsSidang.tanggalSidang}"
					tvValueWaktuSidang.text = "${data.rsSidang.waktuSidang} WIB"
					tvValueRuangSidang.text = data.rsSidang.namaRuang
					tvValueJudul.text = data.rsSidang.judulTaMhs

					btnSelengkapnyaSidangTa.setOnClickListener {
						findNavController().navigate(R.id.action_mahasiswaSidangTugasAkhirFragment_to_mahasiswaSidangTugasAkhirFragmentDetail)
					}
				}

				edtJudulTugasAkhir.setText(data.rsSidang?.judulTaMhs)
				edtLinkPendukungSidangTa.setText(data.rsSidang?.linkUpload)

			}

		} else {
			//  status belum valid
			with(binding) {
				"Belum valid!".also { tvValueStatusIndividu.text = it }
			}
		}
	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaSidangTugasAkhirFragment

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

	private fun showSnackbar(message: String, isRestart: Boolean) {
		val currentFragment = this@MahasiswaSidangTugasAkhirFragment

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
			setShimmerVisibility(shimmerFragmentSidangTa, isLoading)


			if (isLoading) {
				setViewVisibility(tvTitleSidangTaTersedia, false)
				setViewVisibility(cvValueSidangTa, false)
				setViewVisibility(cvSidangTaTersedia, false)
				setViewVisibility(tvTitleFormPendaftaranSidangTa, false)
				setViewVisibility(cvDetailFormSidangTa, false)

				setViewVisibility(cvErrorSidangTaFragment, false)

			}
		}


	}

	private fun setShimmerVisibility(shimmerView: View, isLoading: Boolean) {
		shimmerView.visibility = if (isLoading) View.VISIBLE else View.GONE
		(shimmerView as? ShimmerFrameLayout)?.run {
			if (isLoading) startShimmer() else stopShimmer()
		}
	}

	private fun isFormProfilValid(): Boolean {
		val judulTaEntered = binding.edtJudulTugasAkhir.text.toString().trim()
		val linkPendukungEntered = binding.edtLinkPendukungSidangTa.text.toString().trim()

		var isFormValid = true

		// Validate name
		if (judulTaEntered.isEmpty()) {
			isFormValid = false
			binding.tilJudulTugasAkhir.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilJudulTugasAkhir.error = null
		}

		if (linkPendukungEntered.isEmpty()) {
			isFormValid = false
			binding.tilLinkPendukungSidangTa.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilLinkPendukungSidangTa.error = null
		}

		return isFormValid
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}