package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.sidangproposal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response.SidangProposalByKelompokResponse
import com.kel022322.sicapstonedantatekkom.data.remote.repository.sidangproposal.SidangProposalRemoteRepository
import com.kel022322.sicapstonedantatekkom.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SidangProposalViewModel @Inject constructor(
	private val sidangProposalRemoteRepository: SidangProposalRemoteRepository
) : ViewModel(){

	private var _getSidangProposalByKelompokResult = MutableLiveData<Resource<SidangProposalByKelompokResponse>>()
	val getSidangProposalByKelompokResult: LiveData<Resource<SidangProposalByKelompokResponse>> get() = _getSidangProposalByKelompokResult

	fun getSidangProposalByKelompok(apiToken: String) {

		viewModelScope.launch(Dispatchers.IO) {
			_getSidangProposalByKelompokResult.postValue(Resource.Loading())

			try {
				val data =
					sidangProposalRemoteRepository.getSidangProposalByKelompok(apiToken)

				Log.d("PAYLOAD", data.payload.toString())
				Log.d("PAYLOAD ERROR", data.message.toString())
				if (data.payload != null) {

					viewModelScope.launch(Dispatchers.Main) {
						_getSidangProposalByKelompokResult.postValue(Resource.Success(data.payload))
					}

				} else {
					_getSidangProposalByKelompokResult.postValue(Resource.Error(data.exception, null))
				}

			} catch (e: Exception) {
				viewModelScope.launch(Dispatchers.Main) {
					_getSidangProposalByKelompokResult.postValue(Resource.Error(e, null))
				}
			}

		}

	}
}