package com.example.jobsity.ui.index

import androidx.annotation.WorkerThread
import com.example.jobsity.data.classes.Favorites
import com.example.jobsity.data.db.FavoritesDao
import com.example.jobsity.data.classes.ShowIndex
import com.example.jobsity.data.classes.ShowNames
import com.example.jobsity.network.api.ServiceApiHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class IndexRoomRepository @Inject constructor(
    private val favoritesDao: FavoritesDao
    ) {
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

class IndexApiRepository @Inject constructor(
    private val serviceApiHelper: ServiceApiHelper
) {
    suspend fun getIndex(page: Int) : List<ShowIndex>{
        return serviceApiHelper.getIndex(page)
    }

    suspend fun getShowNames(show: String): List<ShowNames>{
        return serviceApiHelper.getShowNames(show)
    }
}
