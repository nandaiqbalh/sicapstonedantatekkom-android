package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.RsMahasiswa
import com.kel022322.sicapstonedantatekkom.databinding.ItemSiklusDropdownBinding

class ListMahasiswaTigaAdapter(context: Context, private val mahasiswaList: List<RsMahasiswa>) :
	ArrayAdapter<RsMahasiswa>(context, 0, mahasiswaList) {

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

		val mahasiswa = getItem(position)

		binding.siklusText.text = mahasiswa?.userName
		binding.tanggalText.text = "NIP: ${mahasiswa?.nomorInduk}"

		return listItemView
	}
}
