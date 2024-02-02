package com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.capstone.MahasiswaDokumenCapstoneFragment
import com.kel022322.sicapstonedantatekkom.presentation.ui.dokumen.mahasiswadokumen.tugasakhir.MahasiswaDokumenTugasAkhirFragment

class FragmentPageAdapter(
	fragmentManager: FragmentManager,
	lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
	override fun getItemCount(): Int {
		return 2
	}

	override fun createFragment(position: Int): Fragment {
		return if (position == 0) {
			MahasiswaDokumenCapstoneFragment()
		} else {
			MahasiswaDokumenTugasAkhirFragment()
		}
	}
}