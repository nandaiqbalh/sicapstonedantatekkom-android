package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.local.datastore.auth.AuthDataStoreManager
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.request.DosenRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.DosenRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.request.KelompokSayaRemoteRequestBody
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
class KelompokSayaViewModel @Inject constructor(
	private val kelompokSayaRemoteRepository: KelompokSayaRemoteRepository,
	private val dosenRemoteRepository: DosenRemoteRepository,
	private val mahasiswaRemoteRepository: MahasiswaRemoteRepository,
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
}