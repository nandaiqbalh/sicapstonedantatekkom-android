package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.local.action.ActionModel
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaBerandaBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.ActionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaBerandaFragment : Fragment() {

	private var _binding:FragmentMahasiswaBerandaBinding? = null
	private val binding get() = _binding!!

	private lateinit var navController: NavController

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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}