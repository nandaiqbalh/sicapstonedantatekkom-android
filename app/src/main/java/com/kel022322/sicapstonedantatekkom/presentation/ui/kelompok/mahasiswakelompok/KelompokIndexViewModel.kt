package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.status.UpdateStatusKelompokBackwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.status.UpdateStatusKelompokForwardRemoteResponse
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


	private var _updateStatusKelompokForwardResult = MutableLiveData<Resource<UpdateStatusKelompokForwardRemoteResponse>>()
	val updateStatusKelompokForwardResult: LiveData<Resource<UpdateStatusKelompokForwardRemoteResponse>> get() = _updateStatusKelompokForwardResult

	fun updateStatusKelompokForward(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_updateStatusKelompokForwardResult.postValue(Resource.Loading())

			try {
				val data =
					kelompokSayaRemoteRepository.updateStatusKelompokForward(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_updateStatusKelompokForwardResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updateStatusKelompokForwardResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_updateStatusKelompokForwardResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

	// backward
	private var _updateStatusKelompokBackwardResult = MutableLiveData<Resource<UpdateStatusKelompokBackwardRemoteResponse>>()
	val updateStatusKelompokBackwardResult: LiveData<Resource<UpdateStatusKelompokBackwardRemoteResponse>> get() = _updateStatusKelompokBackwardResult

	fun updateStatusKelompokBackward(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_updateStatusKelompokBackwardResult.postValue(Resource.Loading())

			try {
				val data =
					kelompokSayaRemoteRepository.updateStatusKelompokBackward(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_updateStatusKelompokBackwardResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updateStatusKelompokBackwardResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_updateStatusKelompokBackwardResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

}