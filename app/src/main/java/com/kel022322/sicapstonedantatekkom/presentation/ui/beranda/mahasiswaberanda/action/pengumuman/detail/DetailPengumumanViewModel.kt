package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.pengumuman.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request.BroadcastDetailRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.response.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.broadcast.BroadcastRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPengumumanViewModel @Inject constructor(
	private val broadcastRemoteRepository: BroadcastRemoteRepository,
) : ViewModel() {

	private var _broadcastDetailResult = MutableLiveData<Resource<BroadcastDetailRemoteResponse>>()
	val broadcastDetailResult: LiveData<Resource<BroadcastDetailRemoteResponse>> get() = _broadcastDetailResult

	fun getBroadcastDetail(
		broadcastDetailRemoteRequestBody: BroadcastDetailRemoteRequestBody,
	) {
		viewModelScope.launch(Dispatchers.IO) {
			_broadcastDetailResult.postValue(Resource.Loading())

			try {
				val data =
					broadcastRemoteRepository.getBroadcastDetail(broadcastDetailRemoteRequestBody)

				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_broadcastDetailResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_broadcastDetailResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_broadcastDetailResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

}

