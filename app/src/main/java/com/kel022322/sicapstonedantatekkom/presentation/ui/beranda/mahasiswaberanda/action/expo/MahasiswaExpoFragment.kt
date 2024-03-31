package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.expo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
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

						setCardExpo(getExpoResult)

						with(binding) {
							setViewVisibility(linearLayoutExpoFragment, true)

							setViewVisibility(cvErrorExpoFragment, false)

							setViewVisibility(shimmerFragmentExpo, false)
						}
					} else {
						with(binding) {
							Log.d("Succes status, but failed", status.toString())

							if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {

								actionIfLogoutSucces()
							} else {
								setViewVisibility(cvErrorExpoFragment, false)

								setViewVisibility(shimmerFragmentExpo, false)
								setViewVisibility(binding.cvErrorExpoFragment, true)
								tvErrorExpoFragment.text =
									status ?: "Mohon periksa kembali koneksi internet Anda!"

							}

						}
					}

				}

				else -> {}
			}
		}
	}

	private fun daftarExpo() {
		if (isFormValid()) {
			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin data yang anda masukan sudah sesuai?"
			) {
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
							showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

						}

						is Resource.Success -> {
							setLoading(false)

							val status = daftarExpoResult.payload?.status

							if (resultResponse?.success == true && resultResponse.data != null) {
								Log.d("Daftar Expo Succes status", status.toString())
								showSnackbar(resultResponse.status ?: "Berhasil mendaftar expo!")

								findNavController().navigate(R.id.action_mahasiswaExpoFragment_to_mahasiswaBerandaFragment)
							} else {
								Log.d("Daftar Expo Succes status, but failed", status.toString())

								getExpo()
								if (status == "Token is Expired" || status == "Token is Invalid") {
									actionIfLogoutSucces()
								} else {
									getExpo()
									showSnackbar(
										status ?: "Mohon periksa kembali koneksi internet Anda!"
									)

								}
							}

						}

						else -> {}
					}
				}
			}

		}


	}

	@SuppressLint("SetTextI18n")
	private fun setCardExpo(getExpoResult: Resource<ExpoIndexRemoteResponse>) {

		val data = getExpoResult.payload?.data
		val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
		val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
		val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)
		ContextCompat.getColor(requireContext(), R.color.lightblue)

		with(binding){
			tvValueStatusKelompok.text = data?.kelompok?.statusKelompok ?: "Belum Mendaftar Expo!"

			when (data?.kelompok?.statusKelompok) {
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

		if (data?.rsExpo != null) {
			// set id expo
			idExpo = data.rsExpo.id.toString()
		}
		if (data != null) {


			//  kelompok sudah valid
			with(binding) {

				tvValueSiklusExpo.text = data.rsExpo.tahunAjaran

				tvValueHariExpo.text = "${data.rsExpo.hariExpo}, ${data.rsExpo.tanggalExpo}"
				tvValueWaktuExpo.text = "${data.rsExpo.waktuExpo} WIB"
				tvValueTempatExpo.text = data.rsExpo.tempat
				tvValueBatasPendaftaran.text = "${data.rsExpo.hariBatas}, ${data.rsExpo.tanggalBatas}"

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

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaExpoFragment

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

	private fun isFormValid(): Boolean {
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

	private fun showCustomAlertDialog(
		title: String,
		message: String,
		positiveAction: () -> Unit,
	) {
		val builder = AlertDialog.Builder(requireContext()).create()
		val view = layoutInflater.inflate(R.layout.dialog_custom_alert_dialog, null)
		builder.setView(view)
		builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

		val buttonYes = view.findViewById<Button>(R.id.btn_alert_yes)
		val buttonNo = view.findViewById<Button>(R.id.btn_alert_no)
		val alertTitle = view.findViewById<TextView>(R.id.tv_alert_title)
		val alertMessage = view.findViewById<TextView>(R.id.tv_alert_message)

		alertTitle.text = title
		alertMessage.text = message

		buttonYes.setOnClickListener {
			positiveAction.invoke()
			builder.dismiss()
		}

		buttonNo.setOnClickListener {
			builder.dismiss()
		}

		builder.setCanceledOnTouchOutside(false)
		builder.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}