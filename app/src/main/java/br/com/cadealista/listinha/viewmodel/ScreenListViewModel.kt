package br.com.cadealista.listinha.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.models.ScreenList
import br.com.cadealista.listinha.repositories.ScreenListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenListViewModel @Inject constructor(private val screenListRepository: ScreenListRepository): ViewModel() {

    private val _screenLists = MutableLiveData<List<ScreenList>>()
    val screenLists: LiveData<List<ScreenList>>
        get() = _screenLists


    fun fetchScreenList() {
        viewModelScope.launch {
            screenListRepository.getAll().collect{
                _screenLists.postValue(it)
            }
        }
    }

    fun delete(screenList: ScreenList) {
        viewModelScope.launch{
            screenListRepository.delete(screenList)
        }
    }

    fun onItemSwiped(screenList: ScreenList) = viewModelScope.launch {
        screenListRepository.delete(screenList)
    }

}