package com.example.carlos.projetocultural.utils
import android.app.Activity
import android.os.Looper
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.carlos.projetocultural.HomeActivity
import com.example.carlos.projetocultural.extensions.toast
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

//classe para tratar webService/requisições, envios etc...
object HttpHelper{
    private val TAG = "http"
    private val LOG_ON = true
    val JSON = MediaType.parse("application/json; charset=utf-8")
    var client = OkHttpClient()
    var build = OkHttpClient.Builder().connectTimeout(10000,TimeUnit.SECONDS).writeTimeout(10000,TimeUnit.SECONDS).readTimeout(10000,TimeUnit.SECONDS).build()


    //GET
    fun get(url:String):String{
      //  log("HttpHelper.get: $url")
        val request = Request.Builder().url(url).get().build()
        return getJson(request)
    }

    //POST com JSON
    fun post(url: String,json:String):String{
        log("HttpHelper.post: $url > $json")
        val body = RequestBody.create(JSON,json)
        val request = Request.Builder().url(url).post(body).build()
        return getJson(request)
    }


    //Lê a resposta do servidor no formato JSON
    private fun getJson(request: Request):String{
       var strcatcherroraaaa:String ?=null ;
        try {
            client = build
            val response = client.newCall(request).execute()
            val responseBody = response.body()
            if (responseBody != null) {
                val json = responseBody.string()
                //log("  <<: $json")
                return json
            }
         //   strcatcherror="";
            throw IOException("erro ao fazer a requisição")
        }catch (e:IOException){
           // Looper.prepare();
         //   strcatcherror=e.message;
           // print("error")
           // Looper.loop();
            return ""
        }
    }

    private fun log(s: String){
        if(LOG_ON){
            Log.d(TAG,s)
        }
    }

}