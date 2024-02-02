package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.pengumuman.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request.BroadcastDetailRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaDetailPengumumanBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.pengumuman.PengumumanViewModel
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MahasiswaDetailPengumumanFragment : Fragment() {

	private var _binding: FragmentMahasiswaDetailPengumumanBinding? = null
	private val binding get() = _binding!!

	private val pengumumanViewModel: PengumumanViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

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

	private fun setButtonListener() {

		binding.ivCircleBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		binding.icBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	private fun setBindingView() {

		setLoading(true)

		val broadcastId =
			MahasiswaDetailPengumumanFragmentArgs.fromBundle(requireArguments()).broadcastId

		pengumumanViewModel.getBroadcastDetail(BroadcastDetailRemoteRequestBody(broadcastId.toString()))

		pengumumanViewModel.broadcastDetailResult.observe(viewLifecycleOwner) { broadcastDetailResult ->

			when (broadcastDetailResult) {
				is Resource.Loading -> {
					setLoading(true)
				}

				is Resource.Error -> {
					setLoading(false)

					val message = broadcastDetailResult.payload?.message
					showSnackbar(message ?: "Terjadi kesalahan!")
				}

				is Resource.Success -> {
					setLoading(false)

					val message = broadcastDetailResult.payload?.message
					Log.d("Result message", message.toString())
					Log.d("Result message", broadcastDetailResult.payload?.data.toString())

					if (broadcastDetailResult.payload?.data != null) {

						val broadcastResult = broadcastDetailResult.payload.data.broadcast
						binding.apply {
							val base64Image = broadcastResult?.broadcastImagePath

							if (base64Image != "null" || base64Image != "") {
								// Decode base64 string to byte array
								val decodedBytes = decodeBase64ToBitmap(base64Image.toString())

								GlideApp.with(this@MahasiswaDetailPengumumanFragment)
									.asBitmap()
									.load(decodedBytes)
									.into(ivDetailPengumuman)
							}
							val linkPendukung = broadcastResult?.linkPendukung

							linearLayoutLink.visibility = if (linkPendukung.isNullOrEmpty()) View.GONE else View.VISIBLE

							tvLinkText.setOnClickListener {
								linkPendukung?.takeIf { it.isNotBlank() }?.let {
									val url = if (!it.startsWith("http://") && !it.startsWith("https://")) {
										URLUtil.guessUrl(it)
									} else {
										it
									}

									// Buat Intent untuk membuka tautan di browser
									val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
									// Gunakan context dari fragment untuk memulai aktivitas
									requireActivity().startActivity(intent)
								}
							}


							tvTitlePengumumanDetail.text = broadcastResult?.namaEvent
							tvContentPengumumanDetail.text = broadcastResult?.keterangan.toString()

							val formattedTimeDifference =
								formatTimeDifference(broadcastResult?.createdDate.toString())
							tvPosdatePengumumanDetail.text = formattedTimeDifference

						}
					} else {
						setLoading(true)
						showSnackbar(message ?: "Terjadi kesalahan!")
					}
				}

				else -> {}
			}
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


	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaDetailPengumumanFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content),
				message,
				"OK"
			) {
				customSnackbar.dismissSnackbar()
				if (message == "null" || message.equals(null) || message == "Terjadi kesalahan!") {
					restartFragment()
				}
			}
		}

	}


	private fun restartFragment() {
		val currentFragment = this@MahasiswaDetailPengumumanFragment

		// Check if the fragment is currently visible
		if (currentFragment.isVisible) {
			// Detach fragment
			val ftDetach = parentFragmentManager.beginTransaction()
			ftDetach.detach(currentFragment)
			ftDetach.commit()

			// Attach fragment
			val ftAttach = parentFragmentManager.beginTransaction()
			ftAttach.attach(currentFragment)
			ftAttach.commit()
		}
	}

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerDetailPengumumanFragment, isLoading)

			linearLayoutDetailPengumuman.visibility = if (isLoading) View.GONE else View.VISIBLE

		}

	}

	private fun setShimmerVisibility(shimmerView: View, isLoading: Boolean) {
		shimmerView.visibility = if (isLoading) View.VISIBLE else View.GONE
		(shimmerView as? ShimmerFrameLayout)?.run {
			if (isLoading) startShimmer() else stopShimmer()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}