package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.capstone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDokumenCapstoneBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.DokumenViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.KelompokSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaDokumenCapstoneFragment : Fragment() {

	private var _binding: FragmentMahasiswaDokumenCapstoneBinding? = null
	private val binding get() = _binding!!

	private val customSnackbar = CustomSnackbar()

	private val dokumenViewModel: DokumenViewModel by viewModels()
	private val profileViewModel: ProfileSayaViewModel by viewModels()
	private val kelompokViewModel: KelompokSayaViewModel by viewModels()

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

		checkKelompok()

		checkDokumen()
	}

	private fun checkKelompok() {
		setLoading(true)

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
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)
					val message = getKelompokSayaResult.message
					Log.d("Result error", message.toString())

				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload?.message
					Log.d("Result success", message.toString())

					val dataKelompok = getKelompokSayaResult.payload?.data

					Log.d("KELOMPOK", dataKelompok.toString())

					if (dataKelompok?.kelompok != null) {

						if (dataKelompok.kelompok.idKelompok != null){
							binding.cvBelumMemilikiKelompok.visibility = View.GONE

							binding.linearLayoutDokumenCapstone.visibility = View.VISIBLE
						} else{
							binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
							binding.tvBelumMemilikiKelompok.setText(R.string.kelompok_belum_valid)

							binding.linearLayoutDokumenCapstone.visibility = View.GONE
						}

					} else {
						binding.cvBelumMemilikiKelompok.visibility = View.VISIBLE
						binding.tvBelumMemilikiKelompok.setText(R.string.belum_memiliki_kelompok)

						binding.linearLayoutDokumenCapstone.visibility = View.GONE
					}
				}

				else -> {}
			}
		}
	}

	private fun checkDokumen() {
		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						dokumenViewModel.getFileIndex(FileIndexRemoteRequestBody(userId, apiToken))

					}
				}
			}
		}
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaDokumenCapstoneFragment

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
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				} else if (message == "null" || message.equals(null) || message == "Terjadi kesalahan!") {
					restartFragment()
				} else if (message == "Password berhasil diubah, silahkan masuk kembali.") {

					profileViewModel.setApiToken("")
					profileViewModel.setUserId("")
					profileViewModel.setStatusAuth(false)

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				}
			}
		}

	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaDokumenCapstoneFragment

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

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDokumenCapstone, isLoading)

			linearLayoutDokumenCapstone.visibility = if (isLoading) View.GONE else View.VISIBLE

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