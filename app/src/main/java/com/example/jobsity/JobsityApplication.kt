package com.example.jobsity

import android.app.Application
import com.example.jobsity.data.db.FavoritesDatabase
import com.example.jobsity.ui.favorites.FavoritesRepository
import com.example.jobsity.ui.index.IndexRoomRepository
import com.example.jobsity.ui.peopledetails.PeopleDetailsRoomRepository
import dagger.hilt.android.HiltAndroidApp

//Needed for ROOM

@HiltAndroidApp
class JobsityApplication : Application() {

    val database by lazy { FavoritesDatabase.getDatabase(this) }
    val repository by lazy { FavoritesRepository(database.favoritesDao()) }

    val indexRepository by lazy { IndexRoomRepository(database.favoritesDao()) }
    val peopleDetailsRepository by lazy { PeopleDetailsRoomRepository(database.favoritesDao()) }

}