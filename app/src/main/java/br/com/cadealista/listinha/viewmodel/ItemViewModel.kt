package br.com.cadealista.listinha.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.extensions.formataParaMoedaBrasileira
import br.com.cadealista.listinha.models.Item
import br.com.cadealista.listinha.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private var listId: Int = 0

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    fun setup(listId: Int){
        this.listId = listId
    }

    fun fetchItemList() {
        viewModelScope.launch {
            itemRepository.getAllItemsOfList(listId).collect {
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

    fun getTotalMarketPrice() = _items.value?.sumOf {
        it.totalPrice ?: 0.00
    }.formataParaMoedaBrasileira()

    fun deleteAllItemsCompleted() {
        viewModelScope.launch {
            itemRepository.deleteCompletedItem()
        }
    }

    fun delete(item: Item) {
        viewModelScope.launch{
            itemRepository.delete(item)
        }
    }

    fun checkList(): Boolean {
        return _items.value?.isEmpty() == true
    }
}