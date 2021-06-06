package com.example.jobsity.episode

import ShowIndexApi
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.network.ShowEpisodesDetails
import kotlinx.coroutines.launch

class EpisodeViewModel : ViewModel() {

    //ENUM for API
    enum class EpisodeDetailStatus { LOADING, ERROR, DONE }

    //Livedata from shows
    private val _episodeDetails = MutableLiveData<ShowEpisodesDetails?>()
    val episodeDetails: LiveData<ShowEpisodesDetails?> = _episodeDetails

    //ENUM field status
    private val _episodeDetailsStatus = MutableLiveData<EpisodeDetailStatus>()


    //Get episode details from API, based on id
    fun getEpisodeDetails(
        id: Int
    ) {
        viewModelScope.launch {
            EpisodeDetailStatus.LOADING
            try {
                _episodeDetails.value = ShowIndexApi.retrofitService.getEpisodeDetails(id)
                _episodeDetailsStatus.value = EpisodeDetailStatus.DONE
            } catch (e: java.lang.Exception) {
                _episodeDetailsStatus.value = EpisodeDetailStatus.ERROR
                _episodeDetails.value = null
                Log.d("ret_err", e.toString())
            }
        }
    }
}