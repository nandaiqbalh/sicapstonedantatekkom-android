package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kel022322.sicapstonedantatekkom.data.local.model.jeniskelamin.JenisKelaminModel
import com.kel022322.sicapstonedantatekkom.databinding.ItemJenisKelaminDropdownBinding

class JenisKelaminDuaAdapter(context: Context, private val jenisKelaminList: List<JenisKelaminModel>) :
	ArrayAdapter<JenisKelaminModel>(context, 0, jenisKelaminList) {

	@SuppressLint("SetTextI18n", "ViewHolder")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val binding = ItemJenisKelaminDropdownBinding.inflate(LayoutInflater.from(context), parent, false)
		val jenisKelamin = getItem(position)

		binding.jenisKelamin.text = jenisKelamin?.jenisJelamin

		return binding.root
	}
}
