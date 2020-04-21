package com.example.gridphotohomework.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

object WindowUtil {
    fun getScreenWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}