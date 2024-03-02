package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.pendaftaran

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.RsDosen
import com.kel022322.sicapstonedantatekkom.databinding.ItemSiklusDropdownBinding
import java.util.Locale

class ListDosenAdapter(context: Context, private val dosenList: List<RsDosen>) :
	ArrayAdapter<RsDosen>(context, 0, dosenList) {

	private var originalList: List<RsDosen> = dosenList

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

	override fun getFilter(): Filter {
		return object : Filter() {
			override fun performFiltering(constraint: CharSequence?): FilterResults {
				val results = FilterResults()
				val filteredList = mutableListOf<RsDosen>()

				if (constraint.isNullOrBlank()) {
					filteredList.addAll(originalList)
				} else {
					val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim()

					for (dosen in originalList) {
						dosen.userName?.let {
							if (it.lowercase(Locale.getDefault()).contains(filterPattern)) {
								filteredList.add(dosen)
							}
						}
					}
				}

				results.values = filteredList
				results.count = filteredList.size
				return results
			}

			@SuppressLint("NotifyDataSetChanged")
			override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
				clear()
				addAll(results?.values as List<RsDosen>)
				notifyDataSetChanged()
			}
		}
	}

	// Add a method to reset the adapter to the original list
	fun resetAdapter() {
		clear()
		addAll(originalList)
		notifyDataSetChanged()
	}
}
