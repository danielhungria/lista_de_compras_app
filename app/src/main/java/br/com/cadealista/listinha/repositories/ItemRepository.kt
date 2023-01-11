package br.com.cadealista.listinha.repositories

import br.com.cadealista.listinha.data.ItemDao
import br.com.cadealista.listinha.models.Item
import javax.inject.Inject

class ItemRepository @Inject constructor (private val itemDao: ItemDao) {

    fun getAll() = itemDao.getAll()

    fun getAllItemsOfList(idList: Int) = itemDao.getAllItemsOfList(idList)

    fun getAllItemsCompletedOfList(idList: Int, completed: Boolean) = itemDao.getAllItemsCompletedOfList(idList,completed)

    suspend fun getAllItemsOfListWithoutFlow(idList: Int) = itemDao.getAllItemsOfListWithoutFlow(idList)

    suspend fun insert(item: Item){
        itemDao.insert(item)
    }

    suspend fun insert(itemsList: List<Item>) {
        itemDao.insert(itemsList)
    }

    suspend fun update(item: Item){
        itemDao.update(item)
    }

    suspend fun delete(item: Item){
        itemDao.delete(item)
    }

    suspend fun deleteCompletedItem(){
        itemDao.deleteCompletedItem()
    }


}