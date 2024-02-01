package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.pengumuman.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDetailPengumumanBinding
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


class MahasiswaDetailPengumumanFragment : Fragment() {

	private var _binding: FragmentMahasiswaDetailPengumumanBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding =
			FragmentMahasiswaDetailPengumumanBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setBindingView()

		setButtonListener()
	}

	private fun setButtonListener(){

		binding.cvArrowBack.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	private fun setBindingView() {
		val broadcast =
			MahasiswaDetailPengumumanFragmentArgs.fromBundle(requireArguments()).broadcast

		binding.apply {
			val base64Image = broadcast?.broadcastImagePath.toString()

			if (base64Image != "null" || base64Image != "") {
				// Decode base64 string to byte array
				val decodedBytes = decodeBase64ToBitmap(base64Image)

				GlideApp.with(this@MahasiswaDetailPengumumanFragment)
					.asBitmap()
					.load(decodedBytes)
					.into(ivDetailPengumuman)
			}
			val linkPendukung = broadcast?.linkPendukung
			tvLinkText.setOnClickListener {
				// Pastikan linkPendukung tidak null sebelum membuka browser
				linkPendukung?.let {
					// Buat Intent untuk membuka tautan di browser
					val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkPendukung))
					startActivity(intent)
				}
			}

			tvTitlePengumumanDetail.text = broadcast?.namaEvent
			tvContentPengumumanDetail.text = broadcast?.keterangan

			val formattedTimeDifference = formatTimeDifference(broadcast?.createdDate.toString())
			tvPosdatePengumumanDetail.text = formattedTimeDifference


		}
	}

	private fun decodeBase64ToBitmap(base64: String): Bitmap {
		val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
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
				val hoursAgo = DateUtils.getRelativeTimeSpanString(
					date.time,
					currentTime,
					DateUtils.HOUR_IN_MILLIS
				)
				"$hoursAgo"
			}

			else -> {
				// 24 hours or more, format as days ago
				val formattedDate =
					SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date)
				DateUtils.getRelativeTimeSpanString(date.time, currentTime, DateUtils.DAY_IN_MILLIS)
				formattedDate
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}