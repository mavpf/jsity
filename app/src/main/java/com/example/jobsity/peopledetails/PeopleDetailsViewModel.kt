package com.example.jobsity.peopledetails

import ShowIndexApi
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.network.Credits
import com.example.jobsity.network.ShowIndex
import kotlinx.coroutines.launch

class PeopleDetailsViewModel : ViewModel() {

    enum class _statusPeopleDetails { LOADING, DONE, ERROR }
    enum class _statusCastDetails { LOADING, DONE, ERROR }

    private val _castDetailsData = MutableLiveData<MutableList<ShowIndex>>()
    val castDetailsData: LiveData<MutableList<ShowIndex>> = _castDetailsData

    private val _peopleDetailsData = MutableLiveData<Credits?>()
    val peopleDetailsData: LiveData<Credits?> = _peopleDetailsData

    fun getPeopleDetails(id: Int) {
        viewModelScope.launch {
            _statusPeopleDetails.LOADING
            try {
                _peopleDetailsData.value =
                    ShowIndexApi.retrofitService.getPeopleDetails(id, "castcredits")
                _statusPeopleDetails.DONE
            } catch (e: Exception) {
                _statusPeopleDetails.ERROR
                _peopleDetailsData.value = null
            }
        }
    }

    fun getCasting(id: Int) {
        val tempData = mutableListOf<ShowIndex>()
        viewModelScope.launch {
            _statusCastDetails.LOADING
            try {
                val temp =  ShowIndexApi.retrofitService.getShowDetail(id)
                tempData.add(
                    ShowIndex(
                        temp.id,
                        temp.name,
                        temp.genres,
                        temp?.image
                    )
                )
                _statusCastDetails.DONE
            } catch (e: java.lang.Exception) {
                _statusCastDetails.ERROR
                Log.d("ret_err", e.toString())
            }
        }
    }
}