package com.example.listinha.extensions

fun Double?.concatMoneySymbol() = this?.let {
    val priceFormatted = String.format("%.2f", it)
    "R$ $priceFormatted"
} ?: ""