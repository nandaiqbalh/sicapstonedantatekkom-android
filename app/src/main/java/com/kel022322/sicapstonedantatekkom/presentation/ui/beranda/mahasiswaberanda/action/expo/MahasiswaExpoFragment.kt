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
import java.util.regex.Pattern

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

						if (getExpoResult.payload.data?.showButton == false){
							binding.btnSimpanExpo.visibility = View.GONE
						}
						with(binding) {
							setViewVisibility(linearLayoutExpoFragment, true)

							setViewVisibility(cvErrorExpoFragment, false)

							setViewVisibility(shimmerFragmentExpo, false)
						}
					} else {
						with(binding) {

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

							setLoading(false)

							val status = resultResponse?.status
							showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

						}

						is Resource.Success -> {
							setLoading(false)

							val status = daftarExpoResult.payload?.status

							if (resultResponse?.success == true && resultResponse.data != null) {
								showSnackbar(resultResponse.status ?: "Berhasil mendaftar expo!")

								findNavController().navigate(R.id.action_mahasiswaExpoFragment_to_mahasiswaBerandaFragment)
							} else {

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
		val colorBlue = ContextCompat.getColor(requireContext(), R.color.StatusBlue)
		val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

		// Kemudian dalam bagian pengaturan warna teks
		with(binding) {
			tvValueStatusKelompok.text = data?.kelompok?.statusExpo ?: "Belum Mendaftar Expo Project!"

			when (data?.kelompok?.statusExpo) {
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

		if (data?.rsExpo != null) {
			// set id expo
			idExpo = data.rsExpo.id.toString()
		}
		if (data != null) {


			//  kelompok sudah valid
			with(binding) {

				tvValueSiklusExpo.text = data.rsExpo.namaSiklus

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

		// Validate judul TA
		if (judulTaEntered.isEmpty()) {
			isFormValid = false
			binding.tilJudulTugasAkhir.error = getString(R.string.tv_error_input_blank)
		} else if (!isValidJudulTA(judulTaEntered)) {
			isFormValid = false
			binding.tilJudulTugasAkhir.error = "Judul TA maksimal 20 kata!"
		} else {
			binding.tilJudulTugasAkhir.error = null
		}

		// Validate link pendukung
		if (linkPendukungEntered.isEmpty()) {
			isFormValid = false
			binding.tilLinkPendukungExpo.error = getString(R.string.tv_error_input_blank)
		} else if (!isValidURL(linkPendukungEntered)) {
			isFormValid = false
			binding.tilLinkPendukungExpo.error = "Link tidak valid!"
		} else {
			binding.tilLinkPendukungExpo.error = null
		}

		return isFormValid
	}

	private fun isValidURL(url: String): Boolean {
		val pattern = Pattern.compile("^(https?|ftp)://.*\$", Pattern.CASE_INSENSITIVE)
		val matcher = pattern.matcher(url)
		return matcher.matches()
	}

	private fun isValidJudulTA(judul: String): Boolean {
		val wordCount = judul.trim().split("\\s+".toRegex()).size
		return wordCount <= 20
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