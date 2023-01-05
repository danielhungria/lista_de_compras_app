package br.com.cadealista.listinha.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.cadealista.listinha.extensions.formataParaMoedaBrasileira
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Entity(tableName = "item_table")
@Parcelize
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @Expose
    val name: String = "",
    @Expose
    val quantity: String ="",
    @Expose
    val price: String = "",
    @Expose
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
    val priceFormat: Double?
        get() {
            return price.toDoubleOrNull()
        }

    val textViewQuantity: String
    get(){
        return if (quantity.isNotBlank() && price.isNotBlank()){
            "$quantity X ${price.toDouble().formataParaMoedaBrasileira()}"
        }else if(quantity.isNotBlank()){
            "$quantity und."
        }else ""
    }
}
