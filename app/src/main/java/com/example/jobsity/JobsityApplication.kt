package com.example.jobsity

import android.app.Application
import com.example.jobsity.db.FavoritesDatabase
import com.example.jobsity.db.FavoritesRepository

class JobsityApplication : Application() {

    val database by lazy { FavoritesDatabase.getDatabase(this) }
    val repository by lazy { FavoritesRepository(database.favoritesDao()) }

}