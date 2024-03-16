package com.kel022322.sicapstonedantatekkom.presentation.ui.kelompok.mahasiswakelompok.daftarkelompok

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.kelompok.KelompokSayaRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DaftarKelompokViewModel @Inject constructor(
	private val kelompokSayaRemoteRepository: KelompokSayaRemoteRepository,
) : ViewModel() {


	private var _addKelompokPunyaKelompokResult =
		MutableLiveData<Resource<AddKelompokPunyaKelompokRemoteResponse>>()
	val addKelompokPunyaKelompok: LiveData<Resource<AddKelompokPunyaKelompokRemoteResponse>> get() = _addKelompokPunyaKelompokResult

	fun addKelompokPunyaKelompok(
		apiToken: String,
		addKelompokPunyaKelompokRemoteRequestBody: AddKelompokPunyaKelompokRemoteRequestBody,
	) {

		viewModelScope.launch(Dispatchers.IO) {

			_addKelompokPunyaKelompokResult.postValue(Resource.Loading())

			try {
				val data = kelompokSayaRemoteRepository.addKelompokPunyaKelompok(
					apiToken,
					addKelompokPunyaKelompokRemoteRequestBody
				)

				if (data.payload != null) {
					viewModelScope.launch(Dispatchers.Main) {
						_addKelompokPunyaKelompokResult.postValue(Resource.Success(data.payload))
					}
				} else {
					_addKelompokPunyaKelompokResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_addKelompokPunyaKelompokResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}

}