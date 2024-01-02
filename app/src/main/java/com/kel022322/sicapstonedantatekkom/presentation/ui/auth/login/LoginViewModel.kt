package com.kel022322.sicapstonedantatekkom.presentation.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.AuthRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val authDataStoreManager: AuthDataStoreManager,
	private val authRemoteRepository: AuthRemoteRepository
) : ViewModel() {
	
	private val _authResult = MutableLiveData<Resource<AuthRemoteResponse>>()
	val authResult: LiveData<Resource<AuthRemoteResponse>> get() = _authResult // LiveData untuk diobserve di luar kelas

	
	fun authLogin(authRequestBody: AuthRequestBody) {
		viewModelScope.launch(Dispatchers.IO) {
			_authResult.postValue(Resource.Loading())

			try {
				val data = authRemoteRepository.authLogin(authRequestBody)

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

	fun getApiToken(): LiveData<String?> = authDataStoreManager.getApiToken.asLiveData()

	fun setApiToken(apiToken: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setApiToken(apiToken)
	}
	

}