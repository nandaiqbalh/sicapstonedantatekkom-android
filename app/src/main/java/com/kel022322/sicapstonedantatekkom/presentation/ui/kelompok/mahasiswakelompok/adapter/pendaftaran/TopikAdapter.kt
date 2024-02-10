package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.RsTopik
import com.kel022322.sicapstonedantatekkom.databinding.ItemSiklusDropdownBinding

class TopikAdapter(context: Context, private val topikList: List<RsTopik>) :
	ArrayAdapter<RsTopik>(context, 0, topikList) {

	@SuppressLint("SetTextI18n", "ViewHolder")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val binding = ItemSiklusDropdownBinding.inflate(LayoutInflater.from(context), parent, false)
		val topik = getItem(position)

		binding.siklusText.text = topik?.nama
		binding.tanggalText.visibility = View.GONE

		return binding.root
	}
}
