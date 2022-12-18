package br.com.cadealista.listinha.extensions

import android.widget.CheckBox
import android.widget.CompoundButton

fun CheckBox.setCheckedSilent(
    value: Boolean,
    listener: CompoundButton.OnCheckedChangeListener,
) {
    setOnCheckedChangeListener(null)
    isChecked = value
    setOnCheckedChangeListener(listener)
}