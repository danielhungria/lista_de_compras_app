package com.example.listinha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listinha.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedViewModel @Inject constructor(
    private val itemRepository: ItemRepository
): ViewModel() {

    fun onConfirmClick() = viewModelScope.launch {
        itemRepository.deleteCompletedItem()
    }

}