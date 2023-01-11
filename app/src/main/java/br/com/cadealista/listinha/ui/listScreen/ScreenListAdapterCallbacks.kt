package br.com.cadealista.listinha.ui.listScreen

import br.com.cadealista.listinha.models.ScreenList

interface ScreenListAdapterCallbacks {

    fun onClick(screenList: ScreenList)
    fun longPress(screenList: ScreenList)
    fun longPressDelete(screenList: ScreenList)
    fun sharePress(id: Int)
    fun duplicate(screenList: ScreenList)

}