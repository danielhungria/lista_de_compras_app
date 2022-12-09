package com.example.listinha.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "item_table")
@Parcelize
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String = "",
    val quantity: String ="",
    val price: String = "",
    val completed: Boolean = false,
    val idList: Int?
): Parcelable{
    val totalPrice: Double?
    get() {
        val itemQuantity = quantity.toDoubleOrNull() ?: 1.00
        val priceItem = price.toDoubleOrNull()
        return if (priceItem != null) {
            priceItem * itemQuantity
        }else{
            null
        }
    }
}
