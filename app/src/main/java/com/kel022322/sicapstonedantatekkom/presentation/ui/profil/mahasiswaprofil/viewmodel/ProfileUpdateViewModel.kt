package com.kel022322.sicapstonedantatekkom.presentation.ui.profil.mahasiswaprofil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.profile.ProfileRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileUpdateViewModel @Inject constructor(
	private val profileRemoteRepository: ProfileRemoteRepository,
) : ViewModel() {

	// update profile
	private var _updateProfileResult = MutableLiveData<Resource<UpdateProfileRemoteResponse>>()
	val updateProfileResult: LiveData<Resource<UpdateProfileRemoteResponse>> get() = _updateProfileResult

	fun updateMahasiswaProfile(
		apiToken: String,
		updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody,
	) {

		viewModelScope.launch(Dispatchers.IO) {
			_updateProfileResult.postValue(Resource.Loading())

			try {
				val data =
					profileRemoteRepository.updateMahasiswaProfile(
						apiToken,
						updateProfileRemoteRequestBody
					)
				Log.d("PAYLOAD", data.payload.toString())

				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_updateProfileResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updateProfileResult.postValue(Resource.Error(data.exception, null))
				}
			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.IO) {
					_updateProfileResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	// update photo profile
	private var _updatePhotoProfileResult = MutableLiveData<Resource<UpdateProfileRemoteResponse>>()
	val updatePhotoProfileResult: LiveData<Resource<UpdateProfileRemoteResponse>> get() = _updatePhotoProfileResult

	fun updatePhotoProfile(
		apiToken: String,
		user_img: MultipartBody.Part,
	) {

		viewModelScope.launch(Dispatchers.IO) {
			_updatePhotoProfileResult.postValue(Resource.Loading())

			try {
				val data =
					profileRemoteRepository.updatePhotoProfile(apiToken, user_img)
				Log.d("PAYLOAD", data.payload.toString())

				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_updatePhotoProfileResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updatePhotoProfileResult.postValue(Resource.Error(data.exception, null))
				}
			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.IO) {
					_updatePhotoProfileResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

}