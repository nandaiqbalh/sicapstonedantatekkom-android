package com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.login.AuthLoginRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val authDataStoreManager: AuthDataStoreManager,
	private val authLoginRemoteRepository: AuthLoginRemoteRepository
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

	fun setApiToken(apiToken: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setApiToken(apiToken)
	}

	fun setUserId(userId: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setUserId(userId)
	}

	fun getStatusAuth(): LiveData<Boolean?> = authDataStoreManager.getStatusAuth.asLiveData()
	fun setStatusAuth(statusAuth: Boolean) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setStatusAuth(statusAuth)
	}

	fun setUsername(username: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setUsername(username)
	}


}