package com.example.listinha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listinha.models.Item
import com.example.listinha.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditItemsViewModel @Inject constructor(private val itemRepository: ItemRepository) :
    ViewModel() {

    fun onSaveEvent(
        name: String,
        quantity: String,
        price: String,
        closeScreen:() -> Unit
    ) {
        viewModelScope.launch {
            itemRepository.insert(Item(
                name = name,
                quantity = quantity,
                price = price
            ))
            closeScreen()
        }
    }

    fun onSaveEventEdit(
        id: Int,
        name: String,
        quantity: String,
        price: String,
        closeScreen:() -> Unit,
    ) {
        viewModelScope.launch {
            itemRepository.update(Item(
                name = name,
                quantity = quantity,
                price = price,
                id = id
            ))
            closeScreen()
        }
    }
}