package com.example.jobsity.ui.favorites

import androidx.annotation.WorkerThread
import com.example.jobsity.data.classes.Favorites
import com.example.jobsity.data.db.FavoritesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val provideFavoritesDao: FavoritesDao
    ) {

    //Get all favorites
    val getAllFavorites: Flow<List<Favorites>> = provideFavoritesDao.getAllFavorites()

    //Get favorites by name (Search)
    fun getNameFavorites(name: String): Flow<List<Favorites>> =
        provideFavoritesDao.getNameFavorites(name)

    //Get all favorites ID
    val getIdFavorites: Flow<List<Int>> = provideFavoritesDao.getIdFavorites()

    //Count favorites to check if exist or not
    //Must be suspended, in worker
    @WorkerThread
    suspend fun getCountFavorites(id: Int): Int =
        provideFavoritesDao.getCountFavorites(id)

    //Insert favorite
    @WorkerThread
    suspend fun insertFavorite(favorites: Favorites) {
        provideFavoritesDao.insertFavorite(favorites)
    }

    //Delete favorite
    @WorkerThread
    suspend fun deleteFavorite(favorites: Favorites) {
        provideFavoritesDao.delete(favorites)
    }
}