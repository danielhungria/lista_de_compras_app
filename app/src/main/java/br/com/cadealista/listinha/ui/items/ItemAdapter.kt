package br.com.cadealista.listinha.ui.items

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.cadealista.listinha.R
import br.com.cadealista.listinha.databinding.ItemListBinding
import br.com.cadealista.listinha.extensions.formataParaMoedaBrasileira
import br.com.cadealista.listinha.extensions.isItalic
import br.com.cadealista.listinha.extensions.setCheckedSilent
import br.com.cadealista.listinha.extensions.showStrikeThrough
import br.com.cadealista.listinha.models.Item

class ItemAdapter(
    val onComplete: (Boolean, Item) -> Unit,
    val onClick:(Item) -> Unit,
    val onClickDelete:(Item) -> Unit
) : ListAdapter<Item, ItemAdapter.ItemViewHolder>(DiffCallback()), Filterable {

    private val customFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Item>()

            if (query == null || query.toString().isEmpty()) {
                filteredList.addAll(fullList)
            }else if (query == "showCompleted"){
                filteredList.addAll(filterListByCompleted())
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

    fun updateList(listItem: List<Item>) {
        fullList = listItem.toMutableList()
        submitList(fullList)
    }

    private fun filterListBy(query: CharSequence) = fullList.filter {
        it.name.lowercase().trim().contains(query.toString().lowercase().trim())
    }

    private fun filterListByCompleted() = fullList.filter {
        it.completed
    }

    private fun showMenu(
        context: Context,
        view: View,
        menuPopupMenu: Int,
        item: Item
    ) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(menuPopupMenu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_popup_menu_item -> {
                    onClick(item)
                    true
                }
                R.id.delete_popup_menu_item -> {
                    onClickDelete(item)
                    true
                }
                else -> false
            }
        }
        popup.show()
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
                root.setOnClickListener {
                    onClick(item)
                }
                root.setOnLongClickListener {
                    showMenu(it.context, it , R.menu.menu_popup_item, item)
                    true
                }
                textViewPrice.text = item.totalPrice.formataParaMoedaBrasileira()
                textViewName.text = item.name
                textViewName.showStrikeThrough(item.completed)
                textViewName.isItalic(item.completed)
                textViewQuantity.text = item.textViewQuantity
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