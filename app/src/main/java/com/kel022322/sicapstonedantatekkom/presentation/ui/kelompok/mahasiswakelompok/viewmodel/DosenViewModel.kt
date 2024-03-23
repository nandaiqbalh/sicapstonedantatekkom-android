package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing1.DosenPembimbing1RemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing2.DosenPembimbing2RemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.dosen.DosenRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DosenViewModel @Inject constructor(
	private val dosenRemoteRepository: DosenRemoteRepository
) : ViewModel(){

	private var _getDosen1Result = MutableLiveData<Resource<DosenPembimbing1RemoteResponse>>()
	val getDosen1Result: LiveData<Resource<DosenPembimbing1RemoteResponse>> get() = _getDosen1Result

	fun getDosenPembimbing1(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getDosen1Result.postValue(Resource.Loading())

			try {
				val data =
					dosenRemoteRepository.getDataDosenPembimbing1(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getDosen1Result.postValue(Resource.Success(data.payload))
					}

				} else {
					_getDosen1Result.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getDosen1Result.postValue(Resource.Error(e, null))
				}
			}

		}
	}

	private var _getDosen2Result = MutableLiveData<Resource<DosenPembimbing2RemoteResponse>>()
	val getDosen2Result: LiveData<Resource<DosenPembimbing2RemoteResponse>> get() = _getDosen2Result

	fun getDosenPembimbing2(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getDosen2Result.postValue(Resource.Loading())

			try {
				val data =
					dosenRemoteRepository.getDataDosenPembimbing2(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getDosen2Result.postValue(Resource.Success(data.payload))
					}

				} else {
					_getDosen2Result.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getDosen2Result.postValue(Resource.Error(e, null))
				}
			}

		}
	}
}