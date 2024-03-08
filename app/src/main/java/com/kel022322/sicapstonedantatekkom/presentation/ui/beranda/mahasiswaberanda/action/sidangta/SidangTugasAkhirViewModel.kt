package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.request.DaftarSidangTARemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.response.DaftarSidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response.SidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status.UpdateStatusIndividuBackwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.status.UpdateStatusIndividuForwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.sidangta.SidangTARemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SidangTugasAkhirViewModel @Inject constructor(
	private val sidangTARemoteRepository: SidangTARemoteRepository
): ViewModel(){

	private var _getSidangTAResult = MutableLiveData<Resource<SidangTARemoteResponse>>()
	val getSidangTAResult: LiveData<Resource<SidangTARemoteResponse>> get() = _getSidangTAResult

	fun getSidangTA(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getSidangTAResult.postValue(Resource.Loading())

			try {
				val data =
					sidangTARemoteRepository.getJadwalSidangTA(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				Log.d("PAYLOAD ERROR", data.message.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getSidangTAResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getSidangTAResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getSidangTAResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}


	private var _updateStatusIndividuForwardResult = MutableLiveData<Resource<UpdateStatusIndividuForwardRemoteResponse>>()
	val updateStatusIndividuForwardResult: LiveData<Resource<UpdateStatusIndividuForwardRemoteResponse>> get() = _updateStatusIndividuForwardResult

	fun updateStatusIndividuForward(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_updateStatusIndividuForwardResult.postValue(Resource.Loading())

			try {
				val data =
					sidangTARemoteRepository.updateStatusIndividuForward(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				Log.d("PAYLOAD ERROR", data.message.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_updateStatusIndividuForwardResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updateStatusIndividuForwardResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_updateStatusIndividuForwardResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}

	private var _updateStatusIndividuBackwardResult = MutableLiveData<Resource<UpdateStatusIndividuBackwardRemoteResponse>>()
	val updateStatusIndividuBackwardResult: LiveData<Resource<UpdateStatusIndividuBackwardRemoteResponse>> get() = _updateStatusIndividuBackwardResult

	fun updateStatusIndividuBackward(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_updateStatusIndividuBackwardResult.postValue(Resource.Loading())

			try {
				val data =
					sidangTARemoteRepository.updateStatusIndividuBackward(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				Log.d("PAYLOAD ERROR", data.message.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_updateStatusIndividuBackwardResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_updateStatusIndividuBackwardResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_updateStatusIndividuBackwardResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}



	private var _daftarSidangTAResult = MutableLiveData<Resource<DaftarSidangTARemoteResponse>>()
	val daftarSidangTAResult: LiveData<Resource<DaftarSidangTARemoteResponse>> get() = _daftarSidangTAResult

	fun daftarSidangTA(apiToken: String, daftarSidangTARemoteRequestBody: DaftarSidangTARemoteRequestBody) {

		viewModelScope.launch(Dispatchers.IO) {
			_daftarSidangTAResult.postValue(Resource.Loading())

			try {
				val data =
					sidangTARemoteRepository.daftarSidangTA(apiToken, daftarSidangTARemoteRequestBody)

				Log.d("PAYLOAD", data.payload.toString())
				Log.d("PAYLOAD ERROR", data.message.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_daftarSidangTAResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_daftarSidangTAResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_daftarSidangTAResult.postValue(Resource.Error(e, null))
				}
			}
		}
	}
}