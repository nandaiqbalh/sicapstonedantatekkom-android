package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.pengumuman

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
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaPengumumanBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.pengumuman.adapter.PengumumanAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaPengumumanFragment : Fragment() {

	private var _binding: FragmentMahasiswaPengumumanBinding? = null
	private val binding get() = _binding!!

	private val pengumumanViewModel: PengumumanViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaPengumumanBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// call the function
		setButtonListener()
		setPengumumanRecyclerView()
	}

	// function to trigger the action when the user doing an action
	private fun setButtonListener() {

		// back to home
		binding.ivCircleBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		// back to home
		binding.icBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	private fun setPengumumanRecyclerView() {

		// set initial state to loading
		setLoading(true)

		// do networking to get broadcast data
		pengumumanViewModel.getBroadcast()

		// observe the result of our networking
		pengumumanViewModel.broadcastResult.observe(viewLifecycleOwner) { broadcastResult ->

			when (broadcastResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					// Log and show the message
					val message = broadcastResult.payload?.status
					showSnackbar(message ?: "Terjadi kesalahan")

					with(binding){
						setViewVisibility(cvErrorPengumuman, true)
						tvErrorPengumuman.text = message ?: "Terjadi kesalahan"

						setViewVisibility(cvPengumuman, false)

						showSnackbar(message ?: "Terjadi kesalahan!")
					}

					Log.d("Result error", broadcastResult.payload?.status.toString())

				}

				is Resource.Success -> {
					setLoading(false)

					val message = broadcastResult.payload?.status
					Log.d("Result success", message.toString())

					if (broadcastResult.payload?.success == false) {
						setLoading(false)
						showSnackbar(message ?: "Terjadi kesalahan!")

						with(binding){
							setViewVisibility(cvErrorPengumuman, true)
							tvErrorPengumuman.text = message ?: "Terjadi kesalahan"

							setViewVisibility(cvPengumuman, false)

							showSnackbar(message ?: "Terjadi kesalahan!")
						}

					} else if (broadcastResult.payload?.success == true && broadcastResult.payload.data?.rs_broadcast?.data != null) {
						val pengumumanAdapter = PengumumanAdapter()

						pengumumanAdapter.setList(broadcastResult.payload.data.rs_broadcast.data)

						binding.rvPengumuman.layoutManager = LinearLayoutManager(
							requireContext(),
							LinearLayoutManager.VERTICAL,
							false
						)

						binding.rvPengumuman.adapter = pengumumanAdapter

						// navigate to detail
						pengumumanAdapter.setOnItemClickCallback(object : PengumumanAdapter.OnItemClickCallBack {
							override fun onItemClicked(broadcastId: String) {
								val action =
									MahasiswaPengumumanFragmentDirections.actionMahasiswaPengumumanFragmentToMahasiswaDetailPengumumanFragment(
										broadcastId
									)

								// Menggunakan findNavController() dari fragment saat ini untuk navigasi
								findNavController().navigate(action)
							}
						})

					}


				}

				else -> {}

			}
		}
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaPengumumanFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content),
				message,
				"OK"
			) {
				customSnackbar.dismissSnackbar()
				if (message == "Berhasil keluar!" || message == "Gagal! Anda telah masuk melalui perangkat lain." || message == "Pengguna tidak ditemukan!" || message == "Akses tidak sah!" || message == "Sesi anda telah berakhir, silahkan masuk terlebih dahulu.") {

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				} else if (message == "null" || message.equals(null) || message == "Terjadi kesalahan!") {
					restartFragment()
				} else if (message == "Password berhasil diubah, silahkan masuk kembali.") {

					val intent = Intent(requireContext(), SplashscreenActivity::class.java)
					requireContext().startActivity(intent)
					requireActivity().finishAffinity()
				}
			}
		}

	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaPengumumanFragment

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
			setShimmerVisibility(shimmerCvPengumuman, isLoading)
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}