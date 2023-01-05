package br.com.cadealista.listinha.models

import com.google.gson.annotations.Expose


data class ExportedList(
    @Expose
    val listItem: List<Item>,
    @Expose
    val screenList: ScreenList
)
