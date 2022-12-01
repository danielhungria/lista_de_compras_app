package com.example.listinha.repositories

import com.example.listinha.data.ItemDao
import com.example.listinha.models.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepository @Inject constructor (private val itemDao: ItemDao) {

    fun getAll() = itemDao.getAll()

    suspend fun insert(item: Item){
        itemDao.insert(item)
    }

    suspend fun update(item: Item){
        itemDao.update(item)
    }

    suspend fun delete(item: Item){
        itemDao.delete(item)
    }


}