package com.kel022322.sicapstonedantatekkom.presentation.ui.filesaya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.file.FileRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class FileSayaViewModel @Inject constructor(
	private val authDataStoreManager: AuthDataStoreManager,
	private val fileRemoteRepository: FileRemoteRepository,
) : ViewModel() {

	private var _getFileIndexResult = MutableLiveData<Resource<FileIndexRemoteResponse>>()
	val getFileIndexResult: LiveData<Resource<FileIndexRemoteResponse>> get() = _getFileIndexResult

	fun getFileIndex(fileIndexRemoteRequestBody: FileIndexRemoteRequestBody) {

		viewModelScope.launch(Dispatchers.IO) {
			_getFileIndexResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.getFileIndex(fileIndexRemoteRequestBody)

				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_getFileIndexResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_getFileIndexResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getFileIndexResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	private var _uploadMakalahProcessResult = MutableLiveData<Resource<UploadMakalahProcessRemoteResponse>> ()
	val uploadMakalahProcessResult: LiveData<Resource<UploadMakalahProcessRemoteResponse>> get() = _uploadMakalahProcessResult

	fun uploadMakalahProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		makalah: MultipartBody.Part
	){
		viewModelScope.launch(Dispatchers.IO){

			_uploadMakalahProcessResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.uploadMakalahProcess(userId, apiToken, idMahasiswa, makalah)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_uploadMakalahProcessResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_uploadMakalahProcessResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_uploadMakalahProcessResult.postValue(Resource.Error(e, null))
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