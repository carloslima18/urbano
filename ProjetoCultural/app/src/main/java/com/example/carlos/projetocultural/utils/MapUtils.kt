package com.example.carlos.projetocultural.utils

import android.app.Activity
import android.content.Intent
import android.os.Handler
import com.example.carlos.projetocultural.Configuracao_google_maps_Activity
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.PubpesqService
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.PubuserService

/**
 * Created by carlo on 23/03/2018.
 */
class MapUtils {

    fun mapaoffline(pub:String,activity:Activity){
        var pubuser:List<Pubuser> = arrayListOf()
        var pubpesq:List<Pubpesq> = arrayListOf()
        val handle = Handler()
        Thread{
            if(pub=="user") {
                pubuser = PubuserService.getPubuser()
            }else{
                pubpesq = PubpesqService.getPubpesq()
            }

            handle.post {
                //activity.toast("sem conexão")
                val longitude: ArrayList<String> = arrayListOf()
                val latitude: ArrayList<String> = arrayListOf()
                val nome: ArrayList<String> = arrayListOf()
                val atvexercida: ArrayList<String> = arrayListOf()

                var pubuserobj = Pubuser()
                var pubpesqobj = Pubpesq()
                var i = 0
                if (pub == "user") {
                    while (i < pubuser.size) {
                        pubuserobj = pubuser.get(i)
                        var t = pubuserobj.longitude.toString()
                        longitude?.add(pubuserobj.longitude.toString())
                        latitude?.add(pubuserobj.latitude.toString())
                        nome?.add(pubuserobj.nome.toString())
                        atvexercida?.add(pubuserobj.atvexercida.toString())
                        i++
                    }
                } else {
                    while (i < pubpesq.size) {
                        pubpesqobj = pubpesq.get(i)
                        var t = pubpesqobj.longitude.toString()
                        longitude?.add(pubpesqobj.longitude.toString())
                        latitude?.add(pubpesqobj.latitude.toString())
                        nome?.add(pubpesqobj.nome.toString())
                        atvexercida?.add(pubpesqobj.atvexercida.toString())
                        i++
                    }
                }

                //pegando o nome e coordenadas dos registrados no banco de dados

                //intent para enviar para o MAPPUB para abrir a tela do mapa
                val intent = Intent(activity.baseContext, Configuracao_google_maps_Activity::class.java)
                intent.putExtra("mostrar", "todosLocais")
                intent.putExtra("longitude", longitude?.toTypedArray())
                intent.putExtra("latitude", latitude?.toTypedArray())
                intent.putExtra("nome", nome?.toTypedArray())
                intent.putExtra("atvex", atvexercida?.toTypedArray())
                intent.putExtra("semnet", "sim")
                activity.startActivity(intent)
            }
        }.start()

    }
}