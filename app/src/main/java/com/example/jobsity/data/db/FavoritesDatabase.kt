package com.example.jobsity.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jobsity.data.classes.Favorites

//Regular database creation for Room.
@Database(
    entities = [Favorites::class],
    version = 1,
    exportSchema = false
)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

}