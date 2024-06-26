package com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.profile.ProfileRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileIndexViewModel @Inject constructor(
	private val profileRemoteRepository: ProfileRemoteRepository,
) : ViewModel() {

	private var _getProfileResult = MutableLiveData<Resource<ProfileRemoteResponse>>()
	val getProfileResult: LiveData<Resource<ProfileRemoteResponse>> get() = _getProfileResult

	fun getMahasiswaProfile(apiToken: String) {
		viewModelScope.launch(Dispatchers.IO) {
			_getProfileResult.postValue(Resource.Loading())

			try {
				val data = profileRemoteRepository.getMahasiswaProfile(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_getProfileResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getProfileResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getProfileResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

}