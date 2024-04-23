package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangta

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

					if (getSidangTAResult.payload?.data?.showButton == false){
						binding.btnSimpanSidangTa.visibility = View.GONE
					}
					val message = getSidangTAResult.payload
					Log.d("Result success", message.toString())

					val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
					val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
					val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

					val data = resultResponse?.data
					// Kemudian dalam bagian pengaturan warna teks
					with(binding) {
						tvValueStatusIndividu.text = data?.kelompok?.statusTugasAkhir ?: "Belum dijadwalkan sidang!"
						tvValueStatusPendaftaran.text = data?.kelompok?.statusTugasAkhir ?: "Belum dijadwalkan sidang!"

						when (data?.kelompok?.statusTugasAkhir) {
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
								"Gagal Expo Project!",
								"Berkas TA Tidak Disetujui!"
							) -> {
								tvValueStatusIndividu.setTextColor(colorRed)
								tvValueStatusPendaftaran.setTextColor(colorRed)
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
								"Menunggu Penjadwalan Sidang TA!",
								"Menunggu Persetujuan Berkas TA!"
							) -> {
								tvValueStatusIndividu.setTextColor(colorOrange)
								tvValueStatusPendaftaran.setTextColor(colorOrange)
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
								tvValueStatusIndividu.setTextColor(colorGreen)
								tvValueStatusPendaftaran.setTextColor(colorGreen)
							}
							else -> {
								tvValueStatusIndividu.setTextColor(colorRed)
								tvValueStatusPendaftaran.setTextColor(colorRed)
							}
						}
					}


					if (resultResponse?.success == true) {

						with(binding) {
							edtJudulTugasAkhir.setText(resultResponse.data?.kelompok?.judulTAMhs)
							edtLinkPendukungSidangTa.setText(resultResponse.data?.kelompok?.linkUpload)

							setCardSidangTA(getSidangTAResult)

							setViewVisibility(tvTitleSidangTaTersedia, true)
							setViewVisibility(cvValueSidangTa, true)
							setViewVisibility(cvSidangTaTersedia, false)
							setViewVisibility(tvTitleFormPendaftaranSidangTa, true)
							setViewVisibility(cvDetailFormSidangTa, true)

							setViewVisibility(cvErrorSidangTaFragment, false)

							setViewVisibility(shimmerFragmentSidangTa, false)

						}

						// set form
						if (resultResponse.data?.kelompok?.judulTAMhs != null && resultResponse.data.kelompok.linkUpload != null){
							binding.tvValueJudul.text = resultResponse.data.kelompok.judulTAMhs
							binding.edtJudulTugasAkhir.setText(resultResponse.data.kelompok.judulTAMhs)
							binding.edtLinkPendukungSidangTa.setText(resultResponse.data.kelompok.linkUpload)
						}
					} else {

						Log.d("Succes status, but failed", status.toString())
						with(binding) {
							if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
								showSnackbar("Sesi anda telah berakhir :(")

								actionIfLogoutSucces()
							} else if (resultResponse?.data == null){
								setViewVisibility(binding.cvErrorSidangTaFragment, true)
								binding.tvErrorSidangTaFragment.text =
									status ?: "Mohon periksa kembali koneksi internet Anda!"

								setViewVisibility(tvTitleSidangTaTersedia, false)
								setViewVisibility(cvValueSidangTa, false)
								setViewVisibility(cvSidangTaTersedia, false)
								setViewVisibility(tvTitleFormPendaftaranSidangTa, false)
								setViewVisibility(cvDetailFormSidangTa, false)

								setViewVisibility(shimmerFragmentSidangTa, false)
							} else if (resultResponse.data.periode == null) {

								cvErrorSidangTaFragment.visibility = View.VISIBLE
								tvErrorSidangTaFragment.text = "Belum ada periode sidang Tugas Akhir yang tersedia!"

								cvSidangTaTersedia.visibility = View.GONE

								setViewVisibility(shimmerFragmentSidangTa, false)
							} else if (resultResponse.data.statusPendaftaran == null) {

								setViewVisibility(tvTitleSidangTaTersedia, true)
								setViewVisibility(cvValueSidangTa, false)
								setViewVisibility(cvSidangTaTersedia, true)
								setViewVisibility(tvTitleFormPendaftaranSidangTa, true)
								setViewVisibility(cvDetailFormSidangTa, true)

								setViewVisibility(cvErrorSidangTaFragment, false)

								setViewVisibility(shimmerFragmentSidangTa, false)

								tvValueNamaPeriode.text = resultResponse.data.periode.namaPeriode
								tvValueBatasPendaftaran.text =
									"${resultResponse.data.periode.hariBatas}, ${resultResponse.data.periode.tanggalBatas}"

							} else if (resultResponse.data.rsSidang == null) {
								setViewVisibility(binding.cvErrorSidangTaFragment, false)
								binding.tvValueStatusPendaftaran.text =
									resultResponse.data.kelompok?.statusTugasAkhir

								setViewVisibility(tvTitleSidangTaTersedia, true)
								setViewVisibility(cvValueSidangTa, false)
								setViewVisibility(cvSidangTaTersedia, true)
								setViewVisibility(tvTitleFormPendaftaranSidangTa, true)
								setViewVisibility(cvDetailFormSidangTa, true)

								setViewVisibility(shimmerFragmentSidangTa, false)

								tvValueNamaPeriode.text = resultResponse.data.periode.namaPeriode
								tvValueBatasPendaftaran.text =
									"${resultResponse.data.periode.hariBatas}, ${resultResponse.data.periode.tanggalBatas}"

								edtJudulTugasAkhir.setText(resultResponse.data.kelompok?.judulTAMhs)
								edtLinkPendukungSidangTa.setText(resultResponse.data.kelompok?.linkUpload)

							} else {

								setViewVisibility(binding.cvErrorSidangTaFragment, true)
								binding.tvErrorSidangTaFragment.text =
									status ?: "Mohon periksa kembali koneksi internet Anda!"

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
		if (isFormValid()) {

			showCustomAlertDialog(
				"Konfirmasi", "Apakah anda yakin data yang anda masukan sudah sesuai?"
			) {
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
							showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

						}

						is Resource.Success -> {
							setLoading(false)

							val status = daftarSidangTAResult.payload?.status

							if (resultResponse?.success == true && resultResponse.data != null) {
								Log.d("Daftar TA Succes status", status.toString())
								showSnackbar(
									resultResponse.status ?: "Berhasil mendaftar sidang tugas akhir!")

								findNavController().navigate(R.id.action_mahasiswaSidangTugasAkhirFragment_to_mahasiswaBerandaFragment)
							} else {
								Log.d("Daftar TA Succes status, but failed", status.toString())

								getSidangTA()

								if (status == "Token is Expired" || status == "Token is Invalid") {
									showSnackbar("Sesi anda telah berakhir :(")

									actionIfLogoutSucces()
								} else {
									showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

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
	private fun setCardSidangTA(getSidangTAResult: Resource<SidangTARemoteResponse>) {

		val data = getSidangTAResult.payload?.data


		if (data != null) {

			//  SidangTA sudah valid
			with(binding) {
				if (data.rsSidang == null) {
					edtJudulTugasAkhir.setText(data.kelompok?.judulTAMhs)
					edtLinkPendukungSidangTa.setText(data.kelompok?.linkUpload)

					tvValueStatusIndividu.text = data.kelompok?.statusTugasAkhir ?: "-"
					tvValueHariSidang.text = getSidangTAResult.payload.status ?: "-"

					btnSelengkapnyaSidangTa.visibility = View.GONE
				} else {
					edtJudulTugasAkhir.setText(data.kelompok?.judulTAMhs)
					edtLinkPendukungSidangTa.setText(data.kelompok?.linkUpload)

					tvValueStatusIndividu.text = data.kelompok?.statusTugasAkhir ?: "-"
					tvValueHariSidang.text =
						"${data.rsSidang.hariSidang}, ${data.rsSidang.tanggalSidang}"
					tvValueWaktuSidang.text = "${data.rsSidang.waktuSidang} WIB"
					tvValueRuangSidang.text = data.rsSidang.namaRuang ?: "-"
					tvValueJudul.text = data.kelompok?.judulTAMhs ?: "-"

					btnSelengkapnyaSidangTa.setOnClickListener {
						findNavController().navigate(R.id.action_mahasiswaSidangTugasAkhirFragment_to_mahasiswaSidangTugasAkhirFragmentDetail)
					}
				}

				edtJudulTugasAkhir.setText(data.kelompok?.judulTAMhs ?: "-")
				edtLinkPendukungSidangTa.setText(data.kelompok?.linkUpload ?: "-")

			}

		} else {
			//  status belum valid
			with(binding) {
				"Belum valid!".also { tvValueStatusIndividu.text = it }
			}
		}
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaSidangTugasAkhirFragment

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

	private fun isFormValid(): Boolean {
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