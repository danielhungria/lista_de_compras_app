package com.example.listinha.ui.items

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listinha.databinding.ItemListBinding
import com.example.listinha.extensions.isItalic
import com.example.listinha.extensions.showStrikeThrough
import com.example.listinha.models.Item

class ItemAdapter(
    val onComplete: (Boolean, Item) -> Unit,
    val onSearchBy: (CharSequence?) -> Unit
): ListAdapter<Item, ItemAdapter.ItemViewHolder>(DiffCallback()), Filterable {

    private val customFilter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            onSearchBy(constraint)
            return FilterResults()
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            onSearchBy(constraint)
        }

    }

    private val fullList = mutableListOf<Item>()

    fun updateList(listItem: List<Item>){
        fullList.clear()
        fullList.addAll(listItem)
        submitList(fullList)
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
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Item){
            binding.apply {
                val itemQuantity = item.quantity.toDoubleOrNull()
                val priceItem = item.price.toDoubleOrNull()
                if (priceItem != null && itemQuantity != null) {
                    val finalPrice = priceItem * itemQuantity
                    textViewPrice.text = "R$ $finalPrice"
                }
                textViewName.text = item.name
                textViewName.showStrikeThrough(item.completed)
                textViewName.isItalic(item.completed)
                textViewQuantity.text = item.quantity
                checkBox.isChecked = item.completed
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    onComplete(isChecked,item)
                }
                Log.i("Adapter", "bind: $item")
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item) =
            oldItem == newItem
    }

}