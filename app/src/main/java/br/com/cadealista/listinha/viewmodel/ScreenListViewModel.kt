package br.com.cadealista.listinha.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.models.ExportedList
import br.com.cadealista.listinha.models.Item
import br.com.cadealista.listinha.models.ScreenList
import br.com.cadealista.listinha.repositories.ItemRepository
import br.com.cadealista.listinha.repositories.ScreenListRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenListViewModel @Inject constructor(
    private val screenListRepository: ScreenListRepository,
    private val itemRepository: ItemRepository
): ViewModel() {

    private val _screenLists = MutableLiveData<List<ScreenList>>()
    val screenLists: LiveData<List<ScreenList>>
        get() = _screenLists

    private val _exportedData = MutableLiveData<String>()
    val exportedData: LiveData<String>
        get() = _exportedData


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

    fun exportData(id: Int){
        viewModelScope.launch {
            val list = mutableListOf<Item>()
            Log.i("Fragment", "mutablelista: $list")
            itemRepository.getAllItemsOfList(id).collect{
                list.addAll(it)
                Log.i("Fragment", "getall: $it")

            }
            val screenList = screenLists.value?.filter {
                Log.i("Fragment", "screenlist: $it")
                it.id == id
            }
            val exportedList = screenList?.let {
                ExportedList(
                    list,
                    it
                )
            }
            _exportedData.postValue(Gson().toJson(exportedList))
            Log.i("Fragment", "exportData: $exportedList")
        }

    }

     fun insertExampleList(){
         viewModelScope.launch {
             //um problema fazer desse jeito, pois a pessoa pode ter uma lista com esse ID, Nao da pra atualizar infos tb
             val screenList1 = ScreenList(id = 1, name = "Mercado", description = "Compras do mês")
             val screenList2 = ScreenList(id = 2, name = "Aniversário", description = "Festa do Alex")
             val screenList3 = ScreenList(id = 3, name = "Churras", description = "Churrasco do domingão")
             screenListRepository.insert(screenList1)
             screenListRepository.insert(screenList2)
             screenListRepository.insert(screenList3)
         }
     }

}