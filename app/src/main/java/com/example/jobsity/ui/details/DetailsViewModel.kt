package com.example.jobsity.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.data.classes.ShowDetails
import com.example.jobsity.data.classes.ShowEpisodes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DetailsRepository
) : ViewModel() {

    //Repository
    //private val repository = DetailsRepository()

    //Enum for API
    enum class ShowDetailStatus { LOADING, ERROR, DONE }
    enum class ShowEpisodesStatus { LOADING, ERROR, DONE }

    //Livedata from Shows
    private val _showDetails = MutableLiveData<ShowDetails?>()
    val showDetails: LiveData<ShowDetails?> = _showDetails

    //Livedata from episodes and seasons
    private val _showEpisodes = MutableLiveData<List<ShowEpisodes>?>()
    val showEpisodes: LiveData<List<ShowEpisodes>?> = _showEpisodes

    //List of episodes per season.
    private val _filteredEpisodes: MutableList<ShowEpisodes> = mutableListOf()

    //ENUM status
    private val _showDetailStatus = MutableLiveData<ShowDetailStatus>()
    private val _showEpisodesStatus = MutableLiveData<ShowEpisodesStatus>()

    //Setter spinner data selection
    fun addEpisodesPerSeason(data: ShowEpisodes) {
        _filteredEpisodes.add(
            data
        )
    }

    //Clear episodes for each spinner data change
    fun clearEpisodesPerSeason() {
        _filteredEpisodes.clear()
    }

    //Getter of episodes
    fun getEpisodesPerSeason(): List<ShowEpisodes> {
        return _filteredEpisodes
    }

    //Get show details from API
    fun getShowDetails(
        id: Int
    ) {
        viewModelScope.launch {
            ShowDetailStatus.LOADING
            try {
                _showDetails.value = repository.getShowDetail(id)
                _showDetailStatus.value = ShowDetailStatus.DONE
            } catch (e: java.lang.Exception) {
                _showDetailStatus.value = ShowDetailStatus.ERROR
                _showDetails.value = null
                Log.d("ret_err", e.toString())
            }
        }
    }

    //Get episodes and seasons from API
    fun getShowEpisodes(
        id: Int
    ) {
        viewModelScope.launch {
            ShowEpisodesStatus.LOADING
            try {
                _showEpisodes.value = repository.getShowEpisodes(id)
                _showEpisodesStatus.value = ShowEpisodesStatus.DONE
            } catch (e: java.lang.Exception) {
                _showEpisodesStatus.value = ShowEpisodesStatus.ERROR
                _showEpisodes.value = null
                Log.d("ret_err", e.toString())
            }
        }
    }
}