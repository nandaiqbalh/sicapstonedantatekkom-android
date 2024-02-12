package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda

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
import com.kel022322.sicapstonedantatekkom.data.local.action.ActionModel
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaBerandaBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.adapter.ActionAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.pengumuman.adapter.PengumumanAdapter
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

		setToolbar()

		setPengumumanRecyclerView()

		setActionListener()

	}

	// set action listener
	private fun setActionListener() {

//		binding.ivHomeProfilephoto.setOnClickListener {
//			findNavController().navigate(R.id.action_mahasiswaBerandaFragment_to_mahasiswaProfilFragment)
//		}

	}

	private fun setActionRecyclerView() {

		val actionPengumuman = ActionModel(
			R.drawable.ic_action_pengumuman,
			"Pengumuman",
			R.id.action_mahasiswaBerandaFragment_to_mahasiswaPengumumanFragment
		)
		val actionSidangProposal = ActionModel(
			R.drawable.ic_action_sidang_proposal,
			"Sidang Proposal",
			R.id.action_mahasiswaBerandaFragment_to_mahasiswaSidangProposalFragment
		)
		val actionExpo = ActionModel(
			R.drawable.ic_action_expo,
			"Expo Project",
			R.id.action_mahasiswaBerandaFragment_to_mahasiswaExpoFragment
		)
		val actionSidangTugasAkhir = ActionModel(
			R.drawable.ic_action_sidang_tugas_akhir,
			"Sidang Tugas Akhir",
			R.id.action_mahasiswaBerandaFragment_to_mahasiswaSidangTugasAkhirFragment
		)

		val dummyActions =
			listOf(actionPengumuman, actionSidangProposal, actionExpo, actionSidangTugasAkhir)

		val actionAdapter = ActionAdapter(navController)

		actionAdapter.setList(dummyActions)

		binding.rvActionMahasiswaBeranda.layoutManager = LinearLayoutManager(
			requireContext(), LinearLayoutManager.HORIZONTAL, false
		)

		binding.rvActionMahasiswaBeranda.adapter = actionAdapter
	}

	private fun setPengumumanRecyclerView() {

		mahasiswaBerandaViewModel.getBroadcastHome()

		mahasiswaBerandaViewModel.broadcastHomeResult.observe(viewLifecycleOwner) { broadcastHomeResult ->
			val resultResponse = broadcastHomeResult.payload

			when (broadcastHomeResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = resultResponse?.message

					showSnackbar(message ?: "Terjadi kesalahan!", false)

					binding.tvPengumumanTidakDitemukan.visibility = View.VISIBLE

					Log.d("Broadcast error", broadcastHomeResult.payload?.message.toString())

				}

				is Resource.Success -> {
					setLoading(false)

					val message = broadcastHomeResult.payload?.message
					Log.d("Result success", message.toString())

					if (broadcastHomeResult.payload?.status == false) {
						setLoading(false)
						showSnackbar(message ?: "Terjadi kesalahan!", false)

						binding.tvPengumumanTidakDitemukan.visibility = View.VISIBLE

					} else if (broadcastHomeResult.payload?.status == true && broadcastHomeResult.payload.data?.rs_broadcast?.data != null) {
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
	private fun setToolbar() {
		setLoading(true)

		// set username
		userViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			setLoading(false)

			if (username != null && username != "") {
				binding.namauser.text = username
			}
		}

		// set photo profile
		userViewModel.getPhotoProfile().observe(viewLifecycleOwner) {
			setLoading(false)

			if (it != null && it != "") {
				GlideApp.with(this@MahasiswaBerandaFragment).asBitmap().load(it)
					.into(binding.ivHomeProfilephoto)
			}
		}

	}

	private fun showSnackbar(message: String, isRestart: Boolean) {
		val currentFragment = this@MahasiswaBerandaFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), message, "OK"
			) {
				customSnackbar.dismissSnackbar()
				if (isRestart) {
					restartFragment()
				}
			}
		}
	}

	private fun restartFragment() {
		val currentFragment = this@MahasiswaBerandaFragment

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
			setShimmerVisibility(shimmerBerandaNamauser, isLoading)
			setShimmerVisibility(shimmerIvHomeProfilephoto, isLoading)
			setShimmerVisibility(shimmerCvPengumumanTerbaru, isLoading)

			namauser.visibility = if (isLoading) View.GONE else View.VISIBLE
			ivHomeProfilephoto.visibility = if (isLoading) View.GONE else View.VISIBLE
			rvPengumumanTerbaru.visibility = if (isLoading) View.GONE else View.VISIBLE

		}
		if (isLoading) {
			binding.tvPengumumanTidakDitemukan.visibility = View.GONE
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