package com.example.gridphotohomework.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Warren on 2017/8/31.
 */
object NetworkUtil {
    fun isNetworkAvailable(context: Context): Boolean {
        val conManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networInfo = conManager.activeNetworkInfo
        return !(networInfo == null || !networInfo.isAvailable)
    }
}