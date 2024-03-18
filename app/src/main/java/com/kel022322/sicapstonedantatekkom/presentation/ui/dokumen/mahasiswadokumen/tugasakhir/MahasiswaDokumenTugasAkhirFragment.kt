package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.tugasakhir

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenTugasAkhirBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.DokumenViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.KelompokIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MahasiswaDokumenTugasAkhirFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenTugasAkhirBinding? = null
	private val binding get() = _binding!!

	private val customSnackbar = CustomSnackbar()

	private val dokumenViewModel: DokumenViewModel by viewModels()
	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()

	private var id: String? = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding =
			FragmentMahasiswaDokumenTugasAkhirBinding.inflate(layoutInflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		checkKelompok()

		checkDokumen()

		btnListener()

	}

	private fun checkKelompok() {
		setLoadingKelompok(true)

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.getKelompokSaya(apiToken)
			}
		}

		kelompokViewModel.getKelompokSayaResult.observe(viewLifecycleOwner) { getKelompokSayaResult ->
			val resultResponse = getKelompokSayaResult.payload

			when (getKelompokSayaResult) {
				is Resource.Loading -> {
					setLoadingKelompok(true)
				}

				is Resource.Error -> {
					setLoadingKelompok(false)
					Log.d("Result error", getKelompokSayaResult.toString())

					binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.VISIBLE
					binding.tvBelumMemilikiKelompokTugasAkhir.text =
						getString(R.string.tv_terjadi_kesalahan)

					binding.linearLayoutDokumenTugasAkhir.visibility = View.GONE
				}

				is Resource.Success -> {
					setLoadingKelompok(false)

					Log.d("Result success", resultResponse.toString())

					binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.GONE

					binding.linearLayoutDokumenTugasAkhir.visibility = View.VISIBLE

					val dataKelompok = getKelompokSayaResult.payload?.data
					val status = getKelompokSayaResult.payload?.status

					if (getKelompokSayaResult.payload?.success == true) {

						if (dataKelompok?.kelompok?.nomorKelompok != null) {
							binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.GONE

							binding.linearLayoutDokumenTugasAkhir.visibility = View.VISIBLE
						} else {
							binding.cvBelumMemilikiKelompokTugasAkhir.visibility = View.VISIBLE
							binding.tvBelumMemilikiKelompokTugasAkhir.setText(R.string.kelompok_belum_valid)

							binding.linearLayoutDokumenTugasAkhir.visibility = View.GONE
						}

					} else {

						with(binding){
							Log.d("Update Succes status, but failed", status.toString())

							if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {

								actionIfLogoutSucces()
							} else {
								setViewVisibility(linearLayoutDokumenTugasAkhir, false)
								setViewVisibility(cvBelumMemilikiKelompokTugasAkhir, true)
								tvBelumMemilikiKelompokTugasAkhir.text =
									status ?: "Belum mendaftar capstone"

							}
						}
					}

				}

				else -> {}
			}
		}
	}

	private fun checkDokumen() {

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				dokumenViewModel.getFileIndex(apiToken)
			}
		}


		dokumenViewModel.getFileIndexResult.observe(viewLifecycleOwner) { getFileIndexResult ->

			val resultResponse = getFileIndexResult.payload
			val status = resultResponse?.status
			when (getFileIndexResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
				}

				is Resource.Success -> {
					setLoading(false)

					id = getFileIndexResult.payload?.data?.fileMhs?.id.toString()

					if (resultResponse?.success == true) {
						checkFile(getFileIndexResult)

						viewDokumen(getFileIndexResult)
					} else {
						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							actionIfLogoutSucces()
						}
					}


				}

				else -> {}
			}
		}

	}

	private fun viewDokumen(getFileIndexResult: Resource<FileIndexRemoteResponse>) {

		val resultResponse = getFileIndexResult.payload

		with(binding) {

			// LaporanTa
			btnUnduhLaporan.setOnClickListener {

				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk membuka dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlLaporanTa?.takeIf { it.isNotBlank() }
						?.let {
							val url =
								if (!it.startsWith("http://") && !it.startsWith("https://")) {
									URLUtil.guessUrl(it)
								} else {
									it
								}

							// Buat Intent untuk membuka tautan di browser
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
							// Gunakan context dari fragment untuk memulai aktivitas
							requireActivity().startActivity(intent)
						}
				}

			}

			// Makalah
			btnUnduhMakalahTa.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk membuka dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlMakalah?.takeIf { it.isNotBlank() }
						?.let {
							val url =
								if (!it.startsWith("http://") && !it.startsWith("https://")) {
									URLUtil.guessUrl(it)
								} else {
									it
								}

							// Buat Intent untuk membuka tautan di browser
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
							// Gunakan context dari fragment untuk memulai aktivitas
							requireActivity().startActivity(intent)
						}
				}
			}
		}
	}

	private fun btnListener() {

		binding.tvPilihDokumenLaporanTa.setOnClickListener {

			uploadFileLaporanTaLauncher.launch("application/pdf")

		}

		binding.tvPilihDokumenMakalahTa.setOnClickListener {

			uploadFileMakalahLauncher.launch("application/pdf")

		}
	}

	private val uploadFileLaporanTaLauncher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempLaporanTa.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val laporanTaPart =
						MultipartBody.Part.createFormData("laporan_ta", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {
								dokumenViewModel.uploadLaporanProcess(
									apiToken = apiToken,
									laporan_ta = laporanTaPart
								)
							}
						}


					dokumenViewModel.uploadLaporanProcessResult.observe(viewLifecycleOwner) { uploadLaporanProcessResult ->
						when (uploadLaporanProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadLaporanProcessResult.payload?.status
								Log.d("HASIL UPLOAD ERROR", status.toString())
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadLaporanProcessResult.payload?.status

								if (uploadLaporanProcessResult.payload?.success == true && uploadLaporanProcessResult.payload.data != null) {
									findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaBerandaFragment)
									showSnackbar(status ?: "Berhasil!")

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
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

				} catch (e: Exception) {
					e.printStackTrace()
					setLoading(false)
					showSnackbar("Mohon periksa kembali koneksi internet Anda! ${e.message}")

				}
			}
		}

	private val uploadFileMakalahLauncher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempMakalah.pdf")
				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val makalahPart =
						MultipartBody.Part.createFormData("makalah", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {
								dokumenViewModel.uploadMakalahProcess(
									apiToken = apiToken.toString(),
									makalah = makalahPart
								)
							}
						}


					dokumenViewModel.uploadMakalahProcessResult.observe(viewLifecycleOwner) { uploadMakalahProcessResult ->
						when (uploadMakalahProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadMakalahProcessResult.payload?.status
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadMakalahProcessResult.payload?.status

								Log.d("C200 RESULT", uploadMakalahProcessResult.toString())
								if (uploadMakalahProcessResult.payload?.success == true && uploadMakalahProcessResult.payload.data != null) {
									findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaBerandaFragment)

									showSnackbar(status ?: "Berhasil!")

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
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

				} catch (e: Exception) {
					e.printStackTrace()
					showSnackbar("Mohon periksa kembali koneksi internet Anda! ${e.message}")

					setLoading(false)
				}
			}
		}

	@SuppressLint("SetTextI18n")
	private fun checkFile(getFileIndexResult: Resource<FileIndexRemoteResponse>) {
		val fileMhs = getFileIndexResult.payload?.data?.fileMhs

		setViewAppearance(
			binding.btnUnduhLaporan,
			binding.tvNamaDokumenLaporanTa,
			fileMhs?.fileNameLaporanTa,
			binding.garisVerticalLaporan,
			false
		)

		setViewAppearance(
			binding.btnUnduhMakalahTa,
			binding.tvNamaDokumenMakalahTa,
			fileMhs?.fileNameMakalah,
			binding.garisVerticalMakalah,
			false
		)

	}

	private fun setViewAppearance(
		button: MaterialButton,
		textView: TextView,
		fileName: String?,
		garisVertical: View,
		disabled: Boolean,
	) {
		if (fileName == null) {
			button.isEnabled = false

			// Set button appearance when disabled
			button.setBackgroundColor(
				ContextCompat.getColor(
					requireContext(), R.color.tabUnselected
				)
			)
			button.strokeWidth = 2
			button.strokeColor = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.tabUnselected)
			)
			button.setTextColor(ContextCompat.getColor(requireContext(), R.color.White))
		} else {
			button.setBackgroundColor(
				ContextCompat.getColor(
					requireContext(), R.color.RoyalBlue
				)
			)
			button.strokeWidth = 2
			button.strokeColor = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.RoyalBlue)
			)

			textView.text = fileName
			button.isEnabled = !disabled
			garisVertical.backgroundTintList = ColorStateList.valueOf(
				ContextCompat.getColor(requireContext(), R.color.RoyalBlue)
			)
		}
	}

	private fun showSnackbar(status: String) {
		val currentFragment = this@MahasiswaDokumenTugasAkhirFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), status, "OK"
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
			setShimmerVisibility(shimmerDokumenTugasAkhir, isLoading)
			linearLayoutDokumenTugasAkhir.visibility =
				if (isLoading) View.GONE else View.VISIBLE
			cvBelumMemilikiKelompokTugasAkhir.visibility =
				if (isLoading) View.GONE else View.VISIBLE
		}
	}

	private fun setLoadingKelompok(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDokumenTugasAkhir, isLoading)
			linearLayoutDokumenTugasAkhir.visibility =
				if (isLoading) View.GONE else View.VISIBLE
			cvBelumMemilikiKelompokTugasAkhir.visibility =
				if (isLoading) View.GONE else View.VISIBLE

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
		status: String,
		positiveAction: () -> Unit,
	) {
		val builder = AlertDialog.Builder(requireContext()).create()
		val view = layoutInflater.inflate(R.layout.dialog_custom_alert_dialog, null)
		builder.setView(view)
		builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

		val buttonYes = view.findViewById<Button>(R.id.btn_alert_yes)
		val buttonNo = view.findViewById<Button>(R.id.btn_alert_no)
		val alertTitle = view.findViewById<TextView>(R.id.tv_alert_title)
		val alertstatus = view.findViewById<TextView>(R.id.tv_alert_message)

		alertTitle.text = title
		alertstatus.text = status

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
	private fun setViewVisibility(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.GONE
	}
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null

	}

}