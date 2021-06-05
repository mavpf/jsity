package com.example.jobsity.db

import android.util.Log
import androidx.lifecycle.*
import com.example.jobsity.network.Images
import com.example.jobsity.network.ShowIndex
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: FavoritesRepository) : ViewModel() {

    val getIdFavorites: LiveData<List<Int>> = repository.getIdFavorites.asLiveData()

    val getAllFavorites: LiveData<List<Favorites>> = repository.getAllFavorites.asLiveData()

    fun getNameFavorites(name: String): LiveData<List<Favorites>> =
        repository.getNameFavorites(name).asLiveData()

    fun updateFavorite(dataset: ShowIndex) {
        Log.d("ret", dataset.toString())
        var check: Int
        val favoritesData = transformToFavorites(dataset)
        viewModelScope.launch {
            check = repository.getCountFavorites(dataset.id)
            Log.d("ret_1", check.toString())
            Log.d("ret_1.1", favoritesData.toString())
            if (check == 0) {
                viewModelScope.launch {
                    repository.insertFavorite(favoritesData)
                }
            } else {
                viewModelScope.launch {
                    repository.deleteFavorite(favoritesData)
                }
            }
        }
    }

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