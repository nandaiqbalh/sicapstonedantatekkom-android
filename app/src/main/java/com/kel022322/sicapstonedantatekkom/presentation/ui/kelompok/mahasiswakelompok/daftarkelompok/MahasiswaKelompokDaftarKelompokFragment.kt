package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.daftarkelompok

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
import com.google.android.material.textfield.TextInputLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.local.model.jeniskelamin.JenisKelaminModel
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.request.DosenRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.request.MahasiswaIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokDaftarKelompokBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.KelompokSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminDuaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminTigaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.ListDosenAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.ListDosenDuaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.ListMahasiswaDuaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.SiklusAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.TopikAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MahasiswaKelompokDaftarKelompokFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokDaftarKelompokBinding? = null
	private val binding get() = _binding!!

	private val kelompokViewModel: KelompokSayaViewModel by viewModels()
	private val profileViewModel: ProfileSayaViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	private var selectedIdTopik = ""
	private var selectedIdSiklus = ""
	private var selectedIdDosbing1 = ""
	private var selectedIdDosbing2 = ""
	private var selectedIdMahasiswa2 = ""
	private var selectedIdMahasiswa3 = ""

	private var topikCapstoneEntered = ""
	private var judulCapstoneEntered = ""
	private var siklusCapstoneSelected = ""
	private var dosbing1CapstoneEntered = ""
	private var dosbing2CapstoneEntered = ""

	// data mahasiswa 1
	private var namaMahasiswa1Entered = ""
	private var nimMahasiswa1Entered = ""
	private var angkatanMahasiswa1Entered = ""
	private var jenisKelaminMahasiswa1Entered = ""
	private var noTelpMahasiswa1Entered = ""
	private var emailMahasiswa1Entered = ""
	private var sksMahasiswa1Entered = ""
	private var ipkMahasiswa1Entered = ""

	// data mahasiswa 2
	private var namaMahasiswa2Entered = ""
	private var nimMahasiswa2Entered = ""
	private var angkatanMahasiswa2Entered = ""
	private var jenisKelaminMahasiswa2Entered = ""
	private var noTelpMahasiswa2Entered = ""
	private var emailMahasiswa2Entered = ""
	private var sksMahasiswa2Entered = ""
	private var ipkMahasiswa2Entered = ""

	// data mahasiswa 3
	private var namaMahasiswa3Entered = ""
	private var nimMahasiswa3Entered = ""
	private var angkatanMahasiswa3Entered = ""
	private var jenisKelaminMahasiswa3Entered = ""
	private var noTelpMahasiswa3Entered = ""
	private var emailMahasiswa3Entered = ""
	private var sksMahasiswa3Entered = ""
	private var ipkMahasiswa3Entered = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment

		_binding =
			FragmentMahasiswaKelompokDaftarKelompokBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		valueObserver()

		setActionListener()
	}

	private fun setActionListener() {

		with(binding) {
			topikCapstoneEntered = edtTopikCapstoneKelompok.text.toString().trim()
			judulCapstoneEntered = edtJudulCapstoneKelompok.text.toString().trim()
			siklusCapstoneSelected = edtSiklusKelompok.text.toString().trim()
			dosbing1CapstoneEntered = edtDosbing1Kelompok.text.toString().trim()
			dosbing2CapstoneEntered = edtDosbing2Kelompok.text.toString().trim()

			// data mahasiswa 1
			namaMahasiswa1Entered = edtNamaLengkapMahasiswa1.text.toString().trim()
			nimMahasiswa1Entered = edtNimMahasiswa1.text.toString().trim()
			angkatanMahasiswa1Entered = edtAngkatanMahasiswa1.text.toString().trim()
			jenisKelaminMahasiswa1Entered = edtJenisKelaminMahasiswa1.text.toString().trim()
			noTelpMahasiswa1Entered = edtNoTelpMahasiswa1.text.toString().trim()
			emailMahasiswa1Entered = edtEmailMahasiswa1.text.toString().trim()
			sksMahasiswa1Entered = edtSksMahasiswa1.text.toString().trim()
			ipkMahasiswa1Entered = edtIpkMahasiswa1.text.toString().trim()

			// data mahasiswa 1
			namaMahasiswa2Entered = edtNamaLengkapMahasiswa2.text.toString().trim()
			nimMahasiswa2Entered = edtNimMahasiswa2.text.toString().trim()
			angkatanMahasiswa2Entered = edtAngkatanMahasiswa2.text.toString().trim()
			jenisKelaminMahasiswa2Entered = edtJenisKelaminMahasiswa2.text.toString().trim()
			noTelpMahasiswa2Entered = edtNoTelpMahasiswa2.text.toString().trim()
			emailMahasiswa2Entered = edtEmailMahasiswa2.text.toString().trim()
			sksMahasiswa2Entered = edtSksMahasiswa2.text.toString().trim()
			ipkMahasiswa2Entered = edtIpkMahasiswa2.text.toString().trim()

			// data mahasiswa 1
			namaMahasiswa3Entered = edtNamaLengkapMahasiswa3.text.toString().trim()
			nimMahasiswa3Entered = edtNimMahasiswa3.text.toString().trim()
			angkatanMahasiswa3Entered = edtAngkatanMahasiswa3.text.toString().trim()
			jenisKelaminMahasiswa3Entered = edtJenisKelaminMahasiswa3.text.toString().trim()
			noTelpMahasiswa3Entered = edtNoTelpMahasiswa3.text.toString().trim()
			emailMahasiswa3Entered = edtEmailMahasiswa3.text.toString().trim()
			sksMahasiswa3Entered = edtSksMahasiswa3.text.toString().trim()
			ipkMahasiswa3Entered = edtIpkMahasiswa3.text.toString().trim()

			btnSimpanDaftarKelompok.setOnClickListener {
				if (isFormValid()) {
					setLoading(isLoading = true, isSuccess = true)

					profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
						if (userId != null) {
							profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
								apiToken?.let {
									kelompokViewModel.addKelompokPunyaKelompok(
										AddKelompokPunyaKelompokRemoteRequestBody(
											userId = userId,
											apiToken = apiToken,
											idSiklus = selectedIdSiklus,
											email = emailMahasiswa1Entered,
											angkatan = angkatanMahasiswa1Entered,
											judulCapstone = judulCapstoneEntered,
											idTopik = selectedIdTopik,
											dosbingSatu = selectedIdDosbing1,
											dosbingDua = selectedIdDosbing2,
											alamat = "",

											angkatanSatu = angkatanMahasiswa1Entered,
											emailSatu = emailMahasiswa1Entered,
											jenisKelaminSatu = jenisKelaminMahasiswa1Entered,
											ipkSatu = ipkMahasiswa1Entered,
											sksSatu = sksMahasiswa1Entered,
											noTelpSatu = noTelpMahasiswa1Entered,
											alamatSatu = "",

											angkatanDua = angkatanMahasiswa2Entered,
											emailDua = emailMahasiswa2Entered,
											jenisKelaminDua = jenisKelaminMahasiswa2Entered,
											ipkDua = ipkMahasiswa2Entered,
											sksDua = sksMahasiswa2Entered,
											noTelpDua = noTelpMahasiswa2Entered,
											alamatDua = "",

											angkatanTiga = angkatanMahasiswa3Entered,
											emailTiga = emailMahasiswa3Entered,
											jenisKelaminTiga = jenisKelaminMahasiswa3Entered,
											ipkTiga = ipkMahasiswa3Entered,
											sksTiga = sksMahasiswa3Entered,
											noTelpTiga = noTelpMahasiswa3Entered,
											alamatTiga = "",

											namaSatu = userId,
											namaDua = selectedIdMahasiswa2,
											namaTiga = selectedIdMahasiswa3
										)
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

						kelompokViewModel.getDataDosen(
							DosenRemoteRequestBody(
								userId,
								apiToken
							)
						)

						kelompokViewModel.getDataMahasiswa(
							MahasiswaIndexRemoteRequestBody(
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

		kelompokViewModel.addKelompokPunyaKelompok.observe(viewLifecycleOwner) { addKelompokPunyaKelompok ->

			when (addKelompokPunyaKelompok) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", addKelompokPunyaKelompok.toString())
					showSnackbar("Gagal mendaftar capstone!")

				}

				is Resource.Success -> {
					setLoading(isLoading = false, isSuccess = true)
					val message = addKelompokPunyaKelompok.payload?.message

					if (addKelompokPunyaKelompok.payload?.status == true) {
						showSnackbar("Berhasil mendaftar capstone!")
						findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaBerandaFragment)
					} else {
						showSnackbar(message?: "Terjadi kesalahan saat mendaftar!")
					}

				}

				else -> {}
			}
		}

		kelompokViewModel.getDataDosenResult.observe(viewLifecycleOwner) { getDataDosenResult ->

			when (getDataDosenResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getDataDosenResult.message.toString())

				}

				is Resource.Success -> {
					setLoading(isLoading = false, isSuccess = true)

					val message = getDataDosenResult.payload?.message
					Log.d("Result success", message.toString())

					if (getDataDosenResult.payload?.status == true) {

						if (getDataDosenResult.payload.data != null) {
							setDropdownDosen(getDataDosenResult)

						}

					}

				}

				else -> {}
			}
		}

		kelompokViewModel.getDataMahasiswaResult.observe(viewLifecycleOwner) { getDataMahasiswaResult ->

			when (getDataMahasiswaResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getDataMahasiswaResult.message.toString())

				}

				is Resource.Success -> {
					setLoading(isLoading = false, isSuccess = true)

					val message = getDataMahasiswaResult.payload?.message
					Log.d("Result success", message.toString())

					if (getDataMahasiswaResult.payload?.status == true) {

						if (getDataMahasiswaResult.payload.data != null) {
							setDropdownMahasiswa(getDataMahasiswaResult)

						}

					}

				}

				else -> {}
			}
		}
	}

	private fun setInitialValue(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		// topik
		val topikAdapter =
			getKelompokSayaResult.payload?.data?.rsTopik?.let {
				TopikAdapter(
					requireContext(),
					it
				)
			}
		binding.edtTopikCapstoneKelompok.setAdapter(topikAdapter)

		binding.edtTopikCapstoneKelompok.setOnItemClickListener { _, _, position, _ ->
			val selectedTopik = topikAdapter?.getItem(position)
			selectedIdTopik = selectedTopik?.id.toString()

			binding.edtTopikCapstoneKelompok.setText(selectedTopik?.nama)

			// Clear focus and show dropdown
			binding.edtTopikCapstoneKelompok.showDropDown()
			Log.d("EDT", "EDT CLICKED!")
		}

		// siklus
		val siklusAdapter =
			getKelompokSayaResult.payload?.data?.rsSiklus?.let {
				SiklusAdapter(
					requireContext(),
					it
				)
			}
		binding.edtSiklusKelompok.setAdapter(siklusAdapter)

		binding.edtSiklusKelompok.setOnItemClickListener { _, _, position, _ ->
			val selectedSiklus = siklusAdapter?.getItem(position)
			selectedIdSiklus = selectedSiklus?.id.toString()

			binding.edtSiklusKelompok.setText(selectedSiklus?.tahunAjaran)
			// Lakukan sesuatu dengan ID yang dipilih
		}

		// mahasiswa
		with(binding) {
			val dataAkun = getKelompokSayaResult.payload?.data?.getAkun
			if (dataAkun != null) {
				edtNamaLengkapMahasiswa1.setText(dataAkun.userName)
				edtNimMahasiswa1.setText(dataAkun.nomorInduk)
				edtEmailMahasiswa1.setText(dataAkun.userEmail)
				edtNoTelpMahasiswa1.setText(dataAkun.noTelp)
			}
		}

		val listJenisKelamin = listOf(
			JenisKelaminModel(1, "Laki-laki"),
			JenisKelaminModel(2, "Perempuan")
		)

		// jenis kelamin mahasiswa 1
		val jenisKelaminAdapter = JenisKelaminAdapter(requireContext(), listJenisKelamin)
		binding.edtJenisKelaminMahasiswa1.setAdapter(jenisKelaminAdapter)

		binding.edtJenisKelaminMahasiswa1.setOnItemClickListener { _, _, position, _ ->
			val selectedJenisKelamin = jenisKelaminAdapter.getItem(position)

			binding.edtJenisKelaminMahasiswa1.setText(selectedJenisKelamin?.jenisJelamin)
			// Lakukan sesuatu dengan ID yang dipilih
		}

		// jenis kelamin mahasiswa 2
		val jenisKelamin2Adapter = JenisKelaminDuaAdapter(requireContext(), listJenisKelamin)
		binding.edtJenisKelaminMahasiswa2.setAdapter(jenisKelamin2Adapter)

		binding.edtJenisKelaminMahasiswa2.setOnItemClickListener { _, _, position, _ ->
			val selectedJenisKelamin = jenisKelamin2Adapter.getItem(position)

			binding.edtJenisKelaminMahasiswa2.setText(selectedJenisKelamin?.jenisJelamin)
			// Lakukan sesuatu dengan ID yang dipilih
		}

		// jenis kelamin mahasiswa 3
		val jenisKelamin3Adapter = JenisKelaminTigaAdapter(requireContext(), listJenisKelamin)
		binding.edtJenisKelaminMahasiswa3.setAdapter(jenisKelamin3Adapter)

		binding.edtJenisKelaminMahasiswa3.setOnItemClickListener { _, _, position, _ ->
			val selectedJenisKelamin = jenisKelamin3Adapter.getItem(position)

			binding.edtJenisKelaminMahasiswa3.setText(selectedJenisKelamin?.jenisJelamin)
			// Lakukan sesuatu dengan ID yang dipilih
		}

	}

	private fun setDropdownDosen(getDataDosenResult: Resource<DosenRemoteResponse>) {

		// dosen
		val dosbing1Adapter =
			getDataDosenResult.payload?.data?.rs_dosen?.let {
				ListDosenAdapter(
					requireContext(),
					it
				)
			}
		binding.edtDosbing1Kelompok.setAdapter(dosbing1Adapter)

		binding.edtDosbing1Kelompok.setOnItemClickListener { _, _, position, _ ->
			val selectedDosbing1 = dosbing1Adapter?.getItem(position)
			selectedIdDosbing1 = selectedDosbing1?.userId.toString()

			binding.edtDosbing1Kelompok.setText(selectedDosbing1?.userName)
		}

		// dosen
		val dosbing2Adapter =
			getDataDosenResult.payload?.data?.rs_dosen?.let {
				ListDosenDuaAdapter(
					requireContext(),
					it
				)
			}
		binding.edtDosbing2Kelompok.setAdapter(dosbing2Adapter)

		binding.edtDosbing2Kelompok.setOnItemClickListener { _, _, position, _ ->
			val selectedDosbing2 = dosbing2Adapter?.getItem(position)
			selectedIdDosbing2 = selectedDosbing2?.userId.toString()

			binding.edtDosbing2Kelompok.setText(selectedDosbing2?.userName)
			// Lakukan sesuatu dengan ID yang dipilih
		}
	}

	private fun setDropdownMahasiswa(getDataMahasiswaResult: Resource<MahasiswaIndexRemoteResponse>) {

		// mahasiswa
		val mahasiswa2Adapter =
			getDataMahasiswaResult.payload?.data?.rs_mahasiswa?.let {
				ListMahasiswaDuaAdapter(
					requireContext(),
					it
				)
			}
		binding.edtNamaLengkapMahasiswa2.setAdapter(mahasiswa2Adapter)

		binding.edtNamaLengkapMahasiswa2.setOnItemClickListener { _, _, position, _ ->
			val selectedMahasiswa2 = mahasiswa2Adapter?.getItem(position)
			selectedIdMahasiswa2 = selectedMahasiswa2?.userId.toString()

			binding.edtNamaLengkapMahasiswa2.setText(selectedMahasiswa2?.userName)
		}

		// mahasiswa3
		val mahasiswa3Adapter =
			getDataMahasiswaResult.payload?.data?.rs_mahasiswa?.let {
				ListMahasiswaDuaAdapter(
					requireContext(),
					it
				)
			}
		binding.edtNamaLengkapMahasiswa3.setAdapter(mahasiswa3Adapter)

		binding.edtNamaLengkapMahasiswa3.setOnItemClickListener { _, _, position, _ ->
			val selectedMahasiswa3 = mahasiswa3Adapter?.getItem(position)
			selectedIdMahasiswa3 = selectedMahasiswa3?.userId.toString()

			binding.edtNamaLengkapMahasiswa3.setText(selectedMahasiswa3?.userName)
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

	private fun isFormValid(): Boolean {
		with(binding) {

			topikCapstoneEntered = edtTopikCapstoneKelompok.text.toString().trim()
			judulCapstoneEntered = edtJudulCapstoneKelompok.text.toString().trim()
			siklusCapstoneSelected = edtSiklusKelompok.text.toString().trim()
			dosbing1CapstoneEntered = edtDosbing1Kelompok.text.toString().trim()
			dosbing2CapstoneEntered = edtDosbing2Kelompok.text.toString().trim()

			// data mahasiswa 1
			namaMahasiswa1Entered = edtNamaLengkapMahasiswa1.text.toString().trim()
			nimMahasiswa1Entered = edtNimMahasiswa1.text.toString().trim()
			angkatanMahasiswa1Entered = edtAngkatanMahasiswa1.text.toString().trim()
			jenisKelaminMahasiswa1Entered = edtJenisKelaminMahasiswa1.text.toString().trim()
			noTelpMahasiswa1Entered = edtNoTelpMahasiswa1.text.toString().trim()
			emailMahasiswa1Entered = edtEmailMahasiswa1.text.toString().trim()
			sksMahasiswa1Entered = edtSksMahasiswa1.text.toString().trim()
			ipkMahasiswa1Entered = edtIpkMahasiswa1.text.toString().trim()

			// data mahasiswa 1
			namaMahasiswa2Entered = edtNamaLengkapMahasiswa2.text.toString().trim()
			nimMahasiswa2Entered = edtNimMahasiswa2.text.toString().trim()
			angkatanMahasiswa2Entered = edtAngkatanMahasiswa2.text.toString().trim()
			jenisKelaminMahasiswa2Entered = edtJenisKelaminMahasiswa2.text.toString().trim()
			noTelpMahasiswa2Entered = edtNoTelpMahasiswa2.text.toString().trim()
			emailMahasiswa2Entered = edtEmailMahasiswa2.text.toString().trim()
			sksMahasiswa2Entered = edtSksMahasiswa2.text.toString().trim()
			ipkMahasiswa2Entered = edtIpkMahasiswa2.text.toString().trim()

			// data mahasiswa 1
			namaMahasiswa3Entered = edtNamaLengkapMahasiswa3.text.toString().trim()
			nimMahasiswa3Entered = edtNimMahasiswa3.text.toString().trim()
			angkatanMahasiswa3Entered = edtAngkatanMahasiswa3.text.toString().trim()
			jenisKelaminMahasiswa3Entered = edtJenisKelaminMahasiswa3.text.toString().trim()
			noTelpMahasiswa3Entered = edtNoTelpMahasiswa3.text.toString().trim()
			emailMahasiswa3Entered = edtEmailMahasiswa3.text.toString().trim()
			sksMahasiswa3Entered = edtSksMahasiswa3.text.toString().trim()
			ipkMahasiswa3Entered = edtIpkMahasiswa3.text.toString().trim()

			var isFormValid = true

			Log.d("EDTTEXT", topikCapstoneEntered)

			// capstone
			isFormValid = validateField(
				topikCapstoneEntered,
				tilTopikCapstoneKelompok,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				judulCapstoneEntered,
				tilJudulCapstoneKelompok,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				siklusCapstoneSelected,
				tilSiklusKelompok,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				dosbing1CapstoneEntered,
				tilDosbing1Kelompok,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				dosbing2CapstoneEntered,
				tilDosbing2Kelompok,
				R.string.tv_error_input_blank
			) && isFormValid

			// Validasi kedua field tidak boleh bernilai sama
			if (dosbing1CapstoneEntered == dosbing2CapstoneEntered) {
				isFormValid = false
				tilDosbing1Kelompok.error = getString(R.string.tv_error_duplicate_values)
				tilDosbing1Kelompok.isErrorEnabled = true

				tilDosbing2Kelompok.error = getString(R.string.tv_error_duplicate_values)
				tilDosbing2Kelompok.isErrorEnabled = true
			} else {
				tilDosbing1Kelompok.error = null
				tilDosbing1Kelompok.isErrorEnabled = false

				tilDosbing2Kelompok.error = null
				tilDosbing2Kelompok.isErrorEnabled = false
			}

			val emailPattern: Regex = Patterns.EMAIL_ADDRESS.toRegex()

			// MAHASISWA 1
			isFormValid = validateField(
				namaMahasiswa1Entered,
				tilNamaMahasiswa1,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				nimMahasiswa1Entered,
				tilNimMahasiswa1,
				R.string.tv_error_input_blank,
				R.string.nim_tidak_valid,
				14..14
			) && isFormValid
			isFormValid = validateField(
				angkatanMahasiswa1Entered,
				tilAngkatanMahasiswa1,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				jenisKelaminMahasiswa1Entered,
				tilJenisKelaminMahasiswa1,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				noTelpMahasiswa1Entered,
				tilNoTelpMahasiswa1,
				R.string.tv_error_input_blank,
				R.string.no_telp_tidak_valid,
				10..14
			) && isFormValid
			isFormValid = validateField(
				emailMahasiswa1Entered,
				tilEmailMahasiswa1,
				R.string.tv_error_input_blank,
				R.string.email_tidak_valid
			) { emailPattern.matches(it) } && isFormValid
			isFormValid = validateField(
				sksMahasiswa1Entered,
				tilSksMahasiswa1,
				R.string.tv_error_input_blank,
				R.string.sks_tidak_valid,
				1..3
			) && isFormValid
			isFormValid = validateField(
				ipkMahasiswa1Entered,
				tilIpkMahasiswa1,
				R.string.tv_error_input_blank,
				R.string.ipk_tidak_valid
			) { isValidIPK(it) } && isFormValid

			// MAHASISWA 2
			isFormValid = validateField(
				namaMahasiswa2Entered,
				tilNamaMahasiswa2,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				nimMahasiswa2Entered,
				tilNimMahasiswa2,
				R.string.tv_error_input_blank,
				R.string.nim_tidak_valid,
				14..14
			) && isFormValid
			isFormValid = validateField(
				angkatanMahasiswa2Entered,
				tilAngkatanMahasiswa2,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				jenisKelaminMahasiswa2Entered,
				tilJenisKelaminMahasiswa2,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				noTelpMahasiswa2Entered,
				tilNoTelpMahasiswa2,
				R.string.tv_error_input_blank,
				R.string.no_telp_tidak_valid,
				10..14
			) && isFormValid
			isFormValid = validateField(
				emailMahasiswa2Entered,
				tilEmailMahasiswa2,
				R.string.tv_error_input_blank,
				R.string.email_tidak_valid
			) { emailPattern.matches(it) } && isFormValid
			isFormValid = validateField(
				sksMahasiswa2Entered,
				tilSksMahasiswa2,
				R.string.tv_error_input_blank,
				R.string.sks_tidak_valid,
				1..3
			) && isFormValid
			isFormValid = validateField(
				ipkMahasiswa2Entered,
				tilIpkMahasiswa2,
				R.string.tv_error_input_blank,
				R.string.ipk_tidak_valid
			) { isValidIPK(it) } && isFormValid

			// MAHASISWA 3
			isFormValid = validateField(
				namaMahasiswa3Entered,
				tilNamaMahasiswa3,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				nimMahasiswa3Entered,
				tilNimMahasiswa3,
				R.string.tv_error_input_blank,
				R.string.nim_tidak_valid,
				14..14
			) && isFormValid
			isFormValid = validateField(
				angkatanMahasiswa3Entered,
				tilAngkatanMahasiswa3,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				jenisKelaminMahasiswa3Entered,
				tilJenisKelaminMahasiswa3,
				R.string.tv_error_input_blank
			) && isFormValid
			isFormValid = validateField(
				noTelpMahasiswa3Entered,
				tilNoTelpMahasiswa3,
				R.string.tv_error_input_blank,
				R.string.no_telp_tidak_valid,
				10..14
			) && isFormValid
			isFormValid = validateField(
				emailMahasiswa3Entered,
				tilEmailMahasiswa3,
				R.string.tv_error_input_blank,
				R.string.email_tidak_valid
			) { emailPattern.matches(it) } && isFormValid
			isFormValid = validateField(
				sksMahasiswa3Entered,
				tilSksMahasiswa3,
				R.string.tv_error_input_blank,
				R.string.sks_tidak_valid,
				1..3
			) && isFormValid
			isFormValid = validateField(
				ipkMahasiswa3Entered,
				tilIpkMahasiswa3,
				R.string.tv_error_input_blank,
				R.string.ipk_tidak_valid
			) { isValidIPK(it) } && isFormValid

			// Check if NIMs of Mahasiswa1, Mahasiswa2, and Mahasiswa3 are not the same
			val nimMahasiswa1 = nimMahasiswa1Entered.trim()
			val nimMahasiswa2 = nimMahasiswa2Entered.trim()
			val nimMahasiswa3 = nimMahasiswa3Entered.trim()

			if (nimMahasiswa1 == nimMahasiswa2 || nimMahasiswa1 == nimMahasiswa3 || nimMahasiswa2 == nimMahasiswa3) {
				isFormValid = false
				tilNimMahasiswa1.error = getString(R.string.tv_error_duplicate_nim)
				tilNimMahasiswa1.isErrorEnabled = true

				tilNimMahasiswa2.error = getString(R.string.tv_error_duplicate_nim)
				tilNimMahasiswa2.isErrorEnabled = true

				tilNimMahasiswa3.error = getString(R.string.tv_error_duplicate_nim)
				tilNimMahasiswa3.isErrorEnabled = true
			} else {
				tilNimMahasiswa1.error = null
				tilNimMahasiswa1.isErrorEnabled = false

				tilNimMahasiswa2.error = null
				tilNimMahasiswa2.isErrorEnabled = false

				tilNimMahasiswa3.error = null
				tilNimMahasiswa3.isErrorEnabled = false
			}

			return isFormValid
		}
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
		val currentFragment = this@MahasiswaKelompokDaftarKelompokFragment

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
		val currentFragment = this@MahasiswaKelompokDaftarKelompokFragment

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
			setShimmerVisibility(shimmerFragmentDaftarKelompok, isLoading)

			cvDetailCapstoneKelompok.visibility = View.GONE
			tvTitleDetailCapstoneKelompok.visibility = View.GONE

			tvDetailMahasiswaKelompok.visibility = View.GONE
			cvDetailMahasiswa1Kelompok.visibility = View.GONE
			cvDetailMahasiswa2Kelompok.visibility = View.GONE
			cvDetailMahasiswa3Kelompok.visibility = View.GONE

			btnSimpanDaftarKelompok.visibility = View.GONE

			cvErrorDaftarKelompok.visibility = View.GONE

			if (!isLoading) {
				if (isSuccess) {
					cvDetailCapstoneKelompok.visibility = View.VISIBLE
					tvTitleDetailCapstoneKelompok.visibility = View.VISIBLE

					tvDetailMahasiswaKelompok.visibility = View.VISIBLE
					cvDetailMahasiswa1Kelompok.visibility = View.VISIBLE
					cvDetailMahasiswa2Kelompok.visibility = View.VISIBLE
					cvDetailMahasiswa3Kelompok.visibility = View.VISIBLE

					btnSimpanDaftarKelompok.visibility = View.VISIBLE

					cvErrorDaftarKelompok.visibility = View.GONE

				} else {
					cvDetailCapstoneKelompok.visibility = View.GONE
					tvTitleDetailCapstoneKelompok.visibility = View.GONE

					tvDetailMahasiswaKelompok.visibility = View.GONE
					cvDetailMahasiswa1Kelompok.visibility = View.GONE
					cvDetailMahasiswa2Kelompok.visibility = View.GONE
					cvDetailMahasiswa3Kelompok.visibility = View.GONE

					btnSimpanDaftarKelompok.visibility = View.GONE

					cvErrorDaftarKelompok.visibility = View.VISIBLE
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