package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.request.DosenRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.request.MahasiswaIndexRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.dosen.DosenRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok.KelompokSayaRemoteRepository
import com.kel022322.sicapstonedantatekkom.data.remote.repository.mahasiswa.MahasiswaRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KelompokIndexViewModel @Inject constructor(
	private val kelompokSayaRemoteRepository: KelompokSayaRemoteRepository,
	private val dosenRemoteRepository: DosenRemoteRepository,
	private val mahasiswaRemoteRepository: MahasiswaRemoteRepository,
	private val authDataStoreManager: AuthDataStoreManager,
) : ViewModel() {

	private var _getKelompokSayaResult = MutableLiveData<Resource<KelompokSayaRemoteResponse>>()
	val getKelompokSayaResult: LiveData<Resource<KelompokSayaRemoteResponse>> get() = _getKelompokSayaResult

	fun getKelompokSaya(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getKelompokSayaResult.postValue(Resource.Loading())

			try {
				val data =
					kelompokSayaRemoteRepository.getKelompokSaya(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
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

	private var _getDataDosenResult = MutableLiveData<Resource<DosenRemoteResponse>>()
	val getDataDosenResult: LiveData<Resource<DosenRemoteResponse>> get() = _getDataDosenResult

	fun getDataDosen(dosenRemoteRequestBody: DosenRemoteRequestBody) {

		viewModelScope.launch(Dispatchers.IO) {
			_getDataDosenResult.postValue(Resource.Loading())

			try {
				val data =
					dosenRemoteRepository.getDataDosen(dosenRemoteRequestBody)

				Log.d("DATA DOSEN", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getDataDosenResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getDataDosenResult.postValue(Resource.Error(data.exception, null))
					Log.d("ERROR DATA DOSEN", data.exception.toString())

				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getDataDosenResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}


	private var _getDataMahasiswaResult = MutableLiveData<Resource<MahasiswaIndexRemoteResponse>>()
	val getDataMahasiswaResult: LiveData<Resource<MahasiswaIndexRemoteResponse>> get() = _getDataMahasiswaResult

	fun getDataMahasiswa(mahasiswaIndexRemoteRequestBody: MahasiswaIndexRemoteRequestBody) {

		viewModelScope.launch(Dispatchers.IO) {
			_getDataMahasiswaResult.postValue(Resource.Loading())

			try {
				val data =
					mahasiswaRemoteRepository.getDataMahasiswa(mahasiswaIndexRemoteRequestBody)

				Log.d("DATA Mahasiswa", data.payload.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getDataMahasiswaResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getDataMahasiswaResult.postValue(Resource.Error(data.exception, null))
					Log.d("ERROR DATA Mahasiswa", data.exception.toString())

				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getDataMahasiswaResult.postValue(Resource.Error(e, null))
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

	fun getUsername(): LiveData<String?> = authDataStoreManager.getUsername.asLiveData()

	fun setUsername(username: String) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setUsername(username)
	}
	fun getPhotoProfile(): LiveData<String?> = authDataStoreManager.getPhotoProfile.asLiveData()

	fun setStatusAuth(statusAuth: Boolean) = CoroutineScope(Dispatchers.IO).launch {
		authDataStoreManager.setStatusAuth(statusAuth)
	}

}