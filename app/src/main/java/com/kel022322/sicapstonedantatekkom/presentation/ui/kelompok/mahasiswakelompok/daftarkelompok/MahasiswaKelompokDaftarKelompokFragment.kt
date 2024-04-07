package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.daftarkelompok

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
import com.google.android.material.textfield.TextInputLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.local.model.jeniskelamin.JenisKelaminModel
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing1.DosenPembimbing1RemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing2.DosenPembimbing2RemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response.TopikRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokDaftarKelompokBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminDuaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.JenisKelaminTigaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.ListDosenAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.ListDosenDuaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.ListMahasiswaDuaAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran.TopikAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel.DosenViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel.MahasiswaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel.SiklusViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel.TopikViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel.ProfileIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MahasiswaKelompokDaftarKelompokFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokDaftarKelompokBinding? = null
	private val binding get() = _binding!!

	private val profileIndexViewModel: ProfileIndexViewModel by viewModels()
	private val daftarKelompokViewModel: DaftarKelompokViewModel by viewModels()
	private val userViewModel: UserViewModel by viewModels()
	private val dosenViewModel: DosenViewModel by viewModels()
	private val mahasiswaViewModel: MahasiswaViewModel by viewModels()
	private val topikViewModel: TopikViewModel by viewModels()
	private val siklusViewModel: SiklusViewModel by viewModels()

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

		setDropdownJenisKelamin()

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
					showCustomAlertDialog(
						"Konfirmasi", "Apakah anda yakin data yang anda masukan sudah sesuai?"
					) {

						setLoading(isLoading = true, isSuccess = true)

						userViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
							if (userId != null) {
								userViewModel.getApiToken()
									.observe(viewLifecycleOwner) { apiToken ->
										apiToken?.let {
											daftarKelompokViewModel.addKelompokPunyaKelompok(
												apiToken,
												AddKelompokPunyaKelompokRemoteRequestBody(
													idSiklus = selectedIdSiklus,
													judulCapstone = judulCapstoneEntered,
													idTopik = selectedIdTopik,
													dosbingSatu = selectedIdDosbing1,
													dosbingDua = selectedIdDosbing2,

													angkatanSatu = angkatanMahasiswa1Entered,
													emailSatu = emailMahasiswa1Entered,
													jenisKelaminSatu = jenisKelaminMahasiswa1Entered,
													ipkSatu = ipkMahasiswa1Entered,
													sksSatu = sksMahasiswa1Entered,
													noTelpSatu = noTelpMahasiswa1Entered,

													userIdDua = selectedIdMahasiswa2,
													angkatanDua = angkatanMahasiswa2Entered,
													emailDua = emailMahasiswa2Entered,
													jenisKelaminDua = jenisKelaminMahasiswa2Entered,
													ipkDua = ipkMahasiswa2Entered,
													sksDua = sksMahasiswa2Entered,
													noTelpDua = noTelpMahasiswa2Entered,

													userIdTiga = selectedIdMahasiswa3,
													angkatanTiga = angkatanMahasiswa3Entered,
													emailTiga = emailMahasiswa3Entered,
													jenisKelaminTiga = jenisKelaminMahasiswa3Entered,
													ipkTiga = ipkMahasiswa3Entered,
													sksTiga = sksMahasiswa3Entered,
													noTelpTiga = noTelpMahasiswa3Entered,
												)
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
	}

	private fun valueObserver() {
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				profileIndexViewModel.getMahasiswaProfile(apiToken)

				siklusViewModel.getSiklus(apiToken)

			}
		}

		siklusViewModel.getSiklusResult.observe(viewLifecycleOwner) { getSiklusResult ->
			val resultResponse = getSiklusResult.payload
			val status = resultResponse?.status

			when (getSiklusResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getSiklusResult.message.toString())

				}

				is Resource.Success -> {

					val message = getSiklusResult.payload?.status
					Log.d("Result success", message.toString())

					if (resultResponse?.success == true) {
						setLoading(isLoading = false, isSuccess = true)

						binding.edtSiklusKelompok.setText(resultResponse.data?.rs_siklus?.namaSiklus)
						dataObserver()

					} else {

						setLoading(isLoading = false, isSuccess = false)

						with(binding) {

							if (status == "Token is Expired" || status == "Token is Invalid") {
								actionIfLogoutSucces()
							} else {
								setViewVisibility(linearLayoutDataPendaftaranKelompok, false)
								setViewVisibility(cvErrorDaftarKelompok, true)
								tvErrorDaftarKelompok.text =
									status ?: "Mohon periksa kembali koneksi internet Anda!"

							}
						}
					}

				}

				else -> {}
			}
		}

		daftarKelompokViewModel.addKelompokPunyaKelompok.observe(viewLifecycleOwner) { addKelompokPunyaKelompok ->

			val resultResponse = addKelompokPunyaKelompok.payload
			val status = resultResponse?.status

			when (addKelompokPunyaKelompok) {
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

	private fun dataObserver() {
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				profileIndexViewModel.getMahasiswaProfile(apiToken)

				topikViewModel.getTopik(apiToken)

				dosenViewModel.getDosenPembimbing1(apiToken)
				dosenViewModel.getDosenPembimbing2(apiToken)
				mahasiswaViewModel.getDataMahasiswa(apiToken)
			}
		}

		mahasiswaViewModel.getMahasiswaResult.observe(viewLifecycleOwner) { getMahasiswaResult ->
			val status = getMahasiswaResult.payload?.status

			when (getMahasiswaResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getMahasiswaResult.message.toString())

				}

				is Resource.Success -> {

					val message = getMahasiswaResult.payload?.status
					Log.d("Result success", message.toString())

					if (getMahasiswaResult.payload?.success == true && getMahasiswaResult.payload.data != null) {
						setLoading(isLoading = false, isSuccess = true)
						setDropdownMahasiswa(getMahasiswaResult)

						if (selectedIdMahasiswa2 == "") {

							binding.edtNamaLengkapMahasiswa2.text = null
							binding.edtNimMahasiswa2.text = null
							binding.edtAngkatanMahasiswa2.text = null
							binding.edtJenisKelaminMahasiswa2.text = null
							binding.edtNoTelpMahasiswa2.text = null
							binding.edtEmailMahasiswa2.text = null

						}

						if (selectedIdMahasiswa3 == "") {

							binding.edtNamaLengkapMahasiswa3.text = null
							binding.edtNimMahasiswa3.text = null
							binding.edtAngkatanMahasiswa3.text = null
							binding.edtJenisKelaminMahasiswa3.text = null
							binding.edtNoTelpMahasiswa3.text = null
							binding.edtEmailMahasiswa3.text = null
						}

					} else {
						setLoading(isLoading = false, isSuccess = false)

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
		dosenViewModel.getDosen1Result.observe(viewLifecycleOwner) { getDosenResult ->

			val status = getDosenResult.payload?.status
			when (getDosenResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getDosenResult.message.toString())

				}

				is Resource.Success -> {

					val message = getDosenResult.payload?.status
					Log.d("Result", message.toString())

					if (getDosenResult.payload?.success == true) {
						setLoading(isLoading = false, isSuccess = true)

						if (getDosenResult.payload.data != null) {
							setDropdownDosen1(getDosenResult)

							if (selectedIdDosbing1 == "") {

								binding.edtDosbing1Kelompok.text = null
							}

						}

					} else {
						setLoading(isLoading = false, isSuccess = false)

						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						}
					}

				}

				else -> {}
			}
		}

		dosenViewModel.getDosen2Result.observe(viewLifecycleOwner) { getDosenResult ->

			val status = getDosenResult.payload?.status
			when (getDosenResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getDosenResult.message.toString())

				}

				is Resource.Success -> {

					val message = getDosenResult.payload?.status
					Log.d("Result", message.toString())

					if (getDosenResult.payload?.success == true) {
						setLoading(isLoading = false, isSuccess = true)

						if (getDosenResult.payload.data != null) {
							setDropdownDosen2(getDosenResult)

							if (selectedIdDosbing2 == "") {

								binding.edtDosbing2Kelompok.text = null
							}

						}

					} else {
						setLoading(isLoading = false, isSuccess = false)

						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						}
					}

				}

				else -> {}
			}
		}

		topikViewModel.getTopikResult.observe(viewLifecycleOwner) { getTopikResult ->
			val status = getTopikResult.payload?.status

			when (getTopikResult) {
				is Resource.Loading -> {
					setLoading(isLoading = true, isSuccess = true)

				}

				is Resource.Error -> {
					setLoading(isLoading = false, isSuccess = false)
					Log.d("Result error", getTopikResult.message.toString())

				}

				is Resource.Success -> {

					val message = getTopikResult.payload?.status
					Log.d("Result success", message.toString())

					if (getTopikResult.payload?.success == true) {
						setLoading(isLoading = false, isSuccess = true)

						if (getTopikResult.payload.data != null) {
							setTopikDropdown(getTopikResult)

							if (selectedIdTopik == "") {
								binding.edtTopikCapstoneKelompok.text = null
							}
						}

					} else {
						setLoading(isLoading = false, isSuccess = false)

						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						}
					}

				}

				else -> {}
			}
		}

	}

	private fun setInitialValue(getProfileResult: Resource<ProfileRemoteResponse>) {

		// mahasiswa
		with(binding) {
			val dataAkun = getProfileResult.payload?.data
			if (dataAkun != null) {
				edtNamaLengkapMahasiswa1.setText(dataAkun.userName)
				edtNimMahasiswa1.setText(dataAkun.nomorInduk)
				edtEmailMahasiswa1.setText(dataAkun.userEmail)
				edtNoTelpMahasiswa1.setText(dataAkun.noTelp)
				edtAngkatanMahasiswa1.setText(dataAkun.angkatan)
				edtJenisKelaminMahasiswa1.setText(dataAkun.jenisKelamin)

			}
		}

	}

	private fun setDropdownJenisKelamin() {

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

	private fun setDropdownDosen1(getDosenResult: Resource<DosenPembimbing1RemoteResponse>) {

		// dosen
		val dosbing1Adapter = getDosenResult.payload?.data?.rs_dosen?.let {
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
		dosbing1Adapter?.setNotifyOnChange(true)

	}

	private fun setDropdownDosen2(getDosenResult: Resource<DosenPembimbing2RemoteResponse>) {
		// dosen
		val dosbing2Adapter =
			getDosenResult.payload?.data?.rs_dosen?.let {
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

	private fun setDropdownMahasiswa(getMahasiswaResult: Resource<MahasiswaIndexRemoteResponse>) {

		with(binding) {


			// mahasiswa 2
			val mahasiswa2Adapter =
				getMahasiswaResult.payload?.data?.rs_mahasiswa?.let {
					ListMahasiswaDuaAdapter(
						requireContext(),
						it
					)
				}
			edtNamaLengkapMahasiswa2.setAdapter(mahasiswa2Adapter)

			edtNamaLengkapMahasiswa2.setOnItemClickListener { _, _, position, _ ->
				val selectedMahasiswa2 = mahasiswa2Adapter?.getItem(position)
				selectedIdMahasiswa2 = selectedMahasiswa2?.userId.toString()

				edtNamaLengkapMahasiswa2.setText(selectedMahasiswa2?.userName)
				edtNimMahasiswa2.setText(selectedMahasiswa2?.nomorInduk)
				edtAngkatanMahasiswa2.setText(selectedMahasiswa2?.angkatan)
				edtJenisKelaminMahasiswa2.setText(selectedMahasiswa2?.jenisKelamin)
				edtNoTelpMahasiswa2.setText(selectedMahasiswa2?.noTelp)
				edtEmailMahasiswa2.setText(selectedMahasiswa2?.userEmail)

			}

			// mahasiswa3
			val mahasiswa3Adapter =
				getMahasiswaResult.payload?.data?.rs_mahasiswa?.let {
					ListMahasiswaDuaAdapter(
						requireContext(),
						it
					)
				}
			edtNamaLengkapMahasiswa3.setAdapter(mahasiswa3Adapter)

			edtNamaLengkapMahasiswa3.setOnItemClickListener { _, _, position, _ ->
				val selectedMahasiswa3 = mahasiswa3Adapter?.getItem(position)
				selectedIdMahasiswa3 = selectedMahasiswa3?.userId.toString()

				edtNamaLengkapMahasiswa3.setText(selectedMahasiswa3?.userName)
				edtNimMahasiswa3.setText(selectedMahasiswa3?.nomorInduk)
				edtAngkatanMahasiswa3.setText(selectedMahasiswa3?.angkatan)
				edtJenisKelaminMahasiswa3.setText(selectedMahasiswa3?.jenisKelamin)
				edtNoTelpMahasiswa3.setText(selectedMahasiswa3?.noTelp)
				edtEmailMahasiswa3.setText(selectedMahasiswa3?.userEmail)
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

		binding.edtTopikCapstoneKelompok.setAdapter(topikAdapter)

		binding.edtTopikCapstoneKelompok.setOnItemClickListener { _, _, position, _ ->
			val selectedTopik = topikAdapter?.getItem(position)
			selectedIdTopik = selectedTopik?.id.toString()

			binding.edtTopikCapstoneKelompok.setText(selectedTopik?.nama)

			// Clear focus and show dropdown
			binding.edtTopikCapstoneKelompok.showDropDown()
			Log.d("EDT", "EDT CLICKED!")
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
		val ipkRegex = """^(?:0|[1-3](?:\.\d{2})|4(?:\.00?)?)$""".toRegex()
		return ipk.matches(ipkRegex)
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaKelompokDaftarKelompokFragment

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

	private fun setLoading(isLoading: Boolean, isSuccess: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerFragmentDaftarKelompok, isLoading)

			setViewVisibility(linearLayoutDataPendaftaranKelompok, false)

			cvErrorDaftarKelompok.visibility = View.GONE

			if (!isLoading) {
				if (isSuccess) {
					setViewVisibility(linearLayoutDataPendaftaranKelompok, true)

					cvErrorDaftarKelompok.visibility = View.GONE

				} else {
					setViewVisibility(linearLayoutDataPendaftaranKelompok, false)

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

	private fun setViewVisibility(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.GONE
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