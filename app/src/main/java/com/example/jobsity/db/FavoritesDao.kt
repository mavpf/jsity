package com.example.jobsity.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert
    suspend fun insertFavorite(vararg: Favorites)

    @Query("select * from favorites order by name")
    fun getAllFavorites(): Flow<List<Favorites>>

    @Query("select COUNT(id) from favorites where id = :id")
    suspend fun getCountFavorites(vararg id: Int): Int

    @Query("select id from favorites")
    fun getIdFavorites(): Flow<List<Int>>

    @Query("select * from favorites where name = :name order by name")
    fun getNameFavorites(vararg name: String): Flow<List<Favorites>>

    @Delete
    suspend fun delete(vararg: Favorites)
}