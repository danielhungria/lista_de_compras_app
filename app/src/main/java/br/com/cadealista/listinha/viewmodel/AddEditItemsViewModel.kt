package br.com.cadealista.listinha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.models.Item
import br.com.cadealista.listinha.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditItemsViewModel @Inject constructor(private val itemRepository: ItemRepository) :
    ViewModel() {

    private var itemId = 0

    private var isEditMode = false

    fun setupEditMode(itemId: Int) {
        this.itemId = itemId
        isEditMode = true
    }

    fun onSaveEvent(
        name: String,
        quantity: String,
        price: String,
        closeScreen: () -> Unit,
        idList: Int?
    ) {
        viewModelScope.launch {
            val itemToSave = Item(
                id = itemId,
                name = name,
                quantity = quantity,
                price = price,
                idList = idList
            )
            if (isEditMode) {
                itemRepository.update(itemToSave)
            } else {
                itemRepository.insert(itemToSave)
            }
            closeScreen()
        }
    }
}