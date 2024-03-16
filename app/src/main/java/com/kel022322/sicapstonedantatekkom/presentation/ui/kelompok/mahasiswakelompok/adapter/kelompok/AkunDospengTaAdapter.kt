package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.RsDospengTa
import com.kel022322.sicapstonedantatekkom.databinding.ItemListAkunBinding
import com.kel022322.sicapstonedantatekkom.util.GlideApp

class AkunDospengTaAdapter : RecyclerView.Adapter<AkunDospengTaAdapter.AkunViewHolder>() {
	private var dospengList: List<RsDospengTa> = emptyList()

	var itemClickListener: ((item: RsDospengTa) -> Unit)? = null

	private lateinit var onItemClickCallBack: OnItemClickCallBack

	fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
		this.onItemClickCallBack = onItemClickCallBack
	}

	private val diffCallback = object : DiffUtil.ItemCallback<RsDospengTa>() {
		override fun areItemsTheSame(oldItem: RsDospengTa, newItem: RsDospengTa): Boolean {
			return oldItem.userId == newItem.userId
		}

		override fun areContentsTheSame(oldItem: RsDospengTa, newItem: RsDospengTa): Boolean {
			return oldItem.hashCode() == newItem.hashCode()
		}
	}

	private val differ = AsyncListDiffer(this, diffCallback)

	fun setList(dospengs: List<RsDospengTa>?) {
		differ.submitList(dospengs)
	}

	inner class AkunViewHolder(private val binding: ItemListAkunBinding) :
		RecyclerView.ViewHolder(binding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(dospeng: RsDospengTa) {
			binding.apply {

				tvNamaDosenPembimbing.text = dospeng.userName
				tvNimDosenPembimbing.text = dospeng.nomorInduk

				GlideApp.with(itemView.context)
					.asBitmap()
					.load(dospeng.userImgUrl)
					.into(ivPhotoDosbing)

			}

			binding.root.setOnClickListener {
				onItemClickCallBack.onItemClicked(dospeng.userId.toString())
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
		fun onItemClicked(dospengId: String)
	}
}