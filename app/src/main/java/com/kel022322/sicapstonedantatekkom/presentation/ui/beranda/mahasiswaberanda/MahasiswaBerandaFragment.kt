package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda

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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.local.action.ActionModel
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.image.request.PhotoProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaBerandaBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.ActionAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.pengumuman.PengumumanViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.pengumuman.adapter.PengumumanAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.profil.ProfileSayaViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaBerandaFragment : Fragment() {

	private var _binding:FragmentMahasiswaBerandaBinding? = null
	private val binding get() = _binding!!

	private val pengumumanViewModel: PengumumanViewModel by viewModels()
	private val profileViewModel: ProfileSayaViewModel by viewModels()

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

		setActionRecyclerView()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setLoading(true)

		setUpInitialValue()

		setPengumumanRecyclerView()
	}

	private fun setActionRecyclerView(){

		val actionPengumuman = ActionModel(R.drawable.ic_action_pengumuman, "Pengumuman", R.id.action_mahasiswaBerandaFragment_to_mahasiswaPengumumanFragment)
		val actionSidangProposal = ActionModel(R.drawable.ic_action_sidang_proposal, "Sidang Proposal", R.id.action_mahasiswaBerandaFragment_to_mahasiswaSidangProposalFragment)
		val actionExpo = ActionModel(R.drawable.ic_action_expo, "Expo Project", R.id.action_mahasiswaBerandaFragment_to_mahasiswaExpoFragment)
		val actionSidangTugasAkhir = ActionModel(R.drawable.ic_action_pengumuman, "Sidang Tugas Akhir", R.id.action_mahasiswaBerandaFragment_to_mahasiswaSidangTugasAkhirFragment)

		val dummyActions = listOf(actionPengumuman, actionSidangProposal, actionExpo, actionSidangTugasAkhir)

		val actionAdapter = ActionAdapter(navController)

		actionAdapter.setList(dummyActions)

		binding.rvActionMahasiswaBeranda.layoutManager = LinearLayoutManager(
			requireContext(),
			LinearLayoutManager.HORIZONTAL,
			false
		)

		binding.rvActionMahasiswaBeranda.adapter = actionAdapter
	}

	private fun setPengumumanRecyclerView(){

		pengumumanViewModel.getBroadcastHome()

		pengumumanViewModel.broadcastHomeResult.observe(viewLifecycleOwner) { broadcastHomeResult ->

			when (broadcastHomeResult) {
				is Resource.Loading -> {
					setLoading(true)
				}
				is Resource.Error -> {
					setLoading(false)

					Log.d("Result error", broadcastHomeResult.payload?.message.toString())

				}

				is Resource.Success -> {
					setLoading(false)

					val message = broadcastHomeResult.payload?.message.toString()
					Log.d("Result success", message)
					Log.d("Result data", broadcastHomeResult.payload?.data?.rs_broadcast?.data.toString())

					showSnackbar(message)

					if (broadcastHomeResult.payload?.data?.rs_broadcast?.data != null){
						val pengumumanAdapter = PengumumanAdapter()

						pengumumanAdapter.setList(broadcastHomeResult.payload.data.rs_broadcast.data)

						binding.rvPengumumanTerbaru.layoutManager = LinearLayoutManager(
							requireContext(),
							LinearLayoutManager.VERTICAL,
							false
						)

						binding.rvPengumumanTerbaru.adapter = pengumumanAdapter
					}
				}

				else -> {}

			}
		}
	}

	private fun setUpInitialValue(){
		profileViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			if (username != null) {
				binding.namauser.text = username
			}
		}

		profileViewModel.getUserId().observe(viewLifecycleOwner) { userId ->
			if (userId != null) {
				profileViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
					apiToken?.let {
						profileViewModel.getPhotoProfile(
							PhotoProfileRemoteRequestBody(
								userId.toString(), it
							)
						)
					}
				}
			}
		}

		profileViewModel.getPhotoProfileResult.observe(viewLifecycleOwner) { getPhotoProfileResult ->
			when (getPhotoProfileResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

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
									.into(ivHomeProfilephoto)
							}

						}
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
	private fun showSnackbar(message: String) {

		customSnackbar.showSnackbarWithAction(
			requireActivity().findViewById(android.R.id.content),
			message,
			"OK"
		) {
			customSnackbar.dismissSnackbar()
			if (message == "Berhasil keluar!" || message == "Token tidak valid!" || message == "Pengguna tidak ditemukan!" || message == "Tidak ada api token!" || message == "Missing api_token in the request body.") {

				profileViewModel.setApiToken("")
				profileViewModel.setUserId("")
				profileViewModel.setStatusAuth(false)

				val intent = Intent(requireContext(), SplashscreenActivity::class.java)
				requireContext().startActivity(intent)
				requireActivity().finishAffinity()
			} else if (message == "null" || message.equals(null) || message == "Terjadi kesalahan!") {
				restartFragment()
			} else if (message == "Password berhasil diubah, silahkan masuk kembali."){

				profileViewModel.setApiToken("")
				profileViewModel.setUserId("")
				profileViewModel.setStatusAuth(false)

				val intent = Intent(requireContext(), SplashscreenActivity::class.java)
				requireContext().startActivity(intent)
				requireActivity().finishAffinity()
			}
		}
	}

	private fun restartFragment() {

		// Detach fragment
		val ftDetach = parentFragmentManager.beginTransaction()
		ftDetach.detach(this@MahasiswaBerandaFragment)
		ftDetach.commit()

		// Attach fragment
		val ftAttach = parentFragmentManager.beginTransaction()
		ftAttach.attach(this@MahasiswaBerandaFragment)
		ftAttach.commit()
	}

	private fun setLoading(isLoading: Boolean) {
		if (isLoading) {
			binding.shimmerBerandaNamauser.visibility = View.VISIBLE
			binding.shimmerBerandaNamauser.startShimmer()
			binding.namauser.visibility = View.GONE

			binding.shimmerIvHomeProfilephoto.visibility = View.VISIBLE
			binding.shimmerIvHomeProfilephoto.startShimmer()
			binding.ivHomeProfilephoto.visibility = View.GONE

			binding.shimmerCvPengumumanTerbaru.visibility = View.VISIBLE
			binding.shimmerCvPengumumanTerbaru.startShimmer()
			binding.rvPengumumanTerbaru.visibility = View.GONE
		} else {
			binding.shimmerBerandaNamauser.visibility = View.GONE
			binding.shimmerBerandaNamauser.stopShimmer()
			binding.namauser.visibility = View.VISIBLE

			binding.shimmerIvHomeProfilephoto.visibility = View.GONE
			binding.shimmerIvHomeProfilephoto.stopShimmer()
			binding.ivHomeProfilephoto.visibility = View.VISIBLE

			binding.shimmerCvPengumumanTerbaru.visibility = View.GONE
			binding.shimmerCvPengumumanTerbaru.stopShimmer()
			binding.rvPengumumanTerbaru.visibility = View.VISIBLE
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}