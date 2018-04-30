package com.example.carlos.projetocultural.utils

import android.app.DialogFragment
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
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
        /*val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connectivity.allNetworks
        return networks
                .map { connectivity.getNetworkInfo(it) }
                .any { it.state == NetworkInfo.State.CONNECTED };*/
        return true
    }




    fun savePubpesq(pubpesq: Pubpesq?){


        doAsync {
            val tes = PubpesqService.salvar(pubpesq)
            //val tes2 = PubuserService.getPubuser()

            uiThread {

                // Alerta de sucesso
                // Dispara um evento para atualizar a lista
       //         mainact.taskCarros()
            }
        }



      /*  databaseOpenHelper?.use{
            insert("publicacaop",
                    "nome" to nome,
                    "redesocial" to redesocial,
                    "endereco" to endereco,
                    "contato" to contato,
                    "email" to email,
                    "atvexercida" to atvexercida,
                    "categoria" to categoria,
                    "anoinicio" to anoinicio,
                    "cnpj" to cnpj,
                    "representacao" to representacao,
                    "recurso" to recurso,
                    "longitude" to longitude,
                    "latitude" to latitude,
                    "pesquisador" to pesquisador,
                    "img1" to base64_1,
                    "img2" to base64_2,
                    "img3" to base64_3,
                    "img4" to base64_4,
                    "campo1" to campo1,
                    "campo2" to campo2,
                    "campo3" to campo3,
                    "campo4" to campo4,
                    "campo5" to campo5
            )
        } */
    }


    fun updateTablepesq(pubpesq: Pubpesq
    ){

        doAsync {
            val tes = PubpesqService.updatepesq(pubpesq)

            //val tes2 = PubuserService.getPubuser()

            uiThread {

                // Alerta de sucesso
                // Dispara um evento para atualizar a lista
                //         mainact.taskCarros()
            }
        }
        //fragment.dismiss() //fecha o fragment
    }




}