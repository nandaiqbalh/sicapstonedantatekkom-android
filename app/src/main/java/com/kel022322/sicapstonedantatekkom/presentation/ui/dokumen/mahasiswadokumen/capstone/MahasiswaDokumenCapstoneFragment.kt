package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.capstone

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
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenCapstoneBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.DokumenViewModel
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
class MahasiswaDokumenCapstoneFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenCapstoneBinding? = null
	private val binding get() = _binding!!

	private val customSnackbar = CustomSnackbar()

	private val dokumenViewModel: DokumenViewModel by viewModels()
	private val userViewModel: UserViewModel by viewModels()

	private var id: String? = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentMahasiswaDokumenCapstoneBinding.inflate(layoutInflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		checkDokumen()

		btnListener()

	}

	private fun checkDokumen() {
		setLoading(true)

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

					binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
					binding.tvBelumMemilikiKelompok.text = resultResponse?.status ?: "Mohon periksa kembali koneksi internet Anda!"

					binding.linearLayoutDokumenCapstone.visibility = View.GONE
				}

				is Resource.Success -> {
					setLoading(false)

					id = getFileIndexResult.payload?.data?.fileMhs?.id.toString()

					if (resultResponse?.success == true) {

						binding.cvBelumMemilikiKelompok.visibility = View.GONE

						binding.linearLayoutDokumenCapstone.visibility = View.VISIBLE

						checkFile(getFileIndexResult)

						viewDokumen(getFileIndexResult)
					} else {

						binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
						binding.tvBelumMemilikiKelompok.text = resultResponse?.status ?: "Mohon periksa kembali koneksi internet Anda!"

						binding.linearLayoutDokumenCapstone.visibility = View.GONE

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

			// c100
			btnUnduhC100.setOnClickListener {

				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk membuka dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC100?.takeIf { it.isNotBlank() }
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

			// c200
			btnUnduhC200.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk membuka dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC200?.takeIf { it.isNotBlank() }
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

			// c300
			btnUnduhC300.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk membuka dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC300?.takeIf { it.isNotBlank() }
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

			// c400
			btnUnduhC400.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk membuka dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC400?.takeIf { it.isNotBlank() }
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

			// c500
			btnUnduhC500.setOnClickListener {
				showCustomAlertDialog("Konfirmasi", "Apakah anda yakin untuk membuka dokumen?") {
					resultResponse?.data?.fileMhs?.fileUrlC500?.takeIf { it.isNotBlank() }
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

		binding.tvPilihDokumenC100.setOnClickListener {

			uploadFileC100Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC200.setOnClickListener {

			uploadFileC200Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC300.setOnClickListener {

			uploadFileC300Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC400.setOnClickListener {

			uploadFileC400Launcher.launch("application/pdf")

		}

		binding.tvPilihDokumenC500.setOnClickListener {

			uploadFileC500Launcher.launch("application/pdf")

		}

	}

	private val uploadFileC100Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc100.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c100Part = MultipartBody.Part.createFormData("c100", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {
								dokumenViewModel.uploadC100Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c100 = c100Part
								)
							}
						}


					dokumenViewModel.uploadC100ProcessResult.observe(viewLifecycleOwner) { uploadC100ProcessResult ->
						when (uploadC100ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC100ProcessResult.payload?.status
								Log.d("HASIL UPLOAD ERROR", status.toString())
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC100ProcessResult.payload?.status

								if (uploadC100ProcessResult.payload?.success == true && uploadC100ProcessResult.payload.data != null) {

									showSnackbar(status ?: "Berhasil!")
									findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaBerandaFragment)

								} else {
									Log.d("Update Succes status, but failed", status.toString())

									checkDokumen()
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

	private val uploadFileC200Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc200.pdf")
				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c200Part = MultipartBody.Part.createFormData("c200", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {
								dokumenViewModel.uploadC200Process(
									apiToken = apiToken.toString(),
									idKelompok = id.toString(),
									c200 = c200Part
								)
							}
						}


					dokumenViewModel.uploadC200ProcessResult.observe(viewLifecycleOwner) { uploadC200ProcessResult ->
						when (uploadC200ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC200ProcessResult.payload?.status
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC200ProcessResult.payload?.status

								Log.d("C200 RESULT", uploadC200ProcessResult.toString())
								if (uploadC200ProcessResult.payload?.success == true && uploadC200ProcessResult.payload.data != null) {
									showSnackbar(status ?: "Berhasil!")

									findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaBerandaFragment)
								} else {
									Log.d("Update Succes status, but failed", status.toString())
									checkDokumen()

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

	private val uploadFileC300Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc300.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c300Part = MultipartBody.Part.createFormData("c300", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {

								dokumenViewModel.uploadC300Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c300 = c300Part
								)
							}
						}


					dokumenViewModel.uploadC300ProcessResult.observe(viewLifecycleOwner) { uploadC300ProcessResult ->
						when (uploadC300ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC300ProcessResult.payload?.status
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC300ProcessResult.payload?.status

								if (uploadC300ProcessResult.payload?.success == true && uploadC300ProcessResult.payload.data != null) {
									findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaBerandaFragment)

									showSnackbar(status ?: "Berhasil!")

								} else {
									checkDokumen()

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

	private val uploadFileC400Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc400.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c400Part = MultipartBody.Part.createFormData("c400", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {

								dokumenViewModel.uploadC400Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c400 = c400Part
								)
							}
						}
					dokumenViewModel.uploadC400ProcessResult.observe(viewLifecycleOwner) { uploadC400ProcessResult ->
						when (uploadC400ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC400ProcessResult.payload?.status
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")
							}

							is Resource.Success -> {
								setLoading(false)

								val status = uploadC400ProcessResult.payload?.status

								if (uploadC400ProcessResult.payload?.success == true && uploadC400ProcessResult.payload.data != null) {

									showSnackbar(status ?: "Berhasil!")
									findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaBerandaFragment)

								} else {
									checkDokumen()

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

	private val uploadFileC500Launcher =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			uri?.let {
				val contentResolver = context?.contentResolver
				val inputStream = contentResolver?.openInputStream(it)
				val cacheDir: File = requireContext().cacheDir

				val file = File(cacheDir, "tempc500.pdf")

				try {
					inputStream?.use { input ->
						FileOutputStream(file).use { output ->
							input.copyTo(output)
						}
					}

					val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
					val c500Part = MultipartBody.Part.createFormData("c500", file.name, requestBody)


					userViewModel.getApiToken()
						.observe(viewLifecycleOwner) { apiToken ->
							apiToken?.let {

								dokumenViewModel.uploadC500Process(
									apiToken = apiToken,
									idKelompok = id.toString(),
									c500 = c500Part
								)
							}
						}

					dokumenViewModel.uploadC500ProcessResult.observe(viewLifecycleOwner) { uploadC500ProcessResult ->
						when (uploadC500ProcessResult) {
							is Resource.Loading -> {
								setLoading(true)
							}

							is Resource.Error -> {
								setLoading(false)

								val status = uploadC500ProcessResult.payload?.status
								showSnackbar(status ?: "Mohon periksa kembali koneksi internet Anda!")
							}

							is Resource.Success -> {
								setLoading(false)
								val status = uploadC500ProcessResult.payload?.status

								if (uploadC500ProcessResult.payload?.success == true && uploadC500ProcessResult.payload.data != null) {

									showSnackbar(status ?: "Berhasil!")
									findNavController().navigate(R.id.action_mahasiswaDokumenFragment_to_mahasiswaBerandaFragment)

								} else {
									checkDokumen()

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
			binding.btnUnduhC100,
			binding.tvNamaDokumenC100,
			fileMhs?.fileNameC100,
			binding.garisVertical,
			false
		)

		setViewAppearance(
			binding.btnUnduhC200,
			binding.tvNamaDokumenC200,
			fileMhs?.fileNameC200,
			binding.garisVerticalC200,
			false
		)

		setViewAppearance(
			binding.btnUnduhC300,
			binding.tvNamaDokumenC300,
			fileMhs?.fileNameC300,
			binding.garisVerticalC300,
			false
		)

		setViewAppearance(
			binding.btnUnduhC400,
			binding.tvNamaDokumenC400,
			fileMhs?.fileNameC400,
			binding.garisVerticalC400,
			false
		)

		setViewAppearance(
			binding.btnUnduhC500,
			binding.tvNamaDokumenC500,
			fileMhs?.fileNameC500,
			binding.garisVerticalC500,
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
		val currentFragment = this@MahasiswaDokumenCapstoneFragment

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
			setShimmerVisibility(shimmerDokumenCapstone, isLoading)

			setViewVisibility(linearLayoutDokumenCapstone, false)
			setViewVisibility(cvBelumMemilikiKelompok, false)
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