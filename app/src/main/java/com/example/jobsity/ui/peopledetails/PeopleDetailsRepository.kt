package com.example.jobsity.ui.peopledetails

import androidx.annotation.WorkerThread
import com.example.jobsity.data.classes.Favorites
import com.example.jobsity.data.db.FavoritesDao
import com.example.jobsity.data.classes.Credits
import com.example.jobsity.data.classes.ShowDetails
import com.example.jobsity.network.api.ServiceApiHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PeopleDetailsApiRepository @Inject constructor(
    private val provideServiceApiHelper: ServiceApiHelper
    ){

    suspend fun getPeopleDetails(id: Int): Credits {
        return provideServiceApiHelper.getPeopleDetails(id, "castcredits")
    }

    suspend fun getShowDetail(id: Int): ShowDetails {
        return provideServiceApiHelper.getShowDetail(id)
    }
}

class PeopleDetailsRoomRepository @Inject constructor(
    private val provideFavoritesDao: FavoritesDao
    ) {

    @WorkerThread
    suspend fun insertFavorite(favorites: Favorites) {
        provideFavoritesDao.insertFavorite(favorites)
    }

    //Delete favorite
    @WorkerThread
    suspend fun deleteFavorite(favorites: Favorites) {
        provideFavoritesDao.delete(favorites)
    }

    //Get all favorites ID
    val getIdFavorites: Flow<List<Int>> = provideFavoritesDao.getIdFavorites()

    //Count favorites to check if exist or not
    //Must be suspended, in worker
    @WorkerThread
    suspend fun getCountFavorites(id: Int): Int =
        provideFavoritesDao.getCountFavorites(id)
}