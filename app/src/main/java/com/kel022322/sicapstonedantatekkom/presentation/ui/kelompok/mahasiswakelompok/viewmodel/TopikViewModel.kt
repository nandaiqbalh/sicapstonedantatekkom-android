package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response.TopikRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.topik.TopikRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopikViewModel @Inject constructor(
	private val topikRemoteRepository: TopikRemoteRepository
) : ViewModel(){

	private var _getTopikResult = MutableLiveData<Resource<TopikRemoteResponse>>()
	val getTopikResult: LiveData<Resource<TopikRemoteResponse>> get() = _getTopikResult

	fun getTopik(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getTopikResult.postValue(Resource.Loading())

			try {
				val data =
					topikRemoteRepository.getTopikCapstone(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getTopikResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getTopikResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getTopikResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}
}