package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast.BroadcastRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MahasiswaBerandaViewModel @Inject constructor(
	private val authDataStoreManager: AuthDataStoreManager,
	private val broadcastRemoteRepository: BroadcastRemoteRepository
) : ViewModel(){

	private val _broadcastHomeResult = MutableLiveData<Resource<BroadcastPaginateRemoteResponse>>()
	val broadcastHomeResult: LiveData<Resource<BroadcastPaginateRemoteResponse>> get() = _broadcastHomeResult // LiveData untuk diobserve di luar kelas

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