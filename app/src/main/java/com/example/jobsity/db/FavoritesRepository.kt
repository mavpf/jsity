package com.example.jobsity.db

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    val getAllFavorites: Flow<List<Favorites>> = favoritesDao.getAllFavorites()

    fun getNameFavorites(name: String): Flow<List<Favorites>> =
        favoritesDao.getNameFavorites(name)

    val getIdFavorites: Flow<List<Int>> = favoritesDao.getIdFavorites()

    @WorkerThread
    suspend fun getCountFavorites(id: Int): Int =
        favoritesDao.getCountFavorites(id)

    @WorkerThread
    suspend fun insertFavorite(favorites: Favorites) {
        favoritesDao.insertFavorite(favorites)
    }

    @WorkerThread
    suspend fun deleteFavorite(favorites: Favorites) {
        favoritesDao.delete(favorites)
    }
}