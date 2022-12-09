package com.example.listinha.ui.listScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listinha.constants.Constants.SCREEN_LIST_ADAPTER
import com.example.listinha.databinding.CardviewListScreenBinding
import com.example.listinha.models.ScreenList
import com.example.listinha.ui.items.AddEditItemsFragment
import com.example.listinha.ui.items.ItemFragment


class ScreenListAdapter(
    val onClick:(ScreenList) -> Unit
): ListAdapter<ScreenList, ScreenListAdapter.ListViewHolder>(DiffCallback()) {

    private var fullList = mutableListOf<ScreenList>()

    fun updateList(listScreenList: List<ScreenList>) {
        fullList = listScreenList.toMutableList()
        submitList(fullList)
    }

    inner class ListViewHolder(
        private val binding: CardviewListScreenBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(screenList: ScreenList) {
            binding.apply {
                root.setOnClickListener {
                    onClick(screenList)
                }
                textViewNameCardviewListScreen.text = screenList.name
                textViewDescriptionCardviewListScreen.text = screenList.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenListAdapter.ListViewHolder {
        val binding = CardviewListScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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