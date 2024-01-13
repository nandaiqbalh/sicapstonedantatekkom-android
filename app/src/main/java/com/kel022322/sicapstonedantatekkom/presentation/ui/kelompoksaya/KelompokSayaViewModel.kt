package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompoksaya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok.KelompokSayaRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KelompokSayaViewModel @Inject constructor(
	private val kelompokSayaRemoteRepository: KelompokSayaRemoteRepository,
	private val authDataStoreManager: AuthDataStoreManager,
) : ViewModel() {

	private var _getKelompokSayaResult = MutableLiveData<Resource<KelompokSayaRemoteResponse>>()
	val getKelompokSayaResult: LiveData<Resource<KelompokSayaRemoteResponse>> get() = _getKelompokSayaResult

	fun getKelompokSaya(getKelompokSayaRemoteRequestBody: KelompokSayaRemoteRequestBody) {

		viewModelScope.launch(Dispatchers.IO) {
			_getKelompokSayaResult.postValue(Resource.Loading())

			try {
				val data =
					kelompokSayaRemoteRepository.getKelompokSaya(getKelompokSayaRemoteRequestBody)

				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getKelompokSayaResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getKelompokSayaResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getKelompokSayaResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}


	private var _addKelompokIndividuResult =
		MutableLiveData<Resource<AddKelompokIndividuRemoteResponse>>()
	val addKelompokIndividuResult: LiveData<Resource<AddKelompokIndividuRemoteResponse>> get() = _addKelompokIndividuResult
	fun addKelompokIndividu(addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody) {

		viewModelScope.launch(Dispatchers.IO) {
			_addKelompokIndividuResult.postValue(Resource.Loading())

			try {
				val data = kelompokSayaRemoteRepository.addKelompokIndividu(
					addKelompokIndividuRemoteRequestBody
				)

				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {

						_addKelompokIndividuResult.postValue(Resource.Success(data.payload))

					}
				} else {
					_addKelompokIndividuResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_addKelompokIndividuResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

	private var _addKelompokPunyaKelompokResult =
		MutableLiveData<Resource<AddKelompokPunyaKelompokRemoteResponse>>()
	val addKelompokPunyaKelompok: LiveData<Resource<AddKelompokPunyaKelompokRemoteResponse>> get() = _addKelompokPunyaKelompokResult

	fun addKelompokPunyaKelompok(addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody){

		viewModelScope.launch(Dispatchers.IO){

			_addKelompokPunyaKelompokResult.postValue(Resource.Loading())

			try {
				val data = kelompokSayaRemoteRepository.addKelompokPunyaKelompok(addKelompokPunyaKelompokRemoteRequestBody)

				if (data.payload!= null){
					viewModelScope.launch(Dispatchers.Main){
						_addKelompokPunyaKelompokResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_addKelompokPunyaKelompokResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_addKelompokPunyaKelompokResult.postValue(Resource.Error(e, null))
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