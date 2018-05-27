package com.example.carlos.projetocultural.utils

import android.app.DialogFragment
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.ListviewpubpesqActivity
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.PubpesqService
import com.example.carlos.projetocultural.domain.PubuserService
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//perifica a internet
object AndroidUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}