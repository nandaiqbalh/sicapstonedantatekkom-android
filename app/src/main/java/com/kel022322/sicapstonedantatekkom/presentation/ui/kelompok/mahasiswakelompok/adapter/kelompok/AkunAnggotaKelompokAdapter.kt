package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.RsMahasiswa
import com.kel022322.sicapstonedantatekkom.databinding.ItemListAkunBinding
import com.kel022322.sicapstonedantatekkom.util.GlideApp

class AkunAnggotaKelompokAdapter: RecyclerView.Adapter<AkunAnggotaKelompokAdapter.AkunViewHolder>() {
	private var mahasiswaList: List<RsMahasiswa> = emptyList()

	var itemClickListener: ((item: RsMahasiswa) -> Unit)? = null

	private lateinit var onItemClickCallBack: OnItemClickCallBack

	fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
		this.onItemClickCallBack = onItemClickCallBack
	}

	private val diffCallback = object : DiffUtil.ItemCallback<RsMahasiswa>() {
		override fun areItemsTheSame(oldItem: RsMahasiswa, newItem: RsMahasiswa): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: RsMahasiswa, newItem: RsMahasiswa): Boolean {
			return oldItem.hashCode() == newItem.hashCode()
		}
	}

	private val differ = AsyncListDiffer(this, diffCallback)

	fun setList(mahasiswas: List<RsMahasiswa>?) {
		differ.submitList(mahasiswas)
	}

	inner class AkunViewHolder(private val binding: ItemListAkunBinding) : RecyclerView.ViewHolder(binding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(mahasiswa: RsMahasiswa) {
			binding.apply {

				tvNamaDosenPembimbing.text = mahasiswa.userName
				tvNimDosenPembimbing.text = mahasiswa.nomorInduk

				GlideApp.with(itemView.context)
					.asBitmap()
					.load(mahasiswa.userImgUrl)
					.into(ivPhotoDosbing)
			}

			binding.root.setOnClickListener {
				onItemClickCallBack.onItemClicked(mahasiswa.id.toString())
			}
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AkunViewHolder {
		val binding = ItemListAkunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return AkunViewHolder(binding)
	}

	override fun onBindViewHolder(holder: AkunViewHolder, position: Int) {
		holder.bind(differ.currentList[position])
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	interface OnItemClickCallBack {
		fun onItemClicked(mahasiswaId: String)
	}
}