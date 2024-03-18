package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.terimakelompok.TerimaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.tolakkelompok.TolakKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok.KelompokSayaRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KelompokIndexViewModel @Inject constructor(
	private val kelompokSayaRemoteRepository: KelompokSayaRemoteRepository,
) : ViewModel() {

	private var _getKelompokSayaResult = MutableLiveData<Resource<KelompokSayaRemoteResponse>>()
	val getKelompokSayaResult: LiveData<Resource<KelompokSayaRemoteResponse>> get() = _getKelompokSayaResult

	fun getKelompokSaya(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getKelompokSayaResult.postValue(Resource.Loading())

			try {
				val data =
					kelompokSayaRemoteRepository.getKelompokSaya(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getKelompokSayaResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getKelompokSayaResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getKelompokSayaResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}


	private var _terimaKelompokResult = MutableLiveData<Resource<TerimaKelompokRemoteResponse>>()
	val terimaKelompokResult: LiveData<Resource<TerimaKelompokRemoteResponse>> get() = _terimaKelompokResult

	fun terimaKelompok(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_terimaKelompokResult.postValue(Resource.Loading())

			try {
				val data =
					kelompokSayaRemoteRepository.terimaKelompok(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_terimaKelompokResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_terimaKelompokResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_terimaKelompokResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

	private var _tolakKelompokResult = MutableLiveData<Resource<TolakKelompokRemoteResponse>>()
	val tolakKelompokResult: LiveData<Resource<TolakKelompokRemoteResponse>> get() = _tolakKelompokResult

	fun tolakKelompok(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_tolakKelompokResult.postValue(Resource.Loading())

			try {
				val data =
					kelompokSayaRemoteRepository.tolakKelompok(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_tolakKelompokResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_tolakKelompokResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_tolakKelompokResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}


}