package com.example.listinha.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listinha.models.Item

@Database(
    entities = [Item::class],
    version = 1
)
abstract class ItemDatabase: RoomDatabase() {

    abstract fun getItemDao(): ItemDao

}