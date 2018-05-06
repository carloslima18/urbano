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
import org.json.JSONObject
import org.json.JSONArray





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

    fun get2(url:String):String{ // repeti isso pois é para pegar somente elemento unico, logo n vem os metadados e a estrutura vem diferente.
        //dps vou arrumar isso.
        //  log("HttpHelper.get: $url")
        val request = Request.Builder().url(url).get().build()
        return getJson2(request)
    }


    //POST com JSON
    fun post(url: String,json:String):String{
        log("HttpHelper.post: $url > $json")
        val body = RequestBody.create(JSON,json)
        val request = Request.Builder().url(url).post(body).build()
        return getJson2(request)
    }


    //Lê a resposta do servidor no formato JSON
    fun getJson(request: Request):String{
       var strcatcherroraaaa:String ?=null ;
        try {
            client = build
            val response = client.newCall(request).execute()

            val responseBody = response.body()
            if (responseBody != null) {
                val json = responseBody.string()
                val Jobject = JSONObject(json)
                val Jarray = Jobject.getJSONArray("items").toString()
                //log("  <<: $json")
                return Jarray
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

    //Lê a resposta do servidor no formato JSON
    fun getJson2(request: Request):String{
        var strcatcherroraaaa:String ?=null ;
        try {
            client = build
            val response = client.newCall(request).execute()

            val responseBody = response.body()
            if (responseBody != null) {
                val json = responseBody.string()
        //        val Jobject = JSONObject(json)
        //        val Jarray = Jobject.getJSONArray("items").toString()
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