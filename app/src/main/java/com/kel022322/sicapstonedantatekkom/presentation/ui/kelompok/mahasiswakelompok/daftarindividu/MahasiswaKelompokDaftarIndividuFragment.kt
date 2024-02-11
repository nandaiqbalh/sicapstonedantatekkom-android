package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.daftarindividu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.local.model.jeniskelamin.JenisKelaminModel
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokDaftarIndividuBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.KelompokSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.SiklusAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MahasiswaKelompokDaftarIndividuFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokDaftarIndividuBinding? = null
	private val binding get() = _binding!!

	private val kelompokViewModel: KelompokSayaViewModel by viewModels()
	private val profileViewModel: ProfileSayaViewModel by viewModels()

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

					profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						if (userId != null) {
							profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
								apiToken?.let {
									kelompokViewModel.addKelompokIndividu(
										AddKelompokIndividuRemoteRequestBody(
											userId = userId,
											apiToken = apiToken,
											idSiklus = selectedIdSiklus,
											userName = namaEntered,
											nomorInduk = nimEntered,
											email = emailEntered,
											angkatan = angkatanEntered,
											jenisKelamin = jenisKelaminEntered,
											ipk = ipkEntered,
											sks = sksEntered,
											noTelp = noTelpEntered,
											alamat = "",
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
				}
			} else {
				showSnackbar("Form belum valid!")
			}
		}
	}

	private fun valueObserver() {

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						kelompokViewModel.getKelompokSaya(
							KelompokSayaRemoteRequestBody(
								userId,
								apiToken
							)
						)

					}
				}
			}
		}

		kelompokViewModel.getKelompokSayaResult.observe(viewLifecycleOwner) { getKelompokSayaResult ->

			when (getKelompokSayaResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getKelompokSayaResult.toString())

				}

				is Resource.Success -> {
					setLoading(isLoading = false, isSuccess = true)



					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())

					if (getKelompokSayaResult.payload?.status == true) {

						if (getKelompokSayaResult.payload.data?.kelompok == null) {
							setInitialValue(getKelompokSayaResult)

						}

					}

				}

				else -> {}
			}
		}

		kelompokViewModel.addKelompokIndividuResult.observe(viewLifecycleOwner) { addKelompokIndividuResult ->

			when (addKelompokIndividuResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", addKelompokIndividuResult.toString())
					showSnackbar("Gagal mendaftar capstone!")

				}

				is Resource.Success -> {
					setLoading(isLoading = false, isSuccess = true)

					val message = addKelompokIndividuResult.payload?.message
					Log.d("Result success", message.toString())

					if (addKelompokIndividuResult.payload?.status == true) {
						showSnackbar("Berhasil mendaftar capstone!")
						findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaBerandaFragment)
					} else {
						showSnackbar(message?: "Terjadi kesalahan saat mendaftar!")
					}

				}

				else -> {}
			}
		}
	}

	private fun setInitialValue(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		// siklus
		val siklusAdapter =
			getKelompokSayaResult.payload?.data?.rsSiklus?.let {
				SiklusAdapter(
					requireContext(),
					it
				)
			}
		binding.edtSiklusIndividu.setAdapter(siklusAdapter)

		binding.edtSiklusIndividu.setOnItemClickListener { _, _, position, _ ->
			val selectedSiklus = siklusAdapter?.getItem(position)
			selectedIdSiklus = selectedSiklus?.id.toString()

			binding.edtSiklusIndividu.setText(selectedSiklus?.tahunAjaran)
			// Lakukan sesuatu dengan ID yang dipilih
		}

		// mahasiswa
		with(binding) {
			val dataAkun = getKelompokSayaResult.payload?.data?.getAkun
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
			} else if (!isValidIPK(ipkEntered)) {
				isFormValid = false
				tilIpkIndividu.error = getString(R.string.ipk_tidak_valid)
				tilIpkIndividu.isErrorEnabled = true
			} else {
				tilIpkIndividu.error = null
				tilIpkIndividu.isErrorEnabled = false
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
				requireActivity().findViewById(android.R.id.content),
				message,
				"OK"
			) {
				customSnackbar.dismissSnackbar()
				if (message == "Berhasil keluar!" || message == "Gagal! Anda telah masuk melalui perangkat lain." || message == "Pengguna tidak ditemukan!" || message == "Akses tidak sah!" || message == "Sesi anda telah berakhir, silahkan masuk terlebih dahulu.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setUsername("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				} else if (message == "Gagal mendaftar capstone!" || message == "Berhasil mendaftar capstone!" || message == "null" || message.equals(
						null
					) || message == "Terjadi kesalahan!"
				) {
					restartFragment()
				} else if (message == "Password berhasil diubah, silahkan masuk kembali.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setUsername("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				}
			}
		}

	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaKelompokDaftarIndividuFragment

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

	private fun setLoading(isLoading: Boolean, isSuccess: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerFragmentDaftarIndividu, isLoading)

			cvDetailCapstoneIndividu.visibility =  View.GONE
			tvTitleDetailCapstoneIndividu.visibility =  View.GONE

			tvDetailMahasiswaIndividu.visibility = View.GONE
			cvDetailMahasiswaIndividu.visibility =  View.GONE

			tvTitleDetailPeminatanIndividu.visibility = View.GONE
			cvPeminatanIndividu.visibility =  View.GONE

			tvTitleDetailTopikIndividu.visibility = View.GONE
			cvTopikIndividu.visibility =  View.GONE

			btnSimpanDaftarIndividu.visibility = View.GONE

			cvErrorDaftarIndividu.visibility =  View.GONE

			if (!isLoading){
				if (isSuccess){
					cvDetailCapstoneIndividu.visibility =  View.VISIBLE
					tvTitleDetailCapstoneIndividu.visibility =  View.VISIBLE

					tvDetailMahasiswaIndividu.visibility = View.VISIBLE
					cvDetailMahasiswaIndividu.visibility =  View.VISIBLE

					tvTitleDetailPeminatanIndividu.visibility = View.VISIBLE
					cvPeminatanIndividu.visibility =  View.VISIBLE

					tvTitleDetailTopikIndividu.visibility = View.VISIBLE
					cvTopikIndividu.visibility =  View.VISIBLE

					btnSimpanDaftarIndividu.visibility = View.VISIBLE

					cvErrorDaftarIndividu.visibility =  View.GONE

				} else {
					cvDetailCapstoneIndividu.visibility =  View.GONE
					tvTitleDetailCapstoneIndividu.visibility =  View.GONE

					tvDetailMahasiswaIndividu.visibility = View.GONE
					cvDetailMahasiswaIndividu.visibility =  View.GONE

					tvTitleDetailPeminatanIndividu.visibility = View.GONE
					cvPeminatanIndividu.visibility =  View.GONE

					tvTitleDetailTopikIndividu.visibility = View.GONE
					cvTopikIndividu.visibility =  View.GONE

					btnSimpanDaftarIndividu.visibility = View.GONE

					cvErrorDaftarIndividu.visibility =  View.VISIBLE
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}