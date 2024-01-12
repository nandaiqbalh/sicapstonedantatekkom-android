package com.kel022322.sicapstonedantatekkom.presentation.ui.profilsaya

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.request.ProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.profile.ProfileRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSayaViewModel @Inject constructor(
	private val profileRemoteRepository: ProfileRemoteRepository,
	private val authDataStoreManager: AuthDataStoreManager
) :ViewModel() {

	private var _getProfileResult = MutableLiveData<Resource<ProfileRemoteResponse>>()
	val getProfileResult : LiveData<Resource<ProfileRemoteResponse>> get() = _getProfileResult

	// update profile
	private var _updateProfileResult = MutableLiveData<Resource<UpdateProfileRemoteResponse>>()
	val updateProfileResult : LiveData<Resource<UpdateProfileRemoteResponse>> get() = _updateProfileResult

	// update password profile
	private var _updatePasswordResult = MutableLiveData<Resource<UpdatePasswordRemoteResponse>> ()
	val updatePasswordResult : LiveData<Resource<UpdatePasswordRemoteResponse>> get() = _updatePasswordResult

	fun getMahasiswaProfile(profileRemoteRequestBody: ProfileRemoteRequestBody){
		viewModelScope.launch (Dispatchers.IO){
			_getProfileResult.postValue(Resource.Loading())

			try {
				val data = profileRemoteRepository.getMahasiswaProfile(profileRemoteRequestBody)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_getProfileResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getProfileResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e:Exception){
				viewModelScope.launch(Dispatchers.Main){
					_getProfileResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	fun updateMahasiswaProfile(updateProfileRemoteRequestBody: UpdateProfileRemoteRequestBody){

		viewModelScope.launch(Dispatchers.IO){
			_updateProfileResult.postValue(Resource.Loading())

			try {
				val data = profileRemoteRepository.updateMahasiswaProfile(updateProfileRemoteRequestBody)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_updateProfileResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updateProfileResult.postValue(Resource.Error(data.exception, null))
				}
			} catch (e:Exception){
				viewModelScope.launch(Dispatchers.IO){
					_updateProfileResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	fun updatePasswordProfile(updatePasswordRemoteRequestBody: UpdatePasswordRemoteRequestBody){

		viewModelScope.launch(Dispatchers.IO){
			_updatePasswordResult.postValue(Resource.Loading())

			try {

				val data = profileRemoteRepository.updatePasswordProfile(updatePasswordRemoteRequestBody)

				if (data.payload != null){

					viewModelScope.launch(Dispatchers.Main){
						_updatePasswordResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updatePasswordResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_updatePasswordResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

	fun getApiToken(): LiveData<String?> = authDataStoreManager.getApiToken.asLiveData()

	fun setApiToken(apiToken: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setApiToken(apiToken)
	}

	fun getUserId(): LiveData<String?> = authDataStoreManager.getUserId.asLiveData()

	fun setUserId(userId: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setUserId(userId)
	}

}