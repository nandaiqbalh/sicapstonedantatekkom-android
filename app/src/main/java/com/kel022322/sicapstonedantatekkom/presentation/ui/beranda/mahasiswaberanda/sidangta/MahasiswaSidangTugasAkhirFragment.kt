package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.sidangta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaSidangTugasAkhirBinding

class MahasiswaSidangTugasAkhirFragment : Fragment() {

	private var _binding: FragmentMahasiswaSidangTugasAkhirBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaSidangTugasAkhirBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setButtonListener()
	}

	private fun setButtonListener() {

		binding.ivCircleBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		binding.icBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}