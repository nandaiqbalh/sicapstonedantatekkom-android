package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.pengumuman.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
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

	private val detailPengumumanViewModel: DetailPengumumanViewModel by viewModels()

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

		// call the function based on what we want to do
		setBindingView()
		setButtonListener()
	}

	// function to trigger action when the user press the button or called an action
	private fun setButtonListener() {

		// back to home
		binding.ivCircleBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}

		// back to home
		binding.icBackArrow.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	// set binding view to set the view with data we have
	private fun setBindingView() {

		// call set loading function as a initial loading
		setLoading(true)

		// get ID from the adapter with NavArgs
		val broadcastId =
			MahasiswaDetailPengumumanFragmentArgs.fromBundle(requireArguments()).broadcastId

		// doing networking for broadcast detail
		detailPengumumanViewModel.getBroadcastDetail(BroadcastDetailRemoteRequestBody(broadcastId.toString()))

		// observe what we got from network
		detailPengumumanViewModel.broadcastDetailResult.observe(viewLifecycleOwner) { broadcastDetailResult ->

			when (broadcastDetailResult) {

				is Resource.Loading -> {
					// set loading when still loading the data from network
					setLoading(true)
				}

				is Resource.Error -> {
					// set loading to false (not loading anymore) if the result is error
					setLoading(false)

					// get the error message and show the snackbar
					val message = broadcastDetailResult.payload?.message

					with(binding){
						setViewVisibility(cvErrorDetailPengumuman, true)
						tvErrorDetailPengumuman.text = message ?: "Terjadi kesalahan"

						setViewVisibility(linearLayoutDetailPengumuman, false)
						setViewVisibility(shimmerDetailPengumumanFragment, false)

						showSnackbar(message ?: "Terjadi kesalahan!")
					}
				}

				is Resource.Success -> {

					// set loading to false if we got response from the network
					setLoading(false)

					// log the message and data
					val message = broadcastDetailResult.payload?.message
					Log.d("Result message", message.toString())
					Log.d("Result data", broadcastDetailResult.payload?.data.toString())

					// data is not null, then set the view with the data
					if (broadcastDetailResult.payload?.status == true) {

						val broadcastResult = broadcastDetailResult.payload.data!!.broadcast

						binding.apply {

							GlideApp.with(this@MahasiswaDetailPengumumanFragment).asBitmap()
								.load(broadcastResult?.broadcastImageUrl).into(ivDetailPengumuman)

							val linkPendukung = broadcastResult?.linkPendukung

							linearLayoutLink.visibility =
								if (linkPendukung.isNullOrEmpty()) View.GONE else View.VISIBLE

							tvLinkText.setOnClickListener {
								linkPendukung?.takeIf { it.isNotBlank() }?.let {
									val url =
										if (!it.startsWith("http://") && !it.startsWith("https://")) {
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

							setViewVisibility(cvErrorDetailPengumuman, false)
						}
					} else {
						setLoading(true)

						with(binding){
							setViewVisibility(cvErrorDetailPengumuman, true)
							tvErrorDetailPengumuman.text = message ?: "Terjadi kesalahan"

							setViewVisibility(linearLayoutDetailPengumuman, false)
							setViewVisibility(shimmerDetailPengumumanFragment, false)

							showSnackbar(message ?: "Terjadi kesalahan!")
						}

					}
				}

				else -> {}
			}
		}
	}

	// function to get time difference
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
					date.time, currentTime, DateUtils.HOUR_IN_MILLIS
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

	// function to show snackbar to the screen
	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaDetailPengumumanFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), message, "OK"
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
		}

	}

	private fun setShimmerVisibility(shimmerView: View, isLoading: Boolean) {
		shimmerView.visibility = if (isLoading) View.VISIBLE else View.GONE
		(shimmerView as? ShimmerFrameLayout)?.run {
			if (isLoading) startShimmer() else stopShimmer()
		}
	}

	private fun setViewVisibility(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.GONE
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}