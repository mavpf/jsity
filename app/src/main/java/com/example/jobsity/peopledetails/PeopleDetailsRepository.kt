package com.example.jobsity.peopledetails

import androidx.annotation.WorkerThread
import com.example.jobsity.dataclasses.Favorites
import com.example.jobsity.db.FavoritesDao
import com.example.jobsity.dataclasses.Credits
import com.example.jobsity.dataclasses.ShowDetails
import kotlinx.coroutines.flow.Flow

class PeopleDetailsApiRepository {

    suspend fun getPeopleDetails(id: Int): Credits {
        return ShowIndexApi.retrofitService.getPeopleDetails(id, "castcredits")
    }

    suspend fun getShowDetail(id: Int): ShowDetails {
        return ShowIndexApi.retrofitService.getShowDetail(id)
    }
}

class PeopleDetailsRoomRepository(private val favoritesDao: FavoritesDao) {
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