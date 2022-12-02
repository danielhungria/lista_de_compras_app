package com.example.listinha.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listinha.models.Item
import com.example.listinha.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    fun fetchItemList() {
        viewModelScope.launch {
            itemRepository.getAll().collect {
                _items.postValue(it)
            }
        }
    }

    fun onComplete(boolean: Boolean, item: Item) {
        viewModelScope.launch {
            itemRepository.update(item.copy(completed = boolean))
            fetchItemList()
        }
    }

    fun onItemSwiped(item: Item) = viewModelScope.launch {
        itemRepository.delete(item)
    }

}