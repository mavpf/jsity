package com.example.jobsity.ui.index

import android.content.Context
import androidx.room.Room
import com.example.jobsity.data.db.FavoritesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FavoritesDatabaseModule {

    @Provides
    @Named("test_db")
    fun provideFavoritesDatabase(
        @ApplicationContext app: Context
    ): FavoritesDatabase = Room.inMemoryDatabaseBuilder(
        app.applicationContext,
        FavoritesDatabase::class.java
    ).build()
}