package com.example.listinha.data

import androidx.room.*
import com.example.listinha.models.ScreenList
import kotlinx.coroutines.flow.Flow

@Dao
interface ScreenListDao {

    @Query("SELECT * FROM screen_list_table")
    fun getAll(): Flow<List<ScreenList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(screenList: ScreenList)

    @Update
    suspend fun update(screenList: ScreenList)

    @Delete
    suspend fun delete(screenList: ScreenList)

}