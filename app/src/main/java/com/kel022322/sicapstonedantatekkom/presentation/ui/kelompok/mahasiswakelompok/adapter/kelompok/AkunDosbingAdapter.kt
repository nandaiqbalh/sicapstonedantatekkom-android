package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.RsDosbing
import com.kel022322.sicapstonedantatekkom.databinding.ItemListAkunBinding
import com.kel022322.sicapstonedantatekkom.util.GlideApp

class AkunDosbingAdapter : RecyclerView.Adapter<AkunDosbingAdapter.AkunViewHolder>() {
	private var dosbingList: List<RsDosbing> = emptyList()

	var itemClickListener: ((item: RsDosbing) -> Unit)? = null

	private lateinit var onItemClickCallBack: OnItemClickCallBack

	fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
		this.onItemClickCallBack = onItemClickCallBack
	}

	private val diffCallback = object : DiffUtil.ItemCallback<RsDosbing>() {
		override fun areItemsTheSame(oldItem: RsDosbing, newItem: RsDosbing): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: RsDosbing, newItem: RsDosbing): Boolean {
			return oldItem.hashCode() == newItem.hashCode()
		}
	}

	private val differ = AsyncListDiffer(this, diffCallback)

	fun setList(dosbings: List<RsDosbing>?) {
		differ.submitList(dosbings)
	}

	inner class AkunViewHolder(private val binding: ItemListAkunBinding) :
		RecyclerView.ViewHolder(binding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(dosbing: RsDosbing) {
			binding.apply {

				tvNamaDosenPembimbing.text = dosbing.userName
				tvNimDosenPembimbing.text = dosbing.nomorInduk

				GlideApp.with(itemView.context)
					.asBitmap()
					.load(dosbing.userImgUrl)
					.into(ivPhotoDosbing)

			}

			binding.root.setOnClickListener {
				onItemClickCallBack.onItemClicked(dosbing.id.toString())
			}
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AkunViewHolder {
		val binding =
			ItemListAkunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return AkunViewHolder(binding)
	}

	override fun onBindViewHolder(holder: AkunViewHolder, position: Int) {
		holder.bind(differ.currentList[position])
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	interface OnItemClickCallBack {
		fun onItemClicked(dosbingId: String)
	}
}