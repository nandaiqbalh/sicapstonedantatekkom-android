package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.pengumuman.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.format.DateUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.DataXBroadcastPaginate
import com.kel022322.sicapstonedantatekkom.databinding.ItemMahasiswaPengumumanBinding
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class PengumumanAdapter: RecyclerView.Adapter<PengumumanAdapter.BroadcastViewHolder>() {
	private var broadcastList: List<DataXBroadcastPaginate> = emptyList()

	var itemClickListener: ((item: DataXBroadcastPaginate) -> Unit)? = null

	private lateinit var onItemClickCallBack: OnItemClickCallBack

	fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
		this.onItemClickCallBack = onItemClickCallBack
	}

	private val diffCallback = object : DiffUtil.ItemCallback<DataXBroadcastPaginate>() {
		override fun areItemsTheSame(oldItem: DataXBroadcastPaginate, newItem: DataXBroadcastPaginate): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: DataXBroadcastPaginate, newItem: DataXBroadcastPaginate): Boolean {
			return oldItem.hashCode() == newItem.hashCode()
		}
	}

	private val differ = AsyncListDiffer(this, diffCallback)

	fun setList(broadcasts: List<DataXBroadcastPaginate>?) {
		differ.submitList(broadcasts)
	}

	inner class BroadcastViewHolder(private val binding: ItemMahasiswaPengumumanBinding) : RecyclerView.ViewHolder(binding.root) {
		@SuppressLint("SetTextI18n")
		fun bind(broadcast: DataXBroadcastPaginate) {
			binding.apply {

				tvItemPengumumanTitle.text = broadcast.namaEvent
				tvItemPengumumanContent.text = broadcast.keterangan

				val formattedTimeDifference = formatTimeDifference(broadcast.createdDate.toString())
				tvItemPengumumanPostdata.text = formattedTimeDifference

				val base64Image = broadcast.broadcastImagePath.toString()

				if (base64Image != "null" || base64Image != "" || base64Image != null) {
					// Decode base64 string to byte array
					val decodedBytes = decodeBase64ToBitmap(base64Image)

					GlideApp.with(itemView.context)
						.asBitmap()
						.load(decodedBytes)
						.into(ivItemPengumuman)
				}
			}

			binding.root.setOnClickListener {
				itemClickListener?.invoke(broadcast)

			}
		}

		private fun formatTimeDifference(createdDate: String): String {
			val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
			val date = dateFormat.parse(createdDate)
			val currentTime = System.currentTimeMillis()

			val timeDifference = currentTime - date!!.time

			return when {
				timeDifference < TimeUnit.HOURS.toMillis(1) -> {
					// Less than 1 hour, format as minutes ago
					val minutesAgo = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
					"$minutesAgo minutes ago"
				}
				timeDifference < TimeUnit.DAYS.toMillis(1) -> {
					// Less than 24 hours, format as hours ago
					val hoursAgo = DateUtils.getRelativeTimeSpanString(date.time, currentTime, DateUtils.HOUR_IN_MILLIS)
					"$hoursAgo"
				}
				else -> {
					// 24 hours or more, format as days ago
					val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date)
					DateUtils.getRelativeTimeSpanString(date.time, currentTime, DateUtils.DAY_IN_MILLIS)
					formattedDate
				}
			}
		}


		private fun decodeBase64ToBitmap(base64: String): Bitmap {
			val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
			return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BroadcastViewHolder {
		val binding = ItemMahasiswaPengumumanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return BroadcastViewHolder(binding)
	}

	override fun onBindViewHolder(holder: BroadcastViewHolder, position: Int) {
		holder.bind(differ.currentList[position])
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	interface OnItemClickCallBack {
		fun onItemClicked(data: DataXBroadcastPaginate)
	}
}