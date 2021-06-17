package com.example.jobsity

import android.app.Application
import com.example.jobsity.db.FavoritesDatabase
import com.example.jobsity.db.FavoritesRepository
import com.example.jobsity.index.IndexRoomRepository

//Needed for ROOM

class JobsityApplication : Application() {

    val database by lazy { FavoritesDatabase.getDatabase(this) }
    val repository by lazy { FavoritesRepository(database.favoritesDao()) }

    val indexRepository by lazy { IndexRoomRepository(database.favoritesDao()) }

}