package com.example.jobsity.ui.episode

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.data.classes.ShowEpisodesDetails
import kotlinx.coroutines.launch

class EpisodeViewModel : ViewModel() {

    //Repository
    private val repository =  EpisodeRepository()

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
                _episodeDetails.value = repository.getEpisodeDetails(id)
                _episodeDetailsStatus.value = EpisodeDetailStatus.DONE
            } catch (e: java.lang.Exception) {
                _episodeDetailsStatus.value = EpisodeDetailStatus.ERROR
                _episodeDetails.value = null
                Log.d("ret_err", e.toString())
            }
        }
    }
}