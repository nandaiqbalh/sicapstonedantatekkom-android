package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaBerandaBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.pengumuman.adapter.PengumumanAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel.ProfileIndexViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaBerandaFragment : Fragment() {

	private var _binding: FragmentMahasiswaBerandaBinding? = null
	private val binding get() = _binding!!

	private val mahasiswaBerandaViewModel: MahasiswaBerandaViewModel by viewModels()

	private val userViewModel: UserViewModel by viewModels()
	private val profileIndexViewModel: ProfileIndexViewModel by viewModels()

	private lateinit var navController: NavController

	private val customSnackbar = CustomSnackbar()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaBerandaBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onStart() {
		super.onStart()

		// Find the NavController using requireActivity()
		navController = requireActivity().findNavController(R.id.fragmentContainerViewHome)

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setLoading(true)
		setLoadingPengumuman(true)

		getProfile()

		setPengumumanRecyclerView()

		setActionListener()

		getDataBeranda()

	}

	private fun setActionListener(){

		with(binding){

			cardPengumuman.setOnClickListener{
				findNavController().navigate(R.id.action_mahasiswaBerandaFragment_to_mahasiswaPengumumanFragment)
			}

			cardSidangProposal.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaBerandaFragment_to_mahasiswaSidangProposalFragment)
			}

			cardExpo.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaBerandaFragment_to_mahasiswaExpoFragment)
			}

			cardSidangTa.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaBerandaFragment_to_mahasiswaSidangTugasAkhirFragment)
			}

			ivHomeProfilephoto.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaBerandaFragment_to_mahasiswaProfilFragment)
			}
		}
	}

	private fun setPengumumanRecyclerView() {

		mahasiswaBerandaViewModel.getBroadcastHome()

		mahasiswaBerandaViewModel.broadcastHomeResult.observe(viewLifecycleOwner) { broadcastHomeResult ->
			val resultResponse = broadcastHomeResult.payload

			when (broadcastHomeResult) {
				is Resource.Loading -> {
					setLoadingPengumuman(true)
				}

				is Resource.Error -> {
					setLoadingPengumuman(false)

					val message = resultResponse?.status

					with(binding){
						setViewVisibility(cvErrorPengumumanTerbaru, true)
						tvErrorPengumumanTerbaru.text = message ?: "Mohon periksa kembali koneksi internet Anda!"

						setViewVisibility(cvPengumumanTerbaru, false)
						setViewVisibility(shimmerBerandaNamauser, false)

					}

					Log.d("Broadcast error", broadcastHomeResult.payload?.status.toString())

				}

				is Resource.Success -> {
					setLoadingPengumuman(false)

					val message = broadcastHomeResult.payload?.status
					Log.d("Result success", message.toString())

					if (broadcastHomeResult.payload?.success == false) {
						setLoadingPengumuman(false)
						with(binding){
							setViewVisibility(cvErrorPengumumanTerbaru, true)
							tvErrorPengumumanTerbaru.text = message ?: "Mohon periksa kembali koneksi internet Anda!"

							setViewVisibility(cvPengumumanTerbaru, false)
							setViewVisibility(shimmerBerandaNamauser, false)

						}

					} else if (broadcastHomeResult.payload?.success == true && broadcastHomeResult.payload.data?.rs_broadcast?.data != null) {
						val pengumumanAdapter = PengumumanAdapter()

						pengumumanAdapter.setList(broadcastHomeResult.payload.data.rs_broadcast.data)

						binding.rvPengumumanTerbaru.layoutManager = LinearLayoutManager(
							requireContext(), LinearLayoutManager.VERTICAL, false
						)

						Log.d(
							"DATA BROADCAST",
							broadcastHomeResult.payload.data.rs_broadcast.data.toString()
						)

						binding.rvPengumumanTerbaru.adapter = pengumumanAdapter

						// navigate to detail
						pengumumanAdapter.setOnItemClickCallback(object :
							PengumumanAdapter.OnItemClickCallBack {
							override fun onItemClicked(broadcastId: String) {
								val action =
									MahasiswaBerandaFragmentDirections.actionMahasiswaBerandaFragmentToMahasiswaDetailPengumumanFragment(
										broadcastId
									)
								findNavController().navigate(action)
							}
						})

					}


				}

				else -> {}

			}
		}
	}

	// set toolbar view
	private fun setToolbarWithLocalData() {
		setLoading(true)

		// set username
		userViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			setLoading(false)

			if (username != null && username != "") {
				binding.namauser.text = username
			}
		}

		// set photo profile
		userViewModel.getPhotoProfile().observe(viewLifecycleOwner) { photoUri ->
			setLoading(false)

			Log.d("PHOTO", photoUri.toString())

			if (photoUri != null && photoUri != "") {
				GlideApp.with(this@MahasiswaBerandaFragment).asBitmap().load(photoUri)
					.into(binding.ivHomeProfilephoto)
			}
		}

	}

	private fun getDataBeranda(){
		setLoading(true)
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				mahasiswaBerandaViewModel.getDataBeranda(apiToken)
			}
		}

		mahasiswaBerandaViewModel.getBerandaResult.observe(viewLifecycleOwner) { getBerandaResult ->

			val resultResponse = getBerandaResult.payload
			val status = resultResponse?.status

			when (getBerandaResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					Log.d("Error Profile Index", getBerandaResult.payload?.status.toString())

					setLoading(false)

					setToolbarWithLocalData()

				}

				is Resource.Success -> {
					setLoading(false)

					if (resultResponse?.success == true) {
						Log.d("Succes status", status.toString())

						// set binding
						with(binding) {

							// toolbar
							tvStatusPengumuman.text = "Lihat semua pengumuman!"
							tvStatusSidangProposal.text = resultResponse.data.sidangProposal
							tvStatusExpo.text = resultResponse.data.expo
							tvStatusSidangTa.text = resultResponse.data.sidangTA

						}
					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						} else {
							setToolbarWithLocalData()
						}

					}
				}

				else -> {}
			}
		}

	}

	private fun getProfile() {
		setLoading(true)

		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				profileIndexViewModel.getMahasiswaProfile(apiToken)
			}
		}

		profileIndexViewModel.getProfileResult.observe(viewLifecycleOwner) { getProfileResult ->

			val resultResponse = getProfileResult.payload
			val status = resultResponse?.status

			when (getProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					Log.d("Error Profile Index", getProfileResult.payload?.status.toString())

					setLoading(false)

					setToolbarWithLocalData()

				}

				is Resource.Success -> {
					setLoading(false)

					if (resultResponse?.success == true && resultResponse.data != null) {
						Log.d("Succes status", status.toString())

						// set binding
						with(binding) {

							// toolbar
							namauser.text = resultResponse.data.userName

							GlideApp.with(this@MahasiswaBerandaFragment).asBitmap()
								.load(resultResponse.data.userImgUrl).into(ivHomeProfilephoto)

							userViewModel.setPhotoProfile(resultResponse.data.userImgUrl.toString())
							userViewModel.setUsername(resultResponse.data.userName.toString())
							userViewModel.setNIM(resultResponse.data.nomorInduk.toString())

						}
					} else {
						Log.d("Succes status, but failed", status.toString())

						if (status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						} else {
							setToolbarWithLocalData()
						}

					}
				}

				else -> {}
			}
		}

	}


	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaBerandaFragment

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
	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerBerandaNamauser, isLoading)
			setShimmerVisibility(shimmerIvHomeProfilephoto, isLoading)

			namauser.visibility = if (isLoading) View.GONE else View.VISIBLE
			ivHomeProfilephoto.visibility = if (isLoading) View.GONE else View.VISIBLE

		}
	}

	private fun setLoadingPengumuman(isLoading: Boolean){
		with(binding) {
			setShimmerVisibility(shimmerCvPengumumanTerbaru, isLoading)

			rvPengumumanTerbaru.visibility = if (isLoading) View.GONE else View.VISIBLE
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