package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.daftarkelompok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kel022322.sicapstonedantatekkom.R

class MahasiswaKelompokDaftarKelompokFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(
			R.layout.fragment_mahasiswa_kelompok_daftar_kelompok,
			container,
			false
		)
	}

}