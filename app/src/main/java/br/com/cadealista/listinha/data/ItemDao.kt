package br.com.cadealista.listinha.data

import androidx.room.*
import br.com.cadealista.listinha.models.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item_table")
    fun getAll(): Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE idList = :idList")
    fun getAllItemsOfList(idList: Int): Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE idList = :idList and completed = :completed")
    fun getAllItemsCompletedOfList(idList: Int, completed: Boolean): Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE idList = :idList")
    fun getAllItemsOfListWithoutFlow(idList: Int): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE FROM item_table WHERE completed = 1")
    suspend fun deleteCompletedItem()

}