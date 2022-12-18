package br.com.cadealista.listinha.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.cadealista.listinha.models.ScreenList


@Database(
    entities = [ScreenList::class],
    version = 1
)
abstract class ScreenListDatabase(): RoomDatabase(){

    abstract fun getScreenListDao(): ScreenListDao

}