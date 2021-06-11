package com.example.jobsity.people

import ShowIndexApi
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.network.PersonResult
import kotlinx.coroutines.launch

class PeopleViewModel: ViewModel() {

    enum class SearchPersonStatus {ERROR, LOADING, DONE}

    val _personData =  MutableLiveData<List<PersonResult>>()
    val personData: LiveData<List<PersonResult>> = _personData

    val _personDataStatus = MutableLiveData<SearchPersonStatus>()

    fun searchPerson(name: String) {
        viewModelScope.launch {
            SearchPersonStatus.LOADING
            try {
                _personData.value = ShowIndexApi.retrofitService.getPeopleName(name)
                _personDataStatus.value = SearchPersonStatus.DONE
            } catch (E: Exception){
                _personDataStatus.value = SearchPersonStatus.ERROR
                _personData.value = listOf()
                Log.d("ret_ee", E.toString())
            }
        }
    }
}