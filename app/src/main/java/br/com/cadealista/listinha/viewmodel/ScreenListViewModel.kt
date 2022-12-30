package br.com.cadealista.listinha.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.constants.Constants
import br.com.cadealista.listinha.models.ExportedList
import br.com.cadealista.listinha.models.ScreenList
import br.com.cadealista.listinha.repositories.ItemRepository
import br.com.cadealista.listinha.repositories.ScreenListRepository
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
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

    fun exportData(
        id: Int,
        onSuccess: (exportedFile: File) -> Unit,
        onError: () -> Unit,
        context: Context
    ) {

        viewModelScope.launch {
            try {
                val screenList = _screenLists.value?.firstOrNull { it.id == id }
                itemRepository.getAllItemsOfList(id).collect { itemList ->
                    screenList?.let { screenList ->
                        val exportData = ExportedList(itemList, screenList)
                        val gson = GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create()
                        _exportedData.postValue(gson.toJson(exportData))
                        createFile(exportData, onSuccess = { onSuccess(it) }, context)
                    }
                }
            } catch (e: Exception) {
                onError()
                Log.e("ScreenListViewModel", e.toString())
            }
        }

    }

//    fun insertExampleList() {
//        viewModelScope.launch {
//            //um problema fazer desse jeito, pois a pessoa pode ter uma lista com esse ID, Nao da pra atualizar infos tb
//            val screenList1 = ScreenList(id = 1, name = "Mercado", description = "Compras do mês")
//            val screenList2 =
//                ScreenList(id = 2, name = "Aniversário", description = "Festa do Alex")
//            val screenList3 =
//                ScreenList(id = 3, name = "Churras", description = "Churrasco do domingão")
//            screenListRepository.insert(screenList1)
//            screenListRepository.insert(screenList2)
//            screenListRepository.insert(screenList3)
//        }
//    }

    private fun createFile(
        exportedList: ExportedList,
        onSuccess: (exportedFile: File) -> Unit,
        context: Context
    ) {
        val directory = File(
            context.filesDir, "lists"
        )
        if (!directory.exists()) directory.mkdirs()

        val exportFile =
            File(directory, exportedList.screenList.name + Constants.EXPORT_DATA_FILE_EXTENSION)

        exportFile.createNewFile()
        val writer = FileWriter(exportFile, true)
        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
        writer.append(gson.toJson(exportedList))
        writer.flush()
        writer.close()
        onSuccess(exportFile)
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

}

