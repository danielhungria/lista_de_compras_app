package br.com.cadealista.listinha.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cadealista.listinha.constants.Constants
import br.com.cadealista.listinha.models.ExportedList
import br.com.cadealista.listinha.models.ScreenList
import br.com.cadealista.listinha.repositories.ItemRepository
import br.com.cadealista.listinha.repositories.ScreenListRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                launch(Dispatchers.IO) {
                    val listItem = itemRepository.getAllItemsOfListWithoutFlow(id)
                    screenList?.let { screenList ->
                        val exportData = ExportedList(listItem, screenList)
                        createFile(
                            exportData,
                            onSuccess = { onSuccess(it) },
                            onError = { onError() },
                            context
                        )
                    }
                }
            } catch (e: Exception) {
                onError()
                Log.e("ScreenListViewModel", e.toString())
            }
        }

    }

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

        if (!exportFile.exists()) {
            exportFile.createNewFile()
            val writer = FileWriter(exportFile, true)
            val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
            writer.append(gson.toJson(exportedList))
            writer.flush()
            writer.close()
            onSuccess(exportFile)
        } else {
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

    fun duplicateItem(screenList: ScreenList) {
        viewModelScope.launch {
            val itemsToDuplicate = itemRepository.getAllItemsOfListWithoutFlow(screenList.id)
            screenListRepository.insert(screenList.copy(id = 0))?.let { id ->
                val itemsToDuplicateWithUpdatedId =
                    itemsToDuplicate.map {
                        it.copy(id = 0, idList = id.toInt())
                    }
                itemRepository.insert(itemsToDuplicateWithUpdatedId)
            }
            fetchScreenList()
        }
    }
}

