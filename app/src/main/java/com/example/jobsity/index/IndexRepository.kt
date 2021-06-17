package com.example.jobsity.index

import androidx.annotation.WorkerThread
import com.example.jobsity.db.Favorites
import com.example.jobsity.db.FavoritesDao
import com.example.jobsity.network.ShowIndex
import com.example.jobsity.network.ShowNames
import kotlinx.coroutines.flow.Flow


class IndexRoomRepository(private val favoritesDao: FavoritesDao) {
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

    //Get all favorites ID
    val getIdFavorites: Flow<List<Int>> = favoritesDao.getIdFavorites()

    //Count favorites to check if exist or not
    //Must be suspended, in worker
    @WorkerThread
    suspend fun getCountFavorites(id: Int): Int =
        favoritesDao.getCountFavorites(id)

}

class IndexApiRepository(){
    suspend fun getIndex(page: Int) : List<ShowIndex>{
        return ShowIndexApi.retrofitService.getIndex(page)
    }

    suspend fun getShowNames(show: String): List<ShowNames>{
        return ShowIndexApi.retrofitService.getShowNames(show)
    }
}
