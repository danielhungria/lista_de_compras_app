package com.example.listinha.ui.items

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listinha.databinding.ItemListBinding
import com.example.listinha.extensions.isItalic
import com.example.listinha.extensions.setCheckedSilent
import com.example.listinha.extensions.showStrikeThrough
import com.example.listinha.models.Item
import com.example.listinha.models.Prices

class ItemAdapter(
    val onComplete: (Boolean, Item) -> Unit
) : ListAdapter<Item, ItemAdapter.ItemViewHolder>(DiffCallback()), Filterable {

    private val customFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Item>()
            if (query == null || query.toString().isEmpty()) {
                filteredList.addAll(fullList)
            } else {
                filteredList.addAll(filterListBy(query))
            }

            return FilterResults().apply {
                values = filteredList
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.values?.let { submitList(it as List<Item>) }
        }

    }

    private var fullList = mutableListOf<Item>()

    var listPrices = mutableListOf<Prices>()

    fun updateList(listItem: List<Item>) {
        fullList = listItem.toMutableList()
        submitList(fullList)
    }

    private fun filterListBy(query: CharSequence) = fullList.filter {
        it.name.lowercase().trim().contains(query.toString().lowercase().trim())
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        return holder.bind(currentItem)
    }

    inner class ItemViewHolder(
        private val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.apply {
                val checkBoxListener = CompoundButton.OnCheckedChangeListener { view, isChecked ->
                    view?.let { onComplete(isChecked, item) }
                }
                val itemQuantity = item.quantity.toDoubleOrNull()
                val priceItem = item.price.toDoubleOrNull()
                if (priceItem != null && itemQuantity != null) {
                    val finalPrice = priceItem * itemQuantity
                    val finalPriceFormat = String.format("%.2f", finalPrice)
                    textViewPrice.text = "R$ ${finalPriceFormat}"
                    val FinalPrice = Prices(finalPrice)
                    listPrices.add(FinalPrice)
                }
                textViewName.text = item.name
                textViewName.showStrikeThrough(item.completed)
                textViewName.isItalic(item.completed)
                textViewQuantity.text = item.quantity
                checkBox.setCheckedSilent(item.completed, checkBoxListener)
                checkBox.setOnCheckedChangeListener(checkBoxListener)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item) =
            oldItem == newItem
    }

}