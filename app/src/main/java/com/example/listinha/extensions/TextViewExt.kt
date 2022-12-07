package com.example.listinha.extensions

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView

fun TextView.showStrikeThrough(show: Boolean) {
    paintFlags =
        if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

fun TextView.isItalic(value: Boolean) {
    setTypeface(null, if(value) Typeface.ITALIC else Typeface.NORMAL)
}