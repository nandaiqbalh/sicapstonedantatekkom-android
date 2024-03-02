package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.expo

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
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.request.DaftarExpoRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response.ExpoIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaExpoBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaExpoFragment : Fragment() {

	private var _binding: FragmentMahasiswaExpoBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val expoViewModel: MahasiswaExpoViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	private var idExpo = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaExpoBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setButtonListener()

		getExpo()
	}

	private fun setButtonListener() {

		binding.ivCircleBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		binding.icBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		binding.btnSimpanExpo.setOnClickListener {
			daftarExpo()
		}
	}

	@SuppressLint("SetTextI18n")
	private fun getExpo() {
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let { expoViewModel.getExpo(it) }
		}

		expoViewModel.getExpoResult.observe(viewLifecycleOwner) { getExpoResult ->
			val resultResponse = getExpoResult.payload
			val status = resultResponse?.status

			when (getExpoResult) {
				is Resource.Loading -> {
					setLoading(true)

				}

				is Resource.Error -> {
					setLoading(false)

					showSnackbar(status ?: "Terjadi kesalahan!", true)

					Log.d(
						"Error Kelompok Index",
						getExpoResult.payload?.status.toString()
					)

					// set view condition
					with(binding) {
						setViewVisibility(linearLayoutExpoFragment, false)

						setViewVisibility(cvErrorExpoFragment, true)

						setViewVisibility(shimmerFragmentExpo, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getExpoResult.payload
					Log.d("Result success", message.toString())

					if (resultResponse?.success == true && resultResponse.data?.kelompok?.nomorKelompok != null) {

						setCardSidangProposal(getExpoResult)

						with(binding) {
							setViewVisibility(linearLayoutExpoFragment, true)

							setViewVisibility(cvErrorExpoFragment, false)

							setViewVisibility(shimmerFragmentExpo, false)
						}
					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(", true)

							actionIfLogoutSucces()
						} else {
							if (resultResponse?.data?.kelompok?.nomorKelompok == null){
								setViewVisibility(binding.cvErrorExpoFragment, true)
								binding.tvErrorExpoFragment.text = "Kelompok anda belum valid"
							} else {
								setViewVisibility(binding.cvErrorExpoFragment, true)
								binding.tvErrorExpoFragment.text = status ?: "Terjadi kesalahan!"
							}
						}
					}

				}

				else -> {}
			}
		}
	}

	private fun daftarExpo() {
		if (isFormProfilValid()) {
			setLoading(true)

			val judulTaEntered = binding.edtJudulTugasAkhir.text.toString().trim()
			val linkPendukungEntered = binding.edtLinkPendukungExpo.text.toString().trim()

			userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
				apiToken?.let {
					expoViewModel.daftarExpo(
						it, DaftarExpoRemoteRequestBody(
							idExpo = idExpo,
							judulTaMhs = judulTaEntered,
							linkBerkasExpo = linkPendukungEntered
						)
					)
				}
			}

			expoViewModel.daftarExpoResult.observe(viewLifecycleOwner) { daftarExpoResult ->
				val resultResponse = daftarExpoResult.payload

				when (daftarExpoResult) {
					is Resource.Loading -> {
						setLoading(true)
					}

					is Resource.Error -> {
						Log.d("Daftar Expo Error", daftarExpoResult.payload?.status.toString())

						setLoading(false)

						val status = resultResponse?.status
						showSnackbar(status ?: "Terjadi kesalahan!", false)

					}

					is Resource.Success -> {
						setLoading(false)

						val status = daftarExpoResult.payload?.status

						if (resultResponse?.success == true && resultResponse.data != null) {
							Log.d("Daftar Expo Succes status", status.toString())
							showSnackbar(resultResponse.status ?: "Berhasil mendaftar expo!", true)

							findNavController().navigate(R.id.action_mahasiswaExpoFragment_to_mahasiswaBerandaFragment)
						} else {
							Log.d("Daftar Expo Succes status, but failed", status.toString())

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
	private fun setCardSidangProposal(getExpoResult: Resource<ExpoIndexRemoteResponse>) {

		val data = getExpoResult.payload?.data

		if (data?.cekExpo != null){
			binding.tvValueStatusKelompok.text = data.cekExpo.statusExpo
		}

		if (data?.rsExpo != null){
			// set id expo
			idExpo = data.rsExpo.id.toString()
		}
		if (data != null) {


			//  kelompok sudah valid
			with(binding) {

				tvValueSiklusExpo.text = data.rsExpo.tahunAjaran

				tvValueBatasPendaftaran.text = data.rsExpo.tanggalSelesai

				edtJudulTugasAkhir.setText(data.kelengkapan.judulTAMhs)
				edtLinkPendukungExpo.setText(data.kelengkapan.linkBerkasExpo)
			}

		} else {
			//  kelompok belum valid
			with(binding) {
				// card kelompok
				"Belum valid!".also { tvValueStatusKelompok.text = it }
			}
		}
	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaExpoFragment

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
		val currentFragment = this@MahasiswaExpoFragment

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
			setShimmerVisibility(shimmerFragmentExpo, isLoading)


			if (isLoading) {
				linearLayoutExpoFragment.visibility = View.GONE
				cvErrorExpoFragment.visibility = View.GONE


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
		val linkPendukungEntered = binding.edtLinkPendukungExpo.text.toString().trim()

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
			binding.tilLinkPendukungExpo.error = getString(R.string.tv_error_input_blank)
		} else {
			binding.tilLinkPendukungExpo.error = null
		}

		return isFormValid
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}