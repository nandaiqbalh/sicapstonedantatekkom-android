package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.expo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.request.DaftarExpoRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.response.DaftarExpoRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response.ExpoIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.expo.ExpoRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MahasiswaExpoViewModel @Inject constructor(
	private val expoRemoteRepository: ExpoRemoteRepository
): ViewModel(){

	private var _getExpoResult = MutableLiveData<Resource<ExpoIndexRemoteResponse>>()
	val getExpoResult: LiveData<Resource<ExpoIndexRemoteResponse>> get() = _getExpoResult

	fun getExpo(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getExpoResult.postValue(Resource.Loading())

			try {
				val data =
					expoRemoteRepository.getDataExpo(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				Log.d("PAYLOAD ERROR", data.message.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getExpoResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getExpoResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getExpoResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	private var _daftarExpoResult = MutableLiveData<Resource<DaftarExpoRemoteResponse>>()
	val daftarExpoResult: LiveData<Resource<DaftarExpoRemoteResponse>> get() = _daftarExpoResult

	fun daftarExpo(apiToken: String, daftarExpoRemoteRequestBody: DaftarExpoRemoteRequestBody) {

		viewModelScope.launch(Dispatchers.IO) {
			_daftarExpoResult.postValue(Resource.Loading())

			try {
				val data =
					expoRemoteRepository.daftarExpo(apiToken, daftarExpoRemoteRequestBody)

				Log.d("PAYLOAD", data.payload.toString())
				Log.d("PAYLOAD ERROR", data.message.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_daftarExpoResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_daftarExpoResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_daftarExpoResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}
}