package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.detailkelompok

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokDetailBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.KelompokSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunAnggotaKelompokAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDosbingAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaKelompokDetailFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokDetailBinding? = null
	private val binding get() = _binding!!

	private val profileViewModel: ProfileSayaViewModel by viewModels()
	private val kelompokViewModel: KelompokSayaViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaKelompokDetailBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		checkKelompok()

		buttonActionListener()
	}

	private fun buttonActionListener(){

		with(binding){

			ivCircleBackArrow.setOnClickListener {
				findNavController().popBackStack()
			}

			icBackArrow.setOnClickListener {
				findNavController().popBackStack()
			}

		}
	}

	private fun setCardKelompok(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>){

		val data = getKelompokSayaResult.payload?.data

		if (data?.kelompok?.idKelompok != null){

			//  kelompok sudah valid
			with(binding){

				val dataKelompok =  data.kelompok
				// card kelompok
				tvValueStatusKelompokDetail.text = dataKelompok.statusKelompok
				tvValueNomorKelompokDetail.text = dataKelompok.nomorKelompok.toString()
				tvValueTopikDetail.text = dataKelompok.namaTopik
				tvValueJudulDetail.text = dataKelompok.judulCapstone

				// card anggota kelompok
				val dataAnggotaKelompok = data.rsMahasiswa
				// card dosbing
				val akunAnggotaKelompokAdapter = AkunAnggotaKelompokAdapter()

				akunAnggotaKelompokAdapter.setList(dataAnggotaKelompok)

				binding.rvAkunAnggotaKelompokKelompokDetail.layoutManager = LinearLayoutManager(
					requireContext(),
					LinearLayoutManager.VERTICAL,
					false
				)
				rvAkunAnggotaKelompokKelompokDetail.adapter = akunAnggotaKelompokAdapter

				// navigate to detail if necessary
				akunAnggotaKelompokAdapter.setOnItemClickCallback(object : AkunAnggotaKelompokAdapter.OnItemClickCallBack {
					override fun onItemClicked(mahasiswaId: String) {
					}
				})

				val dataDosbing = data.rsDosbing
				// card dosbing
				val akunDosbingAdapter = AkunDosbingAdapter()

				akunDosbingAdapter.setList(dataDosbing)

				binding.rvAkunDosbingKelompokDetail.layoutManager = LinearLayoutManager(
					requireContext(),
					LinearLayoutManager.VERTICAL,
					false
				)
				rvAkunDosbingKelompokDetail.adapter = akunDosbingAdapter

				// navigate to detail if necessary
				akunDosbingAdapter.setOnItemClickCallback(object : AkunDosbingAdapter.OnItemClickCallBack {
					override fun onItemClicked(dosbingId: String) {
					}
				})
			}

		} else {
			//  kelompok belum valid
			with(binding){
				// card kelompok
				"Belum valid!".also { tvValueStatusKelompokDetail.text = it }

				tvErrorKelompokDosbingEmptyDetail.visibility = View.VISIBLE
			}

		}

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
					Log.d("Result error", getKelompokSayaResult.toString())

					binding.cvValueKelompokDetail.visibility = View.GONE
					binding.cvValueDosbingDetail.visibility = View.GONE
					binding.cvValueAnggotaKelompokDetail.visibility = View.GONE
					binding.cvErrorKelompokDetail.visibility = View.VISIBLE

					binding.shimmerFragmentKelompokDetail.visibility = View.GONE
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())

					if (getKelompokSayaResult.payload?.status == true){

						setCardKelompok(getKelompokSayaResult)

						val dataKelompok = getKelompokSayaResult.payload.data

						if (dataKelompok?.kelompok == null) {
							binding.cvValueKelompokDetail.visibility = View.GONE
							binding.cvValueDosbingDetail.visibility = View.GONE
							binding.cvValueAnggotaKelompokDetail.visibility = View.GONE
							binding.cvErrorKelompokDetail.visibility = View.GONE

							binding.shimmerFragmentKelompokDetail.visibility  = View.GONE

						} else {
							binding.cvValueKelompokDetail.visibility = View.VISIBLE
							binding.cvValueDosbingDetail.visibility = View.VISIBLE
							binding.cvValueAnggotaKelompokDetail.visibility = View.VISIBLE
							binding.cvErrorKelompokDetail.visibility = View.GONE

							binding.shimmerFragmentKelompokDetail.visibility = View.GONE

						}
					}

				}

				else -> {}
			}
		}
	}
	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaKelompokDetailFragment

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
				} else if (message == "null" || message.equals(null) || message == "Terjadi kesalahan!") {
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
		val currentFragment = this@MahasiswaKelompokDetailFragment

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

			setShimmerVisibility(shimmerFragmentKelompokDetail, isLoading)

			if (isLoading){
				cvValueKelompokDetail.visibility = View.GONE
				cvValueAnggotaKelompokDetail.visibility = View.GONE
				cvValueDosbingDetail.visibility = View.GONE
				cvErrorKelompokDetail.visibility = View.GONE

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