package com.example.listinha.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.listinha.models.Item
import com.example.listinha.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
private val itemRepository: ItemRepository
):ViewModel(){

    private val fullItemList = mutableListOf<Item>()

    private var searchViewQuery: CharSequence? = ""

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    fun fetchItemList(){
        viewModelScope.launch {
            itemRepository.getAll().collect{
                _items.postValue(it)
                fullItemList.addAll(it)
            }
        }
    }

    fun onComplete(boolean: Boolean, item: Item){
        viewModelScope.launch {
            itemRepository.update(item.copy(completed = boolean))
            fetchItemList()
        }
    }

    fun onSearchBy(query: CharSequence?){
        searchViewQuery = query
        if (searchViewQuery==null||searchViewQuery.toString().isEmpty()){
            _items.postValue(fullItemList)
            Log.i("ViewModel", "onSearch: $fullItemList")
        }else{
            filterListByName()
        }
    }

    private fun filterListByName(){
        val filteredList =  fullItemList.filter {
            it.name.lowercase().trim().contains(searchViewQuery.toString().lowercase().trim())
        }
        _items.postValue(
           filteredList
        )
    }

}