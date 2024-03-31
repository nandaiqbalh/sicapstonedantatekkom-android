package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.kel022322.sicapstonedantatekkom.R
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.databinding.FragmentMahasiswaKelompokBinding
import com.kel022322.sicapstonedantatekkom.presentation.ui.auth.UserViewModel
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.FragmentDaftarCapstonePageAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.adapter.kelompok.AkunDosbingAdapter
import com.kel022322.sicapstonedantatekkom.presentation.ui.splashscreen.SplashscreenActivity
import com.kel022322.sicapstonedantatekkom.util.CustomSnackbar
import com.kel022322.sicapstonedantatekkom.util.GlideApp
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MahasiswaKelompokFragment : Fragment() {

	private var _binding: FragmentMahasiswaKelompokBinding? = null
	private val binding get() = _binding!!

	private val userViewModel: UserViewModel by viewModels()
	private val kelompokViewModel: KelompokIndexViewModel by viewModels()

	private val customSnackbar = CustomSnackbar()

	private var fragmentPageAdapter: FragmentDaftarCapstonePageAdapter? = null
	private var isAlertDialogShowing = false

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMahasiswaKelompokBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setToolbar()

		setViewPager()

		checkKelompok()

		buttonActionListener()
	}

	private fun buttonActionListener() {

		with(binding) {

			btnSelengkapnyaKelompok.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaKelompokDetailFragment)

			}

			ivHomeProfilephotoKelompok.setOnClickListener {
				findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaProfilFragment)
			}


		}
	}

	private fun checkKelompok() {
		setLoading(true)

		// get status kelompok
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.getKelompokSaya(apiToken)

			}
		}

		kelompokViewModel.getKelompokSayaResult.observe(viewLifecycleOwner) { getKelompokSayaResult ->
			val resultResponse = getKelompokSayaResult.payload
			val status = resultResponse?.status

			when (getKelompokSayaResult) {
				is Resource.Loading -> {
					setLoading(true)

				}

				is Resource.Error -> {
					setLoading(false)

					Log.d("Error Kelompok Index", getKelompokSayaResult.payload?.status.toString())

					// set view condition
					with(binding) {
						setViewVisibility(cvValueKelompok, false)
						setViewVisibility(cvValueDosbing, false)
						setViewVisibility(cvValueKelompok, false)
						setViewVisibility(linearLayoutDaftarCapstone, false)
						setViewVisibility(cvErrorKelompok, true)

						setViewVisibility(shimmerFragmentKelompok, false)

					}
				}

				is Resource.Success -> {
					setLoading(false)

					val message = getKelompokSayaResult.payload
					Log.d("Result success", message.toString())

					if (resultResponse?.success == true) {

						if (resultResponse.data?.getAkun?.statusIndividu == "Didaftarkan!") {
							setCardKelompok(getKelompokSayaResult)

							// Panggil fungsi showCustomAlertDialog dengan parameter yang diperlukan
							showCustomAlertDialog(
								"Konfirmasi",
								"Setuju bergabung dengan",
								resultResponse.data.kelompok?.pengusulKelompok.toString(),
								{
									// Aksi yang akan dijalankan saat tombol "Yes" ditekan
									terimaKelompok()
								},
								{
									// Aksi yang akan dijalankan saat tombol "No" ditekan
									tolakKelompok()
								}
							)
							// if already have kelompok
							with(binding) {
								setViewVisibility(cvValueKelompok, true)
								setViewVisibility(cvValueDosbing, true)
								setViewVisibility(linearLayoutDaftarCapstone, false)
								setViewVisibility(cvErrorKelompok, false)

								setViewVisibility(shimmerFragmentKelompok, false)

							}

						} else {
							setCardKelompok(getKelompokSayaResult)

							// if already have kelompok
							with(binding) {
								setViewVisibility(cvValueKelompok, true)
								setViewVisibility(cvValueDosbing, true)
								setViewVisibility(linearLayoutDaftarCapstone, false)
								setViewVisibility(cvErrorKelompok, false)

								setViewVisibility(shimmerFragmentKelompok, false)

							}
						}


					} else {
						Log.d("Succes status, but failed", status.toString())


						if (status == "Authorization Token not found" || status == "Token is Expired" || status == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")

							actionIfLogoutSucces()
						} else if (resultResponse?.data?.kelompok?.idSiklus == 0) {
							// siklus sudah tidak aktif
							with(binding) {
								setViewVisibility(cvValueKelompok, false)
								setViewVisibility(cvValueDosbing, false)
								setViewVisibility(linearLayoutDaftarCapstone, false)
								setViewVisibility(cvErrorKelompok, true)
								tvErrorKelompok.text = status ?: "Terjadi kesalahan"

								setViewVisibility(shimmerFragmentKelompok, false)

							}
						} else if (resultResponse?.data?.kelompok == null) {
							// (kelompok still null) set conditionally view
							with(binding) {
								setViewVisibility(cvValueKelompok, false)
								setViewVisibility(cvValueDosbing, false)
								setViewVisibility(linearLayoutDaftarCapstone, true)
								setViewVisibility(cvErrorKelompok, false)

								setViewVisibility(shimmerFragmentKelompok, false)
							}
						}
					}

				}

				else -> {}
			}
		}
	}

	private fun terimaKelompok() {
		setLoading(true)
		// terima
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.terimaKelompok(apiToken)
			}
		}

		kelompokViewModel.terimaKelompokResult.observe(viewLifecycleOwner) { terimaKelompokResult ->
			when (terimaKelompokResult) {
				is Resource.Loading -> setLoading(true)
				is Resource.Error -> {
					setLoading(false)
					showSnackbar(
						terimaKelompokResult.payload?.status
							?: "Mohon periksa kembali koneksi internet Anda!"
					)
				}

				is Resource.Success -> {
					setLoading(false)
					val message = terimaKelompokResult.payload
					Log.d("Result success", message.toString())
					if (terimaKelompokResult.payload?.success == true) {
						showSnackbar(message?.status ?: "Berhasil menyetujui kelompok!")
						findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaBerandaFragment)
					} else {
						val statusTerima = terimaKelompokResult.payload?.status
						if (statusTerima == "Authorization Token not found" || statusTerima == "Token is Expired" || statusTerima == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")
							actionIfLogoutSucces()
						} else {
							showSnackbar(
								statusTerima
									?: "Mohon periksa kembali koneksi internet Anda!"
							)
						}
					}
				}

				else -> {}
			}
		}
	}

	private fun tolakKelompok() {
		setLoading(true)
		// tolak
		userViewModel.getApiToken().observe(viewLifecycleOwner) { apiToken ->
			apiToken?.let {
				kelompokViewModel.tolakKelompok(apiToken)
			}
		}

		kelompokViewModel.tolakKelompokResult.observe(viewLifecycleOwner) { tolakKelompokResult ->
			when (tolakKelompokResult) {
				is Resource.Loading -> setLoading(true)
				is Resource.Error -> {
					setLoading(false)
					showSnackbar(
						tolakKelompokResult.payload?.status
							?: "Mohon periksa kembali koneksi internet Anda!"
					)
				}

				is Resource.Success -> {
					setLoading(false)
					val message = tolakKelompokResult.payload
					Log.d("Result success", message.toString())
					if (tolakKelompokResult.payload?.success == true) {
						showSnackbar(message?.status ?: "Berhasil menolak kelompok!")
						findNavController().navigate(R.id.action_mahasiswaKelompokFragment_to_mahasiswaBerandaFragment)

					} else {
						val statusTolak = tolakKelompokResult.payload?.status
						if (statusTolak == "Authorization Token not found" || statusTolak == "Token is Expired" || statusTolak == "Token is Invalid") {
							showSnackbar("Sesi anda telah berakhir :(")
							actionIfLogoutSucces()
						} else {
							showSnackbar(
								statusTolak
									?: "Mohon periksa kembali koneksi internet Anda!"
							)
						}
					}
				}

				is Resource.Empty -> TODO()
			}
		}
	}


	private fun setToolbar() {
		setLoading(true)

		// set username
		userViewModel.getUsername().observe(viewLifecycleOwner) { username ->
			setLoading(false)

			if (username != null && username != "") {
				binding.tvNamaUserKelompok.text = username
			}
		}

		// set photo profile
		userViewModel.getPhotoProfile().observe(viewLifecycleOwner) { photoProfile ->
			setLoading(false)

			if (photoProfile != null && photoProfile != "") {
				GlideApp.with(this@MahasiswaKelompokFragment).asBitmap().load(photoProfile)
					.into(binding.ivHomeProfilephotoKelompok)
			}
		}

	}

	private fun setCardKelompok(getKelompokSayaResult: Resource<KelompokSayaRemoteResponse>) {

		val data = getKelompokSayaResult.payload?.data

		if (data?.kelompok?.idKelompok != null) {

			val colorRed = ContextCompat.getColor(requireContext(), R.color.StatusRed)
			val colorOrange = ContextCompat.getColor(requireContext(), R.color.StatusOrange)
			val colorGreen = ContextCompat.getColor(requireContext(), R.color.StatusGreen)

			// Kemudian dalam bagian pengaturan warna teks
			with(binding) {
				tvValueStatusKelompok.text = data.kelompok.statusKelompok ?: "Belum Mendaftar Capstone!"

				when (data.kelompok.statusKelompok) {
					"Menunggu Penetapan Kelompok!",
					"Menunggu Penetapan Dosbing!",
					"Menunggu Persetujuan Anggota!",
					"Menunggu Persetujuan Dosbing!",
					"Menunggu Persetujuan Penguji!",
					"Menunggu Validasi Kelompok!",
					"Menunggu Validasi Expo!" -> {
						tvValueStatusKelompok.setTextColor(colorOrange)
					}
					"Validasi Kelompok Berhasil!",
					"C100 Telah Disetujui!",
					"Penguji Proposal Ditetapkan!",
					"Dijadwalkan Sidang Proposal!",
					"Persetujuan Penguji Berhasil!",
					"Lulus Sidang Proposal!",
					"C200 Telah Disetujui!",
					"C300 Telah Disetujui!",
					"C400 Telah Disetujui!",
					"C500 Telah Disetujui!",
					"Validasi Expo Berhasil!",
					"Lulus Expo Project!",
					"Lulus Capstone Project!" -> {
						tvValueStatusKelompok.setTextColor(colorGreen)
					}
					else -> {
						tvValueStatusKelompok.setTextColor(colorRed)
					}
				}
			}

			//  kelompok sudah valid
			with(binding) {

				val dataKelompok = data.kelompok
				// card kelompok
				tvValueStatusKelompok.text = dataKelompok.statusKelompok
				tvValueNomorKelompok.text = dataKelompok.nomorKelompok ?: "-"
				tvValueTopik.text = dataKelompok.namaTopik ?: "-"
				tvValueJudul.text = dataKelompok.judulCapstone ?: "-"

				val dataDosbing = data.rsDosbing
				// card dosbing
				val akunDosbingAdapter = AkunDosbingAdapter()

				akunDosbingAdapter.setList(dataDosbing)

				binding.rvAkunDosbingKelompok.layoutManager = LinearLayoutManager(
					requireContext(),
					LinearLayoutManager.VERTICAL,
					false
				)
				rvAkunDosbingKelompok.adapter = akunDosbingAdapter

				// navigate to detail if necessary
				akunDosbingAdapter.setOnItemClickCallback(object :
					AkunDosbingAdapter.OnItemClickCallBack {
					override fun onItemClicked(dosbingId: String) {
					}
				})
			}

		} else {
			//  kelompok belum valid
			with(binding) {
				// card kelompok
				"Menunggu Validasi Kelompok!".also { tvValueStatusKelompok.text = it }

				btnSelengkapnyaKelompok.visibility = View.GONE

				tvErrorKelompokDosbingEmpty.visibility = View.VISIBLE
			}

		}

	}


	private fun setViewPager() {

		fragmentPageAdapter = FragmentDaftarCapstonePageAdapter(childFragmentManager, lifecycle)

		with(binding) {
			tabLayoutDaftarCapstone.addTab(tabLayoutDaftarCapstone.newTab().setText("Individu"))
			tabLayoutDaftarCapstone.addTab(
				tabLayoutDaftarCapstone.newTab().setText("Kelompok")
			)

			viewPagerDaftarCapstone.adapter = fragmentPageAdapter

			tabLayoutDaftarCapstone.addOnTabSelectedListener(object :
				TabLayout.OnTabSelectedListener {
				override fun onTabSelected(tab: TabLayout.Tab?) {
					viewPagerDaftarCapstone.currentItem = tab!!.position

				}

				override fun onTabUnselected(tab: TabLayout.Tab?) {


				}

				override fun onTabReselected(tab: TabLayout.Tab?) {
				}
			})

			viewPagerDaftarCapstone.registerOnPageChangeCallback(object :
				ViewPager2.OnPageChangeCallback() {
				override fun onPageSelected(position: Int) {
					super.onPageSelected(position)
					tabLayoutDaftarCapstone.selectTab(tabLayoutDaftarCapstone.getTabAt(position))
				}
			})


		}
	}

	private fun showSnackbar(message: String) {
		val currentFragment = this@MahasiswaKelompokFragment

		if (currentFragment.isVisible) {
			customSnackbar.showSnackbarWithAction(
				requireActivity().findViewById(android.R.id.content), message, "OK"
			) {
				customSnackbar.dismissSnackbar()
			}
		}
	}

	private fun actionIfLogoutSucces() {
		// set auth data store
		userViewModel.setApiToken("")
		userViewModel.setUserId("")
		userViewModel.setUsername("")
		userViewModel.setStatusAuth(false)

		val intent = Intent(requireContext(), SplashscreenActivity::class.java)
		requireContext().startActivity(intent)
		requireActivity().finishAffinity()
	}

	private fun setViewVisibility(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.GONE
	}

	private fun setLoading(isLoading: Boolean) {
		with(binding) {
			setShimmerVisibility(shimmerBerandaNamauser, isLoading)
			setShimmerVisibility(shimmerIvHomeProfilephoto, isLoading)
			setShimmerVisibility(shimmerFragmentKelompok, isLoading)

			tvNamaUserKelompok.visibility = if (isLoading) View.GONE else View.VISIBLE
			ivHomeProfilephotoKelompok.visibility = if (isLoading) View.GONE else View.VISIBLE

			if (isLoading) {
				cvValueKelompok.visibility = View.GONE
				cvValueDosbing.visibility = View.GONE
				cvErrorKelompok.visibility = View.GONE
				linearLayoutDaftarCapstone.visibility = View.GONE
			}
		}


	}

	private fun showCustomAlertDialog(
		title: String,
		message: String,
		pengusulKelompok: String,
		positiveAction: () -> Unit,
		negativeAction: () -> Unit, // Tambahkan parameter untuk tombol "No"
	) {
		if (isAlertDialogShowing) {
			// Jika alert dialog sedang ditampilkan, keluar dari fungsi
			return
		}
		isAlertDialogShowing = true

		val builder = AlertDialog.Builder(requireContext()).create()
		val view =
			layoutInflater.inflate(R.layout.dialog_custom_alert_dialog_konfirmasi_kelompok, null)
		builder.setView(view)
		builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

		val buttonYes = view.findViewById<Button>(R.id.btn_alert_yes)
		val buttonNo = view.findViewById<Button>(R.id.btn_alert_no)
		val alertTitle = view.findViewById<TextView>(R.id.tv_alert_title)
		val alertMessage = view.findViewById<TextView>(R.id.tv_alert_message)
		val alertPengusulKelompok = view.findViewById<TextView>(R.id.tv_alert_pengusul)

		alertTitle.text = title
		alertMessage.text = message
		alertPengusulKelompok.text = pengusulKelompok

		buttonYes.setOnClickListener {
			positiveAction.invoke()
			builder.dismiss()
			isAlertDialogShowing = false // Setelah menutup dialog, atur kembali flag

		}

		buttonNo.setOnClickListener {
			negativeAction.invoke() // Panggil aksi "No" di sini
			builder.dismiss()
			isAlertDialogShowing = false // Setelah menutup dialog, atur kembali flag
		}
		builder.setOnDismissListener {
			isAlertDialogShowing = false // Atur kembali flag saat dialog ditutup
		}

		builder.setCanceledOnTouchOutside(false)
		builder.show()
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