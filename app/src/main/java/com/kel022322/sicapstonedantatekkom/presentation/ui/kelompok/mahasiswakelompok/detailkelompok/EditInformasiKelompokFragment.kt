package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.detailkelompok

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
import com.google.android.material.textfield.TextInputLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.editkelompok.request.EditKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response.TopikRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentEditInformasiKelompokBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.KelompokIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.TopikAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel.TopikViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditInformasiKelompokFragment : Fragment() {

	private var _binding: FragmentEditInformasiKelompokBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()
	private val topikViewModel: TopikViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()
	private var selectedIdTopik = ""
	private var currentIdTopik = ""
	private var topikCapstoneEntered = ""
	private var judulCapstoneEntered = ""
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentEditInformasiKelompokBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		checkKelompok()

		setActionListener()
		
		dataObserver()
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

			judulCapstoneEntered = edtJudulCapstoneEdit.text.toString().trim()


			btnSimpan.setOnClickListener {
				if (isFormValid()) {
					showCustomAlertDialog(
						"Konfirmasi", "Apakah anda yakin data yang anda masukan sudah sesuai?"
					) {

						setLoading(isLoading = true)

						userViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
							if (userId != null) {
								userViewModel.getApiToken()
									.observe(viewLifecycleOwner) { apiToken ->
										apiToken?.let {

											if (selectedIdTopik == ""){
												kelompokViewModel.editInformasiKelompok(
													apiToken,
													EditKelompokRemoteRequestBody(
														idTopik = currentIdTopik,
														judulCapstone = judulCapstoneEntered,
													)
												)
											} else {
												kelompokViewModel.editInformasiKelompok(
													apiToken,
													EditKelompokRemoteRequestBody(
														idTopik = selectedIdTopik,
														judulCapstone = judulCapstoneEntered,
													)
												)
											}

										}
									}
							}
						}
					}
				} else {
					showSnackbar("Form belum valid!")
				}
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
			edtTopikCapstoneEdit.setText(dataKelompok?.namaTopik) ?: "-"
			edtJudulCapstoneEdit.setText(dataKelompok?.judulCapstone) ?: "-"

			val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
			val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
			val colorBlue = ContextCompat.getColor(requireContext(), R.color.StatusBlue)
			val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

			// Kemudian dalam bagian pengaturan warna teks
			with(binding) {
				tvValueStatusKelompokDetail.text = data?.kelompok?.statusKelompok ?: "Belum Mendaftar Capstone!"

				when (data?.kelompok?.statusKelompok) {
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
						tvValueStatusKelompokDetail.setTextColor(colorRed)
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
						tvValueStatusKelompokDetail.setTextColor(colorOrange)
					}
					in listOf(
						"Kelompok Diplot Tim Capstone",
						"Dosbing Diplot Tim Capstone",
						"Dijadwalkan Sidang Proposal",
						"Kelompok Disetujui Expo",
						"Telah Dijadwalkan Sidang TA",
					) -> {
						tvValueStatusKelompokDetail.setTextColor(colorBlue)
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
						tvValueStatusKelompokDetail.setTextColor(colorGreen)
					}
					else -> {
						tvValueStatusKelompokDetail.setTextColor(colorRed)
					}
				}
			}


		}


	}

	private fun checkKelompok() {
		setLoading(true)

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.getKelompokSaya(
					apiToken
				)

				topikViewModel.getTopik(apiToken)
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

						setViewVisibility(shimmerFragmentKelompokEdit, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())
					if (resultResponse?.success == true && resultResponse.data != null) {
						setCardKelompok(getKelompokSayaResult)

						currentIdTopik = resultResponse.data.kelompok?.idTopik.toString()
						// if already have kelompok
						with(binding) {
							setViewVisibility(cvValueKelompokDetail, true)

							setViewVisibility(shimmerFragmentKelompokEdit, false)
						}
					} else {
						Log.d("Succes, but failed", status.toString())

						with(binding){
							setViewVisibility(cvValueKelompokDetail, false)

							setViewVisibility(shimmerFragmentKelompokEdit, false)
						}

						if (status == "Authorization Token not found" ||  status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						}
					}
				}

				else -> {}
			}
		}
	}
	
	private fun dataObserver(){

		topikViewModel.getTopikResult.observe(viewLifecycleOwner) { getTopikResult ->
			val status = getTopikResult.payload?.status

			when (getTopikResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false)
					Log.d("Result error", getTopikResult.message.toString())

				}

				is Resource.Success -> {

					val message = getTopikResult.payload?.status
					Log.d("Result success", message.toString())

					if (getTopikResult.payload?.success == true) {
						setLoading(isLoading = false)

						if (getTopikResult.payload.data != null) {
							setTopikDropdown(getTopikResult)

							if (selectedIdTopik == "") {
								binding.edtTopikCapstoneEdit.text = null
							}
						}

					} else {
						setLoading(isLoading = false)

						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						}
					}

				}

				else -> {}
			}
		}

		kelompokViewModel.editKelompokResult.observe(viewLifecycleOwner) { editKelompokResult ->

			val resultResponse = editKelompokResult.payload
			val status = resultResponse?.status

			when (editKelompokResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true)

				}

				is Resource.Error -> {

					setLoading(isLoading = false)

					showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

				}

				is Resource.Success -> {

					if (resultResponse?.success == true) {
						setLoading(isLoading = false)

						Log.d("Succes status", status.toString())
						showSnackbar(resultResponse.status ?: "Informasi berhasil diperbaharui!")

						findNavController().navigate(R.id.action_editInformasiKelompokFragment_to_mahasiswaBerandaFragment)
					} else {
						setLoading(isLoading = false)

						checkKelompok()
						showSnackbar(
							resultResponse?.status ?: "Terjadi kesalahan saat mendaftar!",
						)

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

	private fun setTopikDropdown(getTopikResult: Resource<TopikRemoteResponse>) {

		// topik
		val topikAdapter =
			getTopikResult.payload?.data?.rs_topik?.let {
				TopikAdapter(
					requireContext(),
					it
				)
			}

		binding.edtTopikCapstoneEdit.setAdapter(topikAdapter)

		binding.edtTopikCapstoneEdit.setOnItemClickListener { _, _, position, _ ->
			val selectedTopik = topikAdapter?.getItem(position)
			selectedIdTopik = selectedTopik?.id.toString()

			binding.edtTopikCapstoneEdit.setText(selectedTopik?.nama)

			// Clear focus and show dropdown
			binding.edtTopikCapstoneEdit.showDropDown()
			Log.d("EDT", "EDT CLICKED!")
		}
	}
	

	private fun isFormValid(): Boolean {
		with(binding) {

			topikCapstoneEntered = edtTopikCapstoneEdit.text.toString().trim()
			judulCapstoneEntered = edtJudulCapstoneEdit.text.toString().trim()
			
			var isFormValid = true

			Log.d("EDTTEXT", topikCapstoneEntered)

			// capstone
			isFormValid = validateField(
				topikCapstoneEntered,
				tilTopikCapstoneEdit,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				judulCapstoneEntered,
				tilJudulCapstoneKelompok,
				R.string.tv_error_input_blank
			) && isFormValid

			return isFormValid
		}
	}

	private fun validateField(
		value: String,
		til: TextInputLayout,
		errorBlank: Int,
		errorInvalid: Int? = null,
		lengthRange: IntRange? = null,
		validateFunction: ((String) -> Boolean)? = null,
	): Boolean {
		if (value.isEmpty()) {
			til.error = getString(errorBlank)
			til.isErrorEnabled = true
			return false
		} else {
			til.error = null
			til.isErrorEnabled = false

			if (lengthRange != null && (value.length !in lengthRange)) {
				til.error = getString(errorInvalid!!)
				til.isErrorEnabled = true
				return false
			}

			if (validateFunction != null && !validateFunction.invoke(value)) {
				til.error = getString(errorInvalid!!)
				til.isErrorEnabled = true
				return false
			}
		}

		return true
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@EditInformasiKelompokFragment
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
			setShimmerVisibility(shimmerFragmentKelompokEdit, isLoading)
			if (isLoading) {
				cvValueKelompokDetail.visibility = View.GONE

			}
		}
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