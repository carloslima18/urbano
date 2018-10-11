package com.example.carlos.projetocultural.utils

import android.content.Context
import android.net.ConnectivityManager

//perifica a internet
object AndroidUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}