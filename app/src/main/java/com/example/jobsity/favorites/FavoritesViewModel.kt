package com.example.jobsity.favorites

import androidx.lifecycle.*
import com.example.jobsity.dataclasses.Favorites
import com.example.jobsity.dataclasses.Images
import com.example.jobsity.dataclasses.ShowIndex
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: FavoritesRepository) : ViewModel() {

    //Livedata for favorites ID
    val getIdFavorites: LiveData<List<Int>> = repository.getIdFavorites.asLiveData()

    //Livedata for all favorites
    val getAllFavorites: LiveData<List<Favorites>> = repository.getAllFavorites.asLiveData()

    //Function to get favorites names
    fun getNameFavorites(name: String): LiveData<List<Favorites>> =
        repository.getNameFavorites(name).asLiveData()


    //Update favorite status
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
            check = repository.getCountFavorites(dataset.id)

            //If 0, insert
            if (check == 0) {
                viewModelScope.launch {
                    repository.insertFavorite(favoritesData)
                }
            } else {
                //Else, remove
                viewModelScope.launch {
                    repository.deleteFavorite(favoritesData)
                }
            }
        }
    }

    //Function to transform ROOM data to API
    fun transformToShowIndex(favorites: List<Favorites>): MutableList<ShowIndex> {
        val finalData = mutableListOf<ShowIndex>()

        favorites.forEach { favorite_item ->
            val tempImages = Images(
                favorite_item.image,
                ""
            )
            val tempData = ShowIndex(
                favorite_item.id,
                favorite_item.name,
                favorite_item.genre.split(",").map { it.trim() },
                tempImages
            )
            finalData.add(
                tempData
            )
        }

        return finalData
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

//Determine the view factory
class FavoritesViewModelFactory(private val repository: FavoritesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}