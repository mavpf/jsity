package com.example.jobsity.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.jobsity.dataclasses.Favorites
import kotlinx.coroutines.flow.Flow


//Interface definitio for DB
@Dao
interface FavoritesDao {
    //Insert function
    @Insert
    suspend fun insertFavorite(vararg: Favorites)

    //Get all favorites, ordered by name. Flow to keep the app updated
    @Query("select * from favorites order by name")
    fun getAllFavorites(): Flow<List<Favorites>>

    //Check if a favorite is already saved
    //Not allowed to have more than one id in the table
    //So if return 0, means that the app must insert the value in the table
    //If return one, means that it should be removed
    //Cant be flow, to avoid loop, and because it's not needed.
    @Query("select COUNT(id) from favorites where id = :id")
    suspend fun getCountFavorites(vararg id: Int): Int

    //Get all favorites ID, to enable the favorite icon. Flow to keep UI updated
    @Query("select id from favorites")
    fun getIdFavorites(): Flow<List<Int>>

    //Get favorite by name, ordered by name. Flow to keep the app updated
    @Query("select * from favorites where name like :name order by name")
    fun getNameFavorites(vararg name: String): Flow<List<Favorites>>

    //Delete function
    @Delete
    suspend fun delete(vararg: Favorites)
}