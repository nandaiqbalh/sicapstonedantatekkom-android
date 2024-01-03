package com.kel022322.sicapstonedantatekkom.presentation.ui.auth.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.request.AuthLogoutRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.auth.logout.AuthLogoutRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
	private val authLogoutRemoteRepository: AuthLogoutRemoteRepository,
	private val authDataStoreManager: AuthDataStoreManager
): ViewModel() {

	private val _logoutResult = MutableLiveData<Resource<AuthLogoutRemoteResponse>>()
	val logoutResult : LiveData<Resource<AuthLogoutRemoteResponse>>  get() = _logoutResult

	fun authLogout(authLogoutRequestBody: AuthLogoutRequestBody){
		viewModelScope.launch(Dispatchers.IO) {
			_logoutResult.postValue(Resource.Loading())

			try {
				val data = authLogoutRemoteRepository.authLogout(authLogoutRequestBody)

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

	fun getApiToken(): LiveData<String?> = authDataStoreManager.getApiToken.asLiveData()

	fun setApiToken(apiToken: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setApiToken(apiToken)
	}

	fun setStatusAuth(statusAuth: Boolean) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setStatusAuth(statusAuth)
	}

	fun getUserId(): LiveData<String?> = authDataStoreManager.getUserId.asLiveData()

	fun setUserId(userId: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setUserId(userId)
	}

}