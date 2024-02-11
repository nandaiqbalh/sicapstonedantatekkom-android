package com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.profile.ProfileRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePasswordViewModel @Inject constructor(
	private val profileRemoteRepository: ProfileRemoteRepository,
) : ViewModel() {

	// update password profile
	private var _updatePasswordResult = MutableLiveData<Resource<UpdatePasswordRemoteResponse>>()
	val updatePasswordResult: LiveData<Resource<UpdatePasswordRemoteResponse>> get() = _updatePasswordResult


	fun updatePasswordProfile(
		apiToken: String,
		updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody,
	) {

		viewModelScope.launch(Dispatchers.IO) {
			_updatePasswordResult.postValue(Resource.Loading())

			try {

				val data =
					profileRemoteRepository.updatePasswordProfile(apiToken, updatePasswordRemoteRequestBody)
				Log.d("PAYLOAD", data.payload.toString())

				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_updatePasswordResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updatePasswordResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_updatePasswordResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

}