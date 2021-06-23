package com.example.jobsity.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FavoritesDatabaseModule {

    @Provides
    @Singleton
    fun provideFavoritesDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app.applicationContext,
        FavoritesDatabase::class.java,
        "favorites_database"
    ).build()

    @Provides
    @Singleton
    fun provideFavoritesDatabaseDao(db: FavoritesDatabase) = db.favoritesDao()
}