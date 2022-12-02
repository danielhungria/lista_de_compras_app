package com.example.listinha.ui.items

import com.example.listinha.models.Item

interface ItemAdapterCallbacks {

    fun onComplete(completed: Boolean, item: Item)

}