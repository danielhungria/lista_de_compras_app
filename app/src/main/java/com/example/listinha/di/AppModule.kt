package com.example.listinha.di

import android.content.Context
import androidx.room.Room
import com.example.listinha.data.ItemDatabase
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

    @Provides
    fun providesItemDao(
        db: ItemDatabase
    ) = db.getItemDao()

}