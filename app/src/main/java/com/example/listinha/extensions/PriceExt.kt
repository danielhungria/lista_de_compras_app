package com.example.listinha.extensions

import android.util.Log
import java.text.NumberFormat
import java.util.*

fun Double?.concatMoneySymbol() = this?.let {
    val priceFormatted = String.format("%.2f", it)
    "R$ $priceFormatted"
} ?: ""


fun Double?.formataParaMoedaBrasileira(): String =
     try {
        val formatador: NumberFormat = NumberFormat
            .getCurrencyInstance(Locale.getDefault())
        formatador.format(this)
    } catch (e: Exception) {
        Log.e("PriceExt", "formataParaMoedaBrasileira: $e")
        ""
    }
