package com.kel022322.sicapstonedantatekkom.presentation.ui.filesaya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c100.response.UploadC100ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response.UploadC200ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response.UploadC300ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response.UploadC400ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response.UploadC500ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.request.FileIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response.UploadLaporanProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.request.ViewPdfRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.viewpdf.response.ViewPdfRemoteResponse
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

	private var _viewPdfResult = MutableLiveData<Resource<ViewPdfRemoteResponse>> ()
	val viewPdfResult: LiveData<Resource<ViewPdfRemoteResponse>> get() = _viewPdfResult

	fun viewPdf(viewPdfRemoteRequestBody: ViewPdfRemoteRequestBody){
		viewModelScope.launch(Dispatchers.IO){

			try {
				val data = fileRemoteRepository.viewPdf(viewPdfRemoteRequestBody)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_viewPdfResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_viewPdfResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_viewPdfResult.postValue(Resource.Error(e, null))
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

	private var _uploadLaporanProcessResult = MutableLiveData<Resource<UploadLaporanProcessRemoteResponse>> ()
	val uploadLaporanProcessResult: LiveData<Resource<UploadLaporanProcessRemoteResponse>> get() = _uploadLaporanProcessResult

	fun uploadLaporanProcess(
		userId: String,
		apiToken: String,
		idMahasiswa: String,
		laporan_ta: MultipartBody.Part
	){
		viewModelScope.launch(Dispatchers.IO){

			_uploadLaporanProcessResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.uploadLaporanProcess(userId, apiToken, idMahasiswa, laporan_ta)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_uploadLaporanProcessResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_uploadLaporanProcessResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_uploadLaporanProcessResult.postValue(Resource.Error(e, null))
				}
			}

		}
	}

	private var _uploadC100ProcessResult = MutableLiveData<Resource<UploadC100ProcessRemoteResponse>> ()
	val uploadC100ProcessResult: LiveData<Resource<UploadC100ProcessRemoteResponse>> get() = _uploadC100ProcessResult

	fun uploadC100Process(
		userId: String,
		apiToken: String,
		id: String,
		c100: MultipartBody.Part
	){
		viewModelScope.launch(Dispatchers.IO){

			_uploadC100ProcessResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.uploadC100Process(userId, apiToken, id, c100)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_uploadC100ProcessResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_uploadC100ProcessResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_uploadC100ProcessResult.postValue(Resource.Error(e, null))
				}
			}

		}
	}


	private var _uploadC200ProcessResult = MutableLiveData<Resource<UploadC200ProcessRemoteResponse>> ()
	val uploadC200ProcessResult: LiveData<Resource<UploadC200ProcessRemoteResponse>> get() = _uploadC200ProcessResult

	fun uploadC200Process(
		userId: String,
		apiToken: String,
		id: String,
		c200: MultipartBody.Part
	){
		viewModelScope.launch(Dispatchers.IO){

			_uploadC200ProcessResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.uploadC200Process(userId, apiToken, id, c200)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_uploadC200ProcessResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_uploadC200ProcessResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_uploadC200ProcessResult.postValue(Resource.Error(e, null))
				}
			}

		}
	}

	private var _uploadC300ProcessResult = MutableLiveData<Resource<UploadC300ProcessRemoteResponse>> ()
	val uploadC300ProcessResult: LiveData<Resource<UploadC300ProcessRemoteResponse>> get() = _uploadC300ProcessResult

	fun uploadC300Process(
		userId: String,
		apiToken: String,
		id: String,
		c300: MultipartBody.Part
	){
		viewModelScope.launch(Dispatchers.IO){

			_uploadC300ProcessResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.uploadC300Process(userId, apiToken, id, c300)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_uploadC300ProcessResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_uploadC300ProcessResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_uploadC300ProcessResult.postValue(Resource.Error(e, null))
				}
			}

		}
	}

	private var _uploadC400ProcessResult = MutableLiveData<Resource<UploadC400ProcessRemoteResponse>> ()
	val uploadC400ProcessResult: LiveData<Resource<UploadC400ProcessRemoteResponse>> get() = _uploadC400ProcessResult

	fun uploadC400Process(
		userId: String,
		apiToken: String,
		id: String,
		c400: MultipartBody.Part
	){
		viewModelScope.launch(Dispatchers.IO){

			_uploadC400ProcessResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.uploadC400Process(userId, apiToken, id, c400)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_uploadC400ProcessResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_uploadC400ProcessResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_uploadC400ProcessResult.postValue(Resource.Error(e, null))
				}
			}

		}
	}

	private var _uploadC500ProcessResult = MutableLiveData<Resource<UploadC500ProcessRemoteResponse>> ()
	val uploadC500ProcessResult: LiveData<Resource<UploadC500ProcessRemoteResponse>> get() = _uploadC500ProcessResult

	fun uploadC500Process(
		userId: String,
		apiToken: String,
		id: String,
		c500: MultipartBody.Part
	){
		viewModelScope.launch(Dispatchers.IO){

			_uploadC500ProcessResult.postValue(Resource.Loading())

			try {
				val data = fileRemoteRepository.uploadC500Process(userId, apiToken, id, c500)

				if (data.payload != null){
					viewModelScope.launch(Dispatchers.Main){
						_uploadC500ProcessResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_uploadC500ProcessResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception){
				viewModelScope.launch(Dispatchers.Main){
					_uploadC500ProcessResult.postValue(Resource.Error(e, null))
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