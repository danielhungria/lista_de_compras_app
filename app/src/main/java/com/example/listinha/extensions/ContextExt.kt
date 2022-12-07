package com.example.listinha.extensions

import android.content.Context
import android.widget.Toast

fun Context.toast(mensagem: String){
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}