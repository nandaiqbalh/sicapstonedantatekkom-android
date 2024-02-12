package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.daftarindividu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok.KelompokSayaRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DaftarIndividuViewModel @Inject constructor(
	private val kelompokSayaRemoteRepository: KelompokSayaRemoteRepository,
) : ViewModel() {

	private var _addKelompokIndividuResult =
		MutableLiveData<Resource<AddKelompokIndividuRemoteResponse>>()
	val addKelompokIndividuResult: LiveData<Resource<AddKelompokIndividuRemoteResponse>> get() = _addKelompokIndividuResult

	fun addKelompokIndividu(
		apiToken: String,
		addKelompokIndividuRemoteRequestBody: AddKelompokIndividuRemoteRequestBody,
	) {

		viewModelScope.launch(Dispatchers.IO) {
			_addKelompokIndividuResult.postValue(Resource.Loading())

			try {
				val data = kelompokSayaRemoteRepository.addKelompokIndividu(
					apiToken,
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

}