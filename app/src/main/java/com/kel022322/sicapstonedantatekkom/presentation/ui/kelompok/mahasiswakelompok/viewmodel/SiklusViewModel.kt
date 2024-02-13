package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.siklus.response.SiklusRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.siklus.SiklusRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SiklusViewModel @Inject constructor(
	private val siklusRemoteRepository: SiklusRemoteRepository
) : ViewModel(){

	private var _getSiklusResult = MutableLiveData<Resource<SiklusRemoteResponse>>()
	val getSiklusResult: LiveData<Resource<SiklusRemoteResponse>> get() = _getSiklusResult

	fun getSiklus(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getSiklusResult.postValue(Resource.Loading())

			try {
				val data =
					siklusRemoteRepository.getSiklusCapstone(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getSiklusResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getSiklusResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getSiklusResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}
}