package br.com.cadealista.listinha.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.cadealista.listinha.models.Item

@Database(
    entities = [Item::class],
    version = 1
)
abstract class ItemDatabase: RoomDatabase() {

    abstract fun getItemDao(): ItemDao

}