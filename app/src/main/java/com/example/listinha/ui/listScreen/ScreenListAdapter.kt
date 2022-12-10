package com.example.listinha.ui.listScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listinha.R
import com.example.listinha.databinding.CardviewListScreenBinding
import com.example.listinha.models.ScreenList


class ScreenListAdapter(
    val onClick: (ScreenList) -> Unit,
    val longPress: (ScreenList) -> Unit,
    val longPressDelete: (ScreenList) -> Unit
) : ListAdapter<ScreenList, ScreenListAdapter.ListViewHolder>(DiffCallback()) {

    private var fullList = mutableListOf<ScreenList>()

    fun updateList(listScreenList: List<ScreenList>) {
        fullList = listScreenList.toMutableList()
        submitList(fullList)
    }

    inner class ListViewHolder(
        private val binding: CardviewListScreenBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(screenList: ScreenList) {
            binding.apply {
                root.setOnClickListener {
                    onClick(screenList)
                }
                root.setOnLongClickListener {
                    showMenu(it.context,it, R.menu.menu_popup_menu, screenList)
                    true
                }
                textViewNameCardviewListScreen.text = screenList.name
                textViewDescriptionCardviewListScreen.text = screenList.description
            }
        }
    }

    private fun showMenu(
        context: Context,
        view: View,
        menuPopupMenu: Int,
        screenList: ScreenList
    ) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(menuPopupMenu, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.edit_popup_menu -> {
                    longPress(screenList)
                    true
                }
                R.id.delete_popup_menu -> {
                    longPressDelete(screenList)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScreenListAdapter.ListViewHolder {
        val binding =
            CardviewListScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenListAdapter.ListViewHolder, position: Int) {
        val currentItem = getItem(position)
        return holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<ScreenList>() {
        override fun areItemsTheSame(oldItem: ScreenList, newItem: ScreenList) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ScreenList, newItem: ScreenList) =
            oldItem == newItem
    }

}