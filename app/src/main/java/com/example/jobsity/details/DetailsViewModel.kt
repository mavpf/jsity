package com.example.jobsity.details

import ShowIndexApi
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.network.ShowDetails
import com.example.jobsity.network.ShowEpisodes
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    enum class ShowDetailStatus { LOADING, ERROR, DONE }
    enum class ShowEpisodesStatus { LOADING, ERROR, DONE }

    private val _showDetails = MutableLiveData<ShowDetails?>()
    val showDetails: LiveData<ShowDetails?> = _showDetails

    private val _showEpisodes = MutableLiveData<List<ShowEpisodes>?>()
    val showEpisodes: LiveData<List<ShowEpisodes>?> = _showEpisodes

    private val _filteredEpisodes: MutableList<ShowEpisodes> = mutableListOf()

    private val _showDetailStatus = MutableLiveData<ShowDetailStatus>()
    private val _showEpisodesStatus = MutableLiveData<ShowEpisodesStatus>()

    fun addEpisodesPerSeason(data: ShowEpisodes) {
        _filteredEpisodes.add(
            data
        )
    }

    fun clearEpisodesPerSeason() {
        _filteredEpisodes.clear()
    }

    fun getEpisodesPerSeason(): List<ShowEpisodes> {
        return _filteredEpisodes
    }


    fun getShowDetails(
        id: Int
    ) {
        viewModelScope.launch {
            ShowDetailStatus.LOADING
            try {
                _showDetails.value = ShowIndexApi.retrofitService.getShowDetail(id)
                _showDetailStatus.value = ShowDetailStatus.DONE
            } catch (e: java.lang.Exception) {
                _showDetailStatus.value = ShowDetailStatus.ERROR
                _showDetails.value = null
                Log.d("ret_err", e.toString())
            }
        }
    }

    fun getShowEpisodes(
        id: Int
    ) {
        viewModelScope.launch {
            ShowEpisodesStatus.LOADING
            try {
                _showEpisodes.value = ShowIndexApi.retrofitService.getShowEpisodes(id)
                _showEpisodesStatus.value = ShowEpisodesStatus.DONE
            } catch (e: java.lang.Exception) {
                _showEpisodesStatus.value = ShowEpisodesStatus.ERROR
                _showEpisodes.value = null
                Log.d("ret_err", e.toString())
            }
        }
    }
}