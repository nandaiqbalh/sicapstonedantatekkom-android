package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.dosen.DosenRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DosenViewModel @Inject constructor(
	private val dosenRemoteRepository: DosenRemoteRepository
) : ViewModel(){

	private var _getDosenResult = MutableLiveData<Resource<DosenRemoteResponse>>()
	val getDosenResult: LiveData<Resource<DosenRemoteResponse>> get() = _getDosenResult

//	fun getTopik(apiToken: String) {
//
//		viewModelScope.launch(Dispatchers.IO) {
//			_getDosenResult.postValue(Resource.Loading())
//
//			try {
//				val data =
//					dosenRemoteRepository.getDataDosen(apiToken)
//
//				Log.d("PAYLOAD", data.payload.toString())
//				if (data.payload != null) {
//
//					viewModelScope.launch(Dispatchers.Main) {
//						_getDosenResult.postValue(Resource.Success(data.payload))
//					}
//
//				} else {
//					_getDosenResult.postValue(Resource.Error(data.exception, null))
//				}
//
//			} catch (e: Exception) {
//				viewModelScope.launch(Dispatchers.Main) {
//					_getDosenResult.postValue(Resource.Error(e, null))
//				}
//			}
//
//		}
//
//	}
}