package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.RsDosbing
import com.kel022322.sicapstonedantatekkom.databinding.ItemListAkunBinding
import com.kel022322.sicapstonedantatekkom.util.GlideApp

class AkunDosbingAdapter: RecyclerView.Adapter<AkunDosbingAdapter.AkunViewHolder>() {
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

	inner class AkunViewHolder(private val binding: ItemListAkunBinding) : RecyclerView.ViewHolder(binding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(dosbing: RsDosbing) {
			binding.apply {

				tvNamaDosenPembimbing.text = dosbing.userName
				tvNimDosenPembimbing.text = dosbing.nomorInduk

				val base64Image = dosbing.userImgPath.toString()

				if (base64Image != "null" && base64Image != "") {
					// Decode base64 string to byte array
					val decodedBytes = decodeBase64ToBitmap(base64Image)

					GlideApp.with(itemView.context)
						.asBitmap()
						.load(decodedBytes)
						.into(ivPhotoDosbing)
				}
			}

			binding.root.setOnClickListener {
				onItemClickCallBack.onItemClicked(dosbing.id.toString())
			}
		}


		private fun decodeBase64ToBitmap(base64: String): Bitmap {
			val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
			return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
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
		fun onItemClicked(dosbingId: String)
	}
}