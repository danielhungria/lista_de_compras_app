package br.com.cadealista.listinha.models

data class ExportedList(
    val listItem: List<Item>,
    val screenList: List<ScreenList>
)
