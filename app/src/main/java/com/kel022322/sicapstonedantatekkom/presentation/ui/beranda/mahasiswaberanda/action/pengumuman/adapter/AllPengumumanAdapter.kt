package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.pengumuman.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.allbroadcast.RsBroadcast
import com.kel022322.sicapstonedantatekkom.databinding.ItemMahasiswaPengumumanBinding
import com.kel022322.sicapstonedantatekkom.util.GlideApp

class AllPengumumanAdapter : RecyclerView.Adapter<AllPengumumanAdapter.BroadcastViewHolder>() {
	private var broadcastList: List<RsBroadcast> = emptyList()

	var itemClickListener: ((item: RsBroadcast) -> Unit)? = null

	private lateinit var onItemClickCallBack: OnItemClickCallBack

	fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
		this.onItemClickCallBack = onItemClickCallBack
	}

	private val diffCallback = object : DiffUtil.ItemCallback<RsBroadcast>() {
		override fun areItemsTheSame(
			oldItem: RsBroadcast,
			newItem: RsBroadcast,
		): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(
			oldItem: RsBroadcast,
			newItem: RsBroadcast,
		): Boolean {
			return oldItem.hashCode() == newItem.hashCode()
		}
	}

	private val differ = AsyncListDiffer(this, diffCallback)

	fun setList(broadcasts: List<RsBroadcast>?) {
		differ.submitList(broadcasts)
	}

	inner class BroadcastViewHolder(private val binding: ItemMahasiswaPengumumanBinding) :
		RecyclerView.ViewHolder(binding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(broadcast: RsBroadcast) {
			binding.apply {

				tvItemPengumumanTitle.text = broadcast.namaEvent
				tvItemPengumumanContent.text = broadcast.keterangan

				tvItemPengumumanPostdata.text = broadcast.postDate

				GlideApp.with(itemView.context)
					.asBitmap()
					.load(broadcast.broadcastImageUrl)
					.into(ivItemPengumuman)

			}

			binding.root.setOnClickListener {
				onItemClickCallBack.onItemClicked(broadcast.id.toString())
			}
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BroadcastViewHolder {
		val binding = ItemMahasiswaPengumumanBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
		return BroadcastViewHolder(binding)
	}

	override fun onBindViewHolder(holder: BroadcastViewHolder, position: Int) {
		holder.bind(differ.currentList[position])
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	interface OnItemClickCallBack {
		fun onItemClicked(broadcastId: String)
	}
}