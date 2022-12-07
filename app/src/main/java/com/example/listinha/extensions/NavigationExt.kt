package com.example.listinha.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigateTo(@IdRes destination: Int, args: Bundle? = null) {
    findNavController().navigate(destination, args)
}