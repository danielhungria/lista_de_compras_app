package br.com.cadealista.listinha.di

import android.content.Context
import androidx.room.Room
import br.com.cadealista.listinha.data.ItemDatabase
import br.com.cadealista.listinha.data.ScreenListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ItemDatabase::class.java, "item.database")
            .build()

    @Singleton
    @Provides
    fun provideDatabaseScreenList(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ScreenListDatabase::class.java, "screen_list.database")
        .build()

    @Provides
    fun providesItemDao(
        db: ItemDatabase
    ) = db.getItemDao()

    @Provides
    fun providesScreenListDao(
        db1: ScreenListDatabase
    ) = db1.getScreenListDao()

}