package com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.request.PhotoProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaProfilBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.EditTextHelper.Companion.setTextOrHint
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaProfilFragment : Fragment() {

	private var _binding: FragmentMahasiswaProfilBinding? = null
	private val binding get() = _binding!!

	private val profileViewModel: ProfileSayaViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaProfilBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// get profile
		getProfile()
	}

	private fun getProfile() {
		setLoading(true)

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						profileViewModel.getMahasiswaProfile(
							ProfileRemoteRequestBody(
								userId.toString(), it
							)
						)
						profileViewModel.getPhotoProfile(
							PhotoProfileRemoteRequestBody(
								userId.toString(), it
							)
						)
					}
				}
			}
		}

		profileViewModel.getProfileResult.observe(viewLifecycleOwner) { getProfileResult ->

			when (getProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = getProfileResult.payload?.message
					showErrorSnackbar(message ?: "Terjadi kesalahan!")
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getProfileResult.payload?.message
					Log.d("Result message", message.toString())

					if (getProfileResult.payload?.data != null) {
						val dataUser = getProfileResult.payload.data
						// set binding
						with(binding) {

							tvNamaUser.text = dataUser.userName
							tvNimUser.text = dataUser.nomorInduk

							// form
							edtNamaLengkapPengguna.setTextOrHint(
								dataUser.userName, R.string.tv_hint_nama_lengkap
							)
							edtNimPengguna.setTextOrHint(dataUser.nomorInduk, R.string.tv_hint_nim)
							edtEmailPengguna.setTextOrHint(
								dataUser.userEmail, R.string.tv_hint_email
							)
							edtNoTelpPengguna.setTextOrHint(
								dataUser.noTelp, R.string.tv_hint_no_telp
							)

						}
					} else {
						showErrorSnackbar(message ?: "Terjadi kesalahan!")
					}
				}

				else -> {}
			}
		}

		profileViewModel.getPhotoProfileResult.observe(viewLifecycleOwner) { getPhotoProfileResult ->
			when (getPhotoProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = getPhotoProfileResult.payload?.message

					showErrorSnackbar(message ?: "Terjadi kesalahan!")
				}

				is Resource.Success -> {
					setLoading(false)
					val message = getPhotoProfileResult.payload?.message

					Log.d("Success message", message.toString())

					if (getPhotoProfileResult.payload?.data != null) {
						// set binding
						with(binding) {
							val base64Image = getPhotoProfileResult.payload.data.toString()

							Log.d("BASE64yuk", base64Image)

							if (base64Image != "null") {
								// Decode base64 string to byte array
								val decodedBytes = decodeBase64ToBitmap(base64Image)

								GlideApp.with(requireContext()).asBitmap().load(decodedBytes)
									.into(ivProfilephoto)
							}

						}
					} else {

						showErrorSnackbar(
							getPhotoProfileResult.payload?.message ?: "Terjadi kesalahan!"
						)
					}
				}

				else -> {}
			}
		}
	}

	private fun decodeBase64ToBitmap(base64: String): Bitmap {
		val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
	}

	private fun showErrorSnackbar(message: String) {

		customSnackbar.showSnackbarWithAction(
			requireActivity().findViewById(android.R.id.content),
			message,
			Snackbar.LENGTH_LONG,
			"OK"
		) {
			customSnackbar.dismissSnackbar()
			if (message == "Token tidak valid!" || message == "Pengguna tidak ditemukan!" || message == "Tidak ada api token!" || message == "Missing api_token in the request body.") {

				profileViewModel.setApiToken("")
				profileViewModel.setUserId("")
				profileViewModel.setStatusAuth(false)

				val intent = Intent(requireContext(), SplashscreenActivity::class.java)
				requireContext().startActivity(intent)
				requireActivity().finishAffinity()
			} else if (message == "null" || message.equals(null)) {
				restartFragment()
			}
		}
	}


	private fun restartFragment() {
		// Detach fragment
		val ftDetach = parentFragmentManager.beginTransaction()
		ftDetach.detach(this@MahasiswaProfilFragment)
		ftDetach.commit()

		// Attach fragment
		val ftAttach = parentFragmentManager.beginTransaction()
		ftAttach.attach(this@MahasiswaProfilFragment)
		ftAttach.commit()
	}

	// set loading and shimmer
	private fun setLoading(isLoading: Boolean) {
		if (isLoading) {
			binding.shimmerProfilMahasiswa.visibility = View.VISIBLE
			binding.shimmerProfilMahasiswa.startShimmer()

			binding.constraintProfilMahasiswa.visibility = View.GONE
		} else {
			binding.shimmerProfilMahasiswa.visibility = View.GONE
			binding.shimmerProfilMahasiswa.stopShimmer()

			binding.constraintProfilMahasiswa.visibility = View.VISIBLE
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}