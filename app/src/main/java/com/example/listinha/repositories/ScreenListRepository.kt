package com.example.listinha.repositories

import com.example.listinha.data.ScreenListDao
import com.example.listinha.models.ScreenList
import javax.inject.Inject

class ScreenListRepository @Inject constructor(private val screenListDao: ScreenListDao) {

    fun getAll() = screenListDao.getAll()

    suspend fun insert(screenList: ScreenList){
        screenListDao.insert(screenList)
    }

    suspend fun update(screenList: ScreenList){
        screenListDao.update(screenList)
    }

    suspend fun delete(screenList: ScreenList){
        screenListDao.delete(screenList)
    }

}