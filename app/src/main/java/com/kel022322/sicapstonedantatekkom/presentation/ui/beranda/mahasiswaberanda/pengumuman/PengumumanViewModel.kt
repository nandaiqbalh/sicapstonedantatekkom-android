package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.pengumuman

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast.BroadcastRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PengumumanViewModel @Inject constructor(
	private val broadcastRemoteRepository: BroadcastRemoteRepository,
) : ViewModel() {

	private val _broadcastResult = MutableLiveData<Resource<BroadcastPaginateRemoteResponse>>()
	val broadcastResult: LiveData<Resource<BroadcastPaginateRemoteResponse>> get() = _broadcastResult // LiveData untuk diobserve di luar kelas

	private val _broadcastHomeResult = MutableLiveData<Resource<BroadcastPaginateRemoteResponse>>()
	val broadcastHomeResult: LiveData<Resource<BroadcastPaginateRemoteResponse>> get() = _broadcastHomeResult // LiveData untuk diobserve di luar kelas

	fun getBroadcast() {
		viewModelScope.launch(Dispatchers.IO) {
			_broadcastResult.postValue(Resource.Loading())

			try {
				val data = broadcastRemoteRepository.getBroadcast()

//				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_broadcastResult.postValue(Resource.Success(data.payload))

					}
				} else {
					_broadcastResult.postValue(Resource.Error(data.exception, null))
				}
			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_broadcastResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	fun getBroadcastHome() {
		viewModelScope.launch(Dispatchers.IO) {
			_broadcastHomeResult.postValue(Resource.Loading())

			try {
				val data = broadcastRemoteRepository.getBroadcastHome()

//				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_broadcastHomeResult.postValue(Resource.Success(data.payload))

					}
				} else {
					_broadcastHomeResult.postValue(Resource.Error(data.exception, null))
				}
			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_broadcastHomeResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

}

