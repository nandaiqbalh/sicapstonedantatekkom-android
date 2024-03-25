package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.daftarindividu

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.local.model.jeniskelamin.JenisKelaminModel
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.siklus.response.SiklusRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokDaftarIndividuBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.SiklusAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel.SiklusViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel.ProfileIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MahasiswaKelompokDaftarIndividuFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokDaftarIndividuBinding? = null
	private val binding get() = _binding!!

	private val daftarIndividuViewModel: DaftarIndividuViewModel by viewModels()
	private val userViewModel: UserViewModel by viewModels()
	private val siklusViewModel: SiklusViewModel by viewModels()
	private val profileIndexViewModel: ProfileIndexViewModel by viewModels()


	private val customSnackbar = CustomSnackbar()

	private var selectedIdSiklus = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment

		_binding =
			FragmentMahasiswaKelompokDaftarIndividuBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		valueObserver()

		setActionListener()
	}

	private fun setActionListener() {

		binding.btnSimpanDaftarIndividu.setOnClickListener {
			if (isFormValid()) {

				showCustomAlertDialog(
					"Konfirmasi", "Apakah anda yakin data yang anda masukan sudah sesuai?"
				) {
					setLoading(isLoading = true, isSuccess = true)

					with(binding) {
						val judulCapstoneEntered = edtJudulCapstoneIndividu.text.toString().trim()
						// data mahasiswa
						val namaEntered = edtNamaLengkapIndividu.text.toString().trim()
						val nimEntered = edtNimIndividu.text.toString().trim()
						val angkatanEntered = edtAngkatanIndividu.text.toString().trim()
						val jenisKelaminEntered = edtJenisKelaminIndividu.text.toString().trim()
						val noTelpEntered = edtNoTelpIndividu.text.toString().trim()
						val emailEntered = edtEmailIndividu.text.toString().trim()
						val sksEntered = edtSksIndividu.text.toString().trim()
						val ipkEntered = edtIpkIndividu.text.toString().trim()

						// peminatan
						val peminatanEntered = edtSkalaPeminatanIndividu.text.toString().trim()
						val peminatanArray =
							peminatanEntered.split(",").map { it.trim() }.toTypedArray()

						// topik
						val topikEntered = edtSkalaTopikIndividu.text.toString().trim()
						val topikArray = topikEntered.split(",").map { it.trim() }.toTypedArray()

						userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {
								daftarIndividuViewModel.addKelompokIndividu(
									apiToken,
									AddKelompokIndividuRemoteRequestBody(
										idSiklus = selectedIdSiklus,
										userName = namaEntered,
										nomorInduk = nimEntered,
										email = emailEntered,
										angkatan = angkatanEntered,
										jenisKelamin = jenisKelaminEntered,
										ipk = ipkEntered,
										sks = sksEntered,
										noTelp = noTelpEntered,
										judulCapstone = judulCapstoneEntered,
										s = peminatanArray[0],
										e = peminatanArray[1],
										c = peminatanArray[2],
										m = peminatanArray[3],
										ews = topikArray[0],
										bac = topikArray[1],
										smb = topikArray[2],
										smc = topikArray[3],
									),
								)
							}
						}
					}


				}
			} else {
				showSnackbar("Form belum valid!")
			}
		}
	}

	private fun valueObserver() {

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				// get siklus
				siklusViewModel.getSiklus(apiToken)

				// get data mahasiswa
				profileIndexViewModel.getMahasiswaProfile(apiToken)

			}
		}

		siklusViewModel.getSiklusResult.observe(viewLifecycleOwner) { getSiklusResult ->

			val resultResponse = getSiklusResult.payload
			val status = resultResponse?.status

			when (getSiklusResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = false)
				}

				is Resource.Error -> {
					Log.d("Error Siklus", getSiklusResult.payload?.status.toString())

					setLoading(isLoading = false, isSuccess = false)
				}

				is Resource.Success -> {

					if (resultResponse?.success == true) {
						setLoading(isLoading = false, isSuccess = true)

						Log.d("Succes Siklus", status.toString())

						setSiklusDropdown(getSiklusResult)
						if (selectedIdSiklus == "") {
							// Clear the text in edtSiklusIndividu when selectedIdSiklus is empty
							binding.edtSiklusIndividu.text = null
						}

						dataObserver()

					} else {
						setLoading(isLoading = false, isSuccess = false)

						Log.d("Succes Siklus, but failed", status.toString())

						with(binding) {

							if (status == "Token is Expired" || status == "Token is Invalid") {
								showSnackbar("Sesi anda telah berakhir :(")

								actionIfLogoutSucces()
							} else if (resultResponse?.data?.rs_siklus == null) {
								setViewVisibility(linearLayoutDataPendaftaran, false)
								setViewVisibility(cvErrorDaftarIndividu, true)
								tvErrorDaftarIndividu.text = status ?: "Siklus tidak aktif"
							} else if (resultResponse.data.periode_pendaftaran == null) {
								setViewVisibility(linearLayoutDataPendaftaran, false)
								setViewVisibility(cvErrorDaftarIndividu, true)
								tvErrorDaftarIndividu.text =
									status ?: "Belum memasuki periode pendaftaran capstone"

							} else {
								setViewVisibility(linearLayoutDataPendaftaran, false)
								setViewVisibility(cvErrorDaftarIndividu, true)
								tvErrorDaftarIndividu.text =
									status ?: "Belum memasuki periode pendaftaran capstone"

							}
						}

					}
				}

				else -> {}
			}
		}


		daftarIndividuViewModel.addKelompokIndividuResult.observe(viewLifecycleOwner) { addKelompokIndividuResult ->
			val resultResponse = addKelompokIndividuResult.payload
			val status = resultResponse?.status

			when (addKelompokIndividuResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					Log.d("Error Profile Index", resultResponse?.status.toString())

					setLoading(isLoading = false, isSuccess = false)

					showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")

				}

				is Resource.Success -> {

					if (resultResponse?.success == true) {
						setLoading(isLoading = false, isSuccess = true)

						Log.d("Succes status", status.toString())
						showSnackbar(resultResponse.status ?: "Berhasil mendaftar capstone!")

						findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaBerandaFragment)
					} else {
						setLoading(isLoading = false, isSuccess = false)

						showSnackbar(
							resultResponse?.status ?: "Terjadi kesalahan saat mendaftar!")

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

	private fun dataObserver() {
		profileIndexViewModel.getProfileResult.observe(viewLifecycleOwner) { getProfileResult ->

			val resultResponse = getProfileResult.payload
			val status = resultResponse?.status

			when (getProfileResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = false)
				}

				is Resource.Error -> {
					Log.d("Error Profile Index", getProfileResult.payload?.status.toString())

					setLoading(isLoading = false, isSuccess = false)

				}

				is Resource.Success -> {

					if (resultResponse?.success == true && resultResponse.data != null) {
						setLoading(isLoading = false, isSuccess = true)
						Log.d("Succes status", status.toString())

						setInitialValue(getProfileResult)
					} else {
						setLoading(isLoading = false, isSuccess = false)

						Log.d("Succes status, but failed", status.toString())

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

	private fun setSiklusDropdown(getSiklusResult: Resource<SiklusRemoteResponse>) {
		// siklus
		val siklusAdapter =

			getSiklusResult.payload?.data?.let {
				SiklusAdapter(
					requireContext(),
					it.periode_pendaftaran!!
				)
			}

		binding.edtSiklusIndividu.setAdapter(siklusAdapter)

		binding.edtSiklusIndividu.setOnItemClickListener { _, _, position, _ ->
			val selectedSiklus = siklusAdapter?.getItem(position)
			selectedIdSiklus = selectedSiklus?.id.toString()

			if (selectedIdSiklus != "") {
				binding.edtSiklusIndividu.setText(selectedSiklus?.tahunAjaran)
			}
		}
	}

	private fun setInitialValue(getProfileResult: Resource<ProfileRemoteResponse>) {

		// mahasiswa
		with(binding) {
			val dataAkun = getProfileResult.payload?.data
			if (dataAkun != null) {
				edtNamaLengkapIndividu.setText(dataAkun.userName)
				edtNimIndividu.setText(dataAkun.nomorInduk)
				edtEmailIndividu.setText(dataAkun.userEmail)
				edtNoTelpIndividu.setText(dataAkun.noTelp)
			}
		}

		// jenis kelamin
		val listJenisKelamin = listOf(
			JenisKelaminModel(1, "Laki-laki"),
			JenisKelaminModel(2, "Perempuan")
		)
		val jenisKelaminAdapter = JenisKelaminAdapter(requireContext(), listJenisKelamin)
		binding.edtJenisKelaminIndividu.setAdapter(jenisKelaminAdapter)

		binding.edtJenisKelaminIndividu.setOnItemClickListener { _, _, position, _ ->
			val selectedJenisKelamin = jenisKelaminAdapter.getItem(position)

			binding.edtJenisKelaminIndividu.setText(selectedJenisKelamin?.jenisJelamin)
			// Lakukan sesuatu dengan ID yang dipilih
		}

	}


	private fun isFormValid(): Boolean {

		with(binding) {
			var isFormValid = true

			val judulCapstoneEntered = edtJudulCapstoneIndividu.text.toString().trim()
			val siklusCapstoneSelected = edtSiklusIndividu.text.toString().trim()

			// data mahasiswa
			val namaEntered = edtNamaLengkapIndividu.text.toString().trim()
			val nimEntered = edtNimIndividu.text.toString().trim()
			val angkatanEntered = edtAngkatanIndividu.text.toString().trim()
			val jenisKelaminEntered = edtJenisKelaminIndividu.text.toString().trim()
			val noTelpEntered = edtNoTelpIndividu.text.toString().trim()
			val emailEntered = edtEmailIndividu.text.toString().trim()
			val sksEntered = edtSksIndividu.text.toString().trim()
			val ipkEntered = edtIpkIndividu.text.toString().trim()

			// peminatan
			val peminatanEntered = edtSkalaPeminatanIndividu.text.toString().trim()

			// topik
			val topikEntered = edtSkalaTopikIndividu.text.toString().trim()

			// capstone
			if (judulCapstoneEntered.isEmpty()) {
				isFormValid = false
				tilJudulCapstoneIndividu.error = getString(R.string.tv_error_input_blank)
				tilJudulCapstoneIndividu.isErrorEnabled = true
			} else {
				tilJudulCapstoneIndividu.error = null
				tilJudulCapstoneIndividu.isErrorEnabled = false
			}

			if (siklusCapstoneSelected.isEmpty()) {
				isFormValid = false
				tilSiklusIndividu.error = getString(R.string.tv_error_input_blank)
				tilSiklusIndividu.isErrorEnabled = true
			} else {
				tilSiklusIndividu.error = null
				tilSiklusIndividu.isErrorEnabled = false
			}

			// MAHASISWA
			if (namaEntered.isEmpty()) {
				isFormValid = false
				tilNamaIndividu.error = getString(R.string.tv_error_input_blank)
				tilNamaIndividu.isErrorEnabled = true
			} else {
				tilNamaIndividu.error = null
				tilNamaIndividu.isErrorEnabled = false
			}

			if (nimEntered.isEmpty()) {
				isFormValid = false
				tilNimIndividu.error = getString(R.string.tv_error_input_blank)
				tilNimIndividu.isErrorEnabled = true
			} else if (nimEntered.length != 14 && !nimEntered.all { it.isDigit() }) {
				isFormValid = false
				tilNimIndividu.error = getString(R.string.nim_tidak_valid)
				tilNimIndividu.isErrorEnabled = true
			} else {
				tilNimIndividu.error = null
				tilNimIndividu.isErrorEnabled = false
			}

			if (angkatanEntered.isEmpty()) {
				isFormValid = false
				tilAngkatanIndividu.error = getString(R.string.tv_error_input_blank)
				tilAngkatanIndividu.isErrorEnabled = true
			} else {
				tilAngkatanIndividu.error = null
				tilAngkatanIndividu.isErrorEnabled = false
			}

			if (jenisKelaminEntered.isEmpty()) {
				isFormValid = false
				tilJenisKelaminIndividu.error = getString(R.string.tv_error_input_blank)
				tilJenisKelaminIndividu.isErrorEnabled = true
			} else {
				tilJenisKelaminIndividu.error = null
				tilJenisKelaminIndividu.isErrorEnabled = false
			}

			if (noTelpEntered.isEmpty()) {
				isFormValid = false
				tilNoTelpIndividu.error = getString(R.string.tv_error_input_blank)
				tilNoTelpIndividu.isErrorEnabled = true
			} else if (noTelpEntered.length !in 10..14) {
				isFormValid = false
				tilNoTelpIndividu.error = getString(R.string.no_telp_tidak_valid)
				tilNoTelpIndividu.isErrorEnabled = true
			} else {
				tilNoTelpIndividu.error = null
				tilNoTelpIndividu.isErrorEnabled = false
			}

			val emailPattern: Regex = Patterns.EMAIL_ADDRESS.toRegex()
			if (emailEntered.isEmpty()) {
				isFormValid = false
				tilEmailIndividu.error = getString(R.string.tv_error_input_blank)
				tilEmailIndividu.isErrorEnabled = true
			} else if (!emailPattern.matches(emailEntered)) {
				isFormValid = false
				tilEmailIndividu.error = getString(R.string.email_tidak_valid)
				tilEmailIndividu.isErrorEnabled = true
			} else {
				tilEmailIndividu.error = null
				tilEmailIndividu.isErrorEnabled = false
			}

			if (sksEntered.isEmpty()) {
				isFormValid = false
				tilSksIndividu.error = getString(R.string.tv_error_input_blank)
				tilSksIndividu.isErrorEnabled = true
			} else if (sksEntered.length !in 1..3) {
				isFormValid = false
				tilSksIndividu.error = getString(R.string.sks_tidak_valid)
				tilSksIndividu.isErrorEnabled = true
			} else {
				tilSksIndividu.error = null
				tilSksIndividu.isErrorEnabled = false
			}

			if (ipkEntered.isEmpty()) {
				isFormValid = false
				tilIpkIndividu.error = getString(R.string.tv_error_input_blank)
				tilIpkIndividu.isErrorEnabled = true
			} else {
				val ipkRegex = """^(?:0|[1-3](?:\.\d{2})|4(?:\.00?)?)$""".toRegex()
				if (!ipkEntered.matches(ipkRegex)) {
					isFormValid = false
					tilIpkIndividu.error = getString(R.string.ipk_tidak_valid)
					tilIpkIndividu.isErrorEnabled = true
				} else {
					tilIpkIndividu.error = null
					tilIpkIndividu.isErrorEnabled = false
				}
			}

			// peminatan
			if (peminatanEntered.isEmpty()) {
				isFormValid = false
				tilSkalaPeminatanIndividu.error = getString(R.string.tv_error_input_blank)
				tilSkalaPeminatanIndividu.isErrorEnabled = true
			} else if (!isValidPeminatan(peminatanEntered)) {
				isFormValid = false
				tilSkalaPeminatanIndividu.error = getString(R.string.peminatan_tidak_valid)
				tilSkalaPeminatanIndividu.isErrorEnabled = true
			} else {
				tilSkalaPeminatanIndividu.error = null
				tilSkalaPeminatanIndividu.isErrorEnabled = false
			}

			// topik
			if (topikEntered.isEmpty()) {
				isFormValid = false
				tilSkalaTopikIndividu.error = getString(R.string.tv_error_input_blank)
				tilSkalaTopikIndividu.isErrorEnabled = true
			} else if (!isValidPeminatan(topikEntered)) {
				isFormValid = false
				tilSkalaTopikIndividu.error = getString(R.string.topik_tidak_valid)
				tilSkalaTopikIndividu.isErrorEnabled = true
			} else {
				tilSkalaTopikIndividu.error = null
				tilSkalaTopikIndividu.isErrorEnabled = false
			}

			return isFormValid
		}
	}

	private fun isValidPeminatan(peminatan: String): Boolean {
		val peminatanList = peminatan.split(",").map { it.trim().toIntOrNull() }
		return peminatanList.size == 4 && peminatanList.all { it in 1..4 } && peminatanList.toSet().size == 4
	}

	private fun isValidIPK(ipk: String): Boolean {
		return ipk.isNotEmpty() && try {
			val ipkValue = ipk.toDouble()
			val decimalCount = getDecimalCount(ipk)
			ipkValue in 0.00..4.00 && decimalCount <= 2
		} catch (e: NumberFormatException) {
			false
		}
	}

	private fun getDecimalCount(value: String): Int {
		val decimalIndex = value.indexOf('.')
		return if (decimalIndex == -1) {
			0
		} else {
			value.length - decimalIndex - 1
		}
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaKelompokDaftarIndividuFragment

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

	private fun setLoading(isLoading: Boolean, isSuccess: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerFragmentDaftarIndividu, isLoading)

			setViewVisibility(linearLayoutDataPendaftaran, false)

			// card error
			setViewVisibility(cvErrorDaftarIndividu, false)

			if (!isLoading) {
				if (isSuccess) {
					setViewVisibility(linearLayoutDataPendaftaran, true)

					// card error
					setViewVisibility(cvErrorDaftarIndividu, false)


				} else {
					setViewVisibility(linearLayoutDataPendaftaran, false)

					// card error
					setViewVisibility(cvErrorDaftarIndividu, true)

				}
			}
		}
	}

	private fun setShimmerVisibility(shimmerView: View, isLoading: Boolean) {
		shimmerView.visibility = if (isLoading) View.VISIBLE else View.GONE
		(shimmerView as? ShimmerFrameLayout)?.run {
			if (isLoading) startShimmer() else stopShimmer()
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}