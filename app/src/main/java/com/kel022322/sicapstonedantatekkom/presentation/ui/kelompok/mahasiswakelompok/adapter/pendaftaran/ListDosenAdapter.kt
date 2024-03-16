package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.RsDosen
import com.kel022322.sicapstonedantatekkom.databinding.ItemSiklusDropdownBinding

class ListDosenAdapter(context: Context, private val dosenList: List<RsDosen>) :
	ArrayAdapter<RsDosen>(context, 0, dosenList) {

	@SuppressLint("SetTextI18n", "ViewHolder")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val binding: ItemSiklusDropdownBinding
		val listItemView: View

		if (convertView == null) {
			binding = ItemSiklusDropdownBinding.inflate(LayoutInflater.from(context), parent, false)
			listItemView = binding.root
			listItemView.tag = binding
		} else {
			binding = convertView.tag as ItemSiklusDropdownBinding
			listItemView = convertView
		}

		val dosen = getItem(position)

		binding.siklusText.text = dosen?.userName
		binding.tanggalText.text = "NIP: ${dosen?.nomorInduk}"

		return listItemView
	}

}

