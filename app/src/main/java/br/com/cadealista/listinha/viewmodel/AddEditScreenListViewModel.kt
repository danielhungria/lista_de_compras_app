package br.com.cadealista.listinha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.models.ScreenList
import br.com.cadealista.listinha.repositories.ScreenListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditScreenListViewModel @Inject constructor(private val screenListRepository: ScreenListRepository): ViewModel(){


    private var screenListId = 0

    private var isEditMode = false

    fun setupEditMode(screenListId: Int) {
        this.screenListId = screenListId
        isEditMode = true
    }

    fun onSaveEvent(
        name: String,
        description: String,
        closeScreen: () -> Unit
    ) {
        viewModelScope.launch {
            val screeListToSave = ScreenList(
                id = screenListId,
                name = name,
                description = description
            )
            if (isEditMode) {
                screenListRepository.update(screeListToSave)
            } else {
                screenListRepository.insert(screeListToSave)
            }
            closeScreen()
        }
    }

}