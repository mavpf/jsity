package com.example.jobsity.favorites

import androidx.annotation.WorkerThread
import com.example.jobsity.dataclasses.Favorites
import com.example.jobsity.db.FavoritesDao
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    //Get all favorites
    val getAllFavorites: Flow<List<Favorites>> = favoritesDao.getAllFavorites()

    //Get favorites by name (Search)
    fun getNameFavorites(name: String): Flow<List<Favorites>> =
        favoritesDao.getNameFavorites(name)

    //Get all favorites ID
    val getIdFavorites: Flow<List<Int>> = favoritesDao.getIdFavorites()

    //Count favorites to check if exist or not
    //Must be suspended, in worker
    @WorkerThread
    suspend fun getCountFavorites(id: Int): Int =
        favoritesDao.getCountFavorites(id)

    //Insert favorite
    @WorkerThread
    suspend fun insertFavorite(favorites: Favorites) {
        favoritesDao.insertFavorite(favorites)
    }

    //Delete favorite
    @WorkerThread
    suspend fun deleteFavorite(favorites: Favorites) {
        favoritesDao.delete(favorites)
    }
}