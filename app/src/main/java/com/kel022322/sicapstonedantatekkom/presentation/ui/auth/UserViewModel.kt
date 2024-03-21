package com.kel022322.sicapstonedantatekkom.presentation.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.login.AuthLoginRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.logout.AuthLogoutRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
	private val authDataStoreManager: AuthDataStoreManager,
	private val authLoginRemoteRepository: AuthLoginRemoteRepository,
	private val authLogoutRemoteRepository: AuthLogoutRemoteRepository,

	) : ViewModel() {

	private val _authResult = MutableLiveData<Resource<AuthLoginRemoteResponse>>()
	val authResult: LiveData<Resource<AuthLoginRemoteResponse>> get() = _authResult // LiveData untuk diobserve di luar kelas

	fun authLogin(authLoginRequestBody: AuthLoginRequestBody) {
		viewModelScope.launch(Dispatchers.IO) {
			_authResult.postValue(Resource.Loading())

			try {
				val data = authLoginRemoteRepository.authLogin(authLoginRequestBody)

				Log.d("PAYLOAD", data.payload.toString())
				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_authResult.postValue(Resource.Success(data.payload))

					}
				} else {
					_authResult.postValue(Resource.Error(data.exception, null))
				}
			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_authResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	private val _logoutResult = MutableLiveData<Resource<AuthLogoutRemoteResponse>>()
	val logoutResult : LiveData<Resource<AuthLogoutRemoteResponse>>  get() = _logoutResult

	fun authLogout(apiToken: String){
		viewModelScope.launch(Dispatchers.IO) {
			_logoutResult.postValue(Resource.Loading())

			try {
				val data = authLogoutRemoteRepository.authLogout(apiToken)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_logoutResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_logoutResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch (Dispatchers.Main) {
					_logoutResult.postValue(Resource.Error(e, null))
				}
			}

		}
	}



	fun getStatusAuth(): LiveData<Boolean?> = authDataStoreManager.getStatusAuth.asLiveData()
	fun setStatusAuth(statusAuth: Boolean) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setStatusAuth(statusAuth)
	}

	fun getApiToken(): LiveData<String?> = authDataStoreManager.getApiToken.asLiveData()

	fun setApiToken(apiToken: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setApiToken(apiToken)
	}

	fun getNIM(): LiveData<String?> = authDataStoreManager.getNIM.asLiveData()

	fun setNIM(NIM: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setNIM(NIM)
	}

	fun getUserId(): LiveData<String?> = authDataStoreManager.getUserId.asLiveData()

	fun setUserId(userId: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setUserId(userId)
	}

	fun getUsername(): LiveData<String?> = authDataStoreManager.getUsername.asLiveData()

	fun setUsername(username: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setUsername(username)
	}
	fun getPhotoProfile(): LiveData<String?> = authDataStoreManager.getPhotoProfile.asLiveData()

	fun setPhotoProfile(photoProfile: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setPhotoProfile(photoProfile)
	}
}