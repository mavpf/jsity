package com.example.jobsity

import android.app.Application
import com.example.jobsity.db.FavoritesDatabase
import com.example.jobsity.favorites.FavoritesRepository
import com.example.jobsity.index.IndexRoomRepository
import com.example.jobsity.peopledetails.PeopleDetailsRoomRepository

//Needed for ROOM

class JobsityApplication : Application() {

    val database by lazy { FavoritesDatabase.getDatabase(this) }
    val repository by lazy { FavoritesRepository(database.favoritesDao()) }

    val indexRepository by lazy { IndexRoomRepository(database.favoritesDao()) }
    val peopleDetailsRepository by lazy { PeopleDetailsRoomRepository(database.favoritesDao()) }

}