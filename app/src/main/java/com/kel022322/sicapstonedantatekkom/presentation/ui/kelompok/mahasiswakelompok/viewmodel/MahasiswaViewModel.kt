package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.mahasiswa.MahasiswaRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MahasiswaViewModel @Inject constructor(
	private val mahasiswaRemoteRepository: MahasiswaRemoteRepository
) : ViewModel(){

	private var _getMahasiswaResult = MutableLiveData<Resource<MahasiswaIndexRemoteResponse>>()
	val getMahasiswaResult: LiveData<Resource<MahasiswaIndexRemoteResponse>> get() = _getMahasiswaResult

//	fun getTopik(apiToken: String) {
//
//		viewModelScope.launch(Dispatchers.IO) {
//			_getMahasiswaResult.postValue(Resource.Loading())
//
//			try {
//				val data =
////					mahasiswaRemoteRepository.getDataMahasiswa(apiToken)
//
//				Log.d("PAYLOAD", data.payload.toString())
//				if (data.payload != null) {
//
//					viewModelScope.launch(Dispatchers.Main) {
//						_getMahasiswaResult.postValue(Resource.Success(data.payload))
//					}
//
//				} else {
//					_getMahasiswaResult.postValue(Resource.Error(data.exception, null))
//				}
//
//			} catch (e: Exception) {
//				viewModelScope.launch(Dispatchers.Main) {
//					_getMahasiswaResult.postValue(Resource.Error(e, null))
//				}
//			}
//
//		}
//
//	}
}