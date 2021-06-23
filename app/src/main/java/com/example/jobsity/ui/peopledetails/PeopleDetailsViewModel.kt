package com.example.jobsity.ui.peopledetails

import android.util.Log
import androidx.lifecycle.*
import com.example.jobsity.data.classes.Favorites
import com.example.jobsity.data.classes.Credits
import com.example.jobsity.data.classes.ShowIndex
import com.example.jobsity.network.api.ServiceApiHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleDetailsViewModel @Inject constructor(
    private val roomRepository: PeopleDetailsRoomRepository,
    private val apiRepository: PeopleDetailsApiRepository
): ViewModel() {

    //private val apiRepository = PeopleDetailsApiRepository()

    enum class StatusPeopleDetails { LOADING, DONE, ERROR }
    enum class StatusCastDetails { LOADING, DONE, ERROR }

    private val _castDetailsData = MutableLiveData<MutableList<ShowIndex>>()
    val castDetailsData: LiveData<MutableList<ShowIndex>> = _castDetailsData

    private val _peopleDetailsData = MutableLiveData<Credits?>()
    val peopleDetailsData: LiveData<Credits?> = _peopleDetailsData

    fun getPeopleDetails(id: Int) {
        viewModelScope.launch {
            StatusPeopleDetails.LOADING
            try {
                _peopleDetailsData.value =
                    apiRepository.getPeopleDetails(id)
                StatusPeopleDetails.DONE
            } catch (e: Exception) {
                StatusPeopleDetails.ERROR
                _peopleDetailsData.value = null
            }
        }
    }

    fun getCasting(id: List<String>) {
        val tempData = mutableListOf<ShowIndex>()
        viewModelScope.launch {
            id.forEach {
                StatusCastDetails.LOADING
                try {
                    val temp = apiRepository.getShowDetail(it.toInt())
                    tempData.add(
                        ShowIndex(
                            temp.id,
                            temp.name,
                            temp.genres,
                            temp.image
                        )
                    )
                    Log.d("ret_te", temp.toString())
                    StatusCastDetails.DONE
                } catch (e: java.lang.Exception) {
                    StatusCastDetails.ERROR
                    Log.d("ret_err", e.toString())
                }
            }
            _castDetailsData.value = tempData
        }
    }

    //Livedata for favorites ID
    val getIdFavorites: LiveData<List<Int>> = roomRepository.getIdFavorites.asLiveData()

    fun updateFavorite(dataset: ShowIndex) {
        var check: Int

        //Transform data from API format to ROOM format
        //Two fields, genre and image, are lists, and ROOM doesn't handle lists.
        //Index View and recycler are working with API data format, so doesn't make sense
        // to create another view, only because of two fields.
        val favoritesData = transformToFavorites(dataset)

        viewModelScope.launch {
            //Check if a favorite is already saved
            //Not allowed to have more than one id in the table
            //So if return 0, means that the app must insert the value in the table
            //If return one, means that it should be removed
            check = roomRepository.getCountFavorites(dataset.id)

            //If 0, insert
            if (check == 0) {
                viewModelScope.launch {
                    roomRepository.insertFavorite(favoritesData)
                }
            } else {
                //Else, remove
                viewModelScope.launch {
                    roomRepository.deleteFavorite(favoritesData)
                }
            }
        }
    }

    //Function to transform API data to ROOM
    fun transformToFavorites(showIndex: ShowIndex): Favorites {

        return Favorites(
            showIndex.id,
            showIndex.name,
            showIndex.genres.joinToString(", "),
            showIndex.image?.medium.toString()
        )
    }

}