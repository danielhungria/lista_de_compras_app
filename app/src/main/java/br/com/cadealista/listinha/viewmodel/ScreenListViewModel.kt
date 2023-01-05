package br.com.cadealista.listinha.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.constants.Constants
import br.com.cadealista.listinha.extensions.toast
import br.com.cadealista.listinha.models.ExportedList
import br.com.cadealista.listinha.models.Item
import br.com.cadealista.listinha.models.ScreenList
import br.com.cadealista.listinha.repositories.ItemRepository
import br.com.cadealista.listinha.repositories.ScreenListRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.StringReader
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class ScreenListViewModel @Inject constructor(
    private val screenListRepository: ScreenListRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _screenLists = MutableLiveData<List<ScreenList>>()
    val screenLists: LiveData<List<ScreenList>>
        get() = _screenLists

    private val _exportedData = MutableLiveData<String>()
    val exportedData: LiveData<String>
        get() = _exportedData

    fun fetchScreenList() {
        viewModelScope.launch {
            screenListRepository.getAll().collect {
                _screenLists.postValue(it)
            }
        }
    }

    fun delete(screenList: ScreenList) {
        viewModelScope.launch {
            screenListRepository.delete(screenList)
        }
    }

    fun onItemSwiped(screenList: ScreenList) = viewModelScope.launch {
        screenListRepository.delete(screenList)
    }

<<<<<<< HEAD
//    fun exportData(id: Int){
//        viewModelScope.launch {
//            val list = mutableListOf<Item>()
//            Log.i("Fragment", "mutablelista: $list")
//            itemRepository.getAllItemsOfList(id).collect{
//                list.addAll(it)
//                Log.i("Fragment", "getall: $it")
//
//            }
//            val screenList = screenLists.value?.filter {
//                Log.i("Fragment", "screenlist: $it")
//                it.id == id
//            }
//            val exportedList = screenList?.let {
//                ExportedList(
//                    list,
//                    it
//                )
//            }
//            _exportedData.postValue(Gson().toJson(exportedList))
//            Log.i("Fragment", "exportData: $exportedList")
//        }
//
//    }
=======
    fun exportData(
        id: Int,
        onSuccess: (exportedFile: File) -> Unit,
        onError: () -> Unit,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val screenList = _screenLists.value?.firstOrNull { it.id == id }
                launch(Dispatchers.IO) {
                    val listItem = itemRepository.getAllItemsOfListWithoutFlow(id)
                    screenList?.let { screenList ->
                        val exportData = ExportedList(listItem, screenList)
                        createFile(exportData, onSuccess = { onSuccess(it) }, onError = { onError()}, context)
                    }
                }
            } catch (e: Exception) {
                onError()
                Log.e("ScreenListViewModel", e.toString())
            }
        }

    }
>>>>>>> feature/shareData

    private fun createFile(
        exportedList: ExportedList,
        onSuccess: (exportedFile: File) -> Unit,
        onError: () -> Unit,
        context: Context
    ) {
        val directory = File(
            context.filesDir, "lists"
        )
        if (!directory.exists()) directory.mkdirs()

        val exportFile =
            File(directory, exportedList.screenList.name + Constants.EXPORT_DATA_FILE_EXTENSION)

        if (!exportFile.exists()){
            exportFile.createNewFile()
            val writer = FileWriter(exportFile, true)
            val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
            writer.append(gson.toJson(exportedList))
            writer.flush()
            writer.close()
            onSuccess(exportFile)
        }else{
            onError()
        }

    }

    fun importData(bytesArray: ByteArray, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val stringOfBytesArray = String(bytesArray, StandardCharsets.UTF_8)
                val readText = StringReader(stringOfBytesArray).readText()
                val json = JSONObject(readText).toString()
                val fromJson = Gson().fromJson(json, ExportedList::class.java)

                screenListRepository.insert(fromJson.screenList)?.let { screenListId ->
                    fromJson.listItem.forEach {
                        itemRepository.insert(it.copy(idList = screenListId.toInt()))
                    }
                }
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun checkList(): Boolean {
        return _screenLists.value?.isEmpty() == true
    }
}

