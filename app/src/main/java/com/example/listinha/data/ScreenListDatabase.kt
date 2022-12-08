package com.example.listinha.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listinha.models.ScreenList


@Database(
    entities = [ScreenList::class],
    version = 1
)
abstract class ScreenListDatabase(): RoomDatabase(){

    abstract fun getScreenListDao(): ScreenListDao

}