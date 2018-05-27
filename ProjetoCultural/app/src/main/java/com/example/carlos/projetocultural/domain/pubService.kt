package com.example.carlos.projetocultural.domain

import android.app.Activity
import android.content.Context
import android.util.Base64
import android.widget.Toast
import com.example.carlos.projetocultural.MainActivity
import com.example.carlos.projetocultural.R.id.e
import com.example.carlos.projetocultural.extensions.fromJson
import com.example.carlos.projetocultural.extensions.toJson
import com.example.carlos.projetocultural.utils.HttpHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.toast
import org.json.JSONArray
import java.io.File
import org.json.JSONObject



object pubService{
    var ip = "192.168.15.4"
    private val BASE_URLuser = "http://orbeapp.com/web/sendpubuser?_format=json"
    private val BASE_URLpesq = "http://orbeapp.com/web/sendpubpesq?_format=json"


    // Busca os carros por tipo (clássicos, esportivos ou luxo)
    fun getPubpesq(URL:String): MutableList<Pubpesq> {
        val url = URL
        val json = HttpHelper.get(url)
        val carros = fromJson<MutableList<Pubpesq>>(json)
        return carros
    }

    fun getPesquisador(url:String): JSONObject {
        val json = HttpHelper.get(url)
        val carros = fromJson<JSONObject>(json)
        return carros
    }

    fun getPubpesqparasoUmresultado(URL:String): Pubpesq {
        val url = URL
        val json = HttpHelper.get2(url)
        val carros = fromJson<Pubpesq>(json)
        return carros
    }

    fun meta_dados_pesq():JSONObject{
        var client = OkHttpClient()
        var request = Request.Builder().url("http://orbeapp.com/web/sendpubpesq?_format=json&fields=_ meta").build();
        var responses = client.newCall(request).execute();
        var jsonData = responses.body()!!.string();
        var Jobject = JSONObject(jsonData);
        var Jarray = Jobject.getJSONObject("_meta");
        return Jarray
    }
    fun meta_dados_user():JSONObject{
        var client = OkHttpClient()
        var request = Request.Builder().url("http://orbeapp.com/web/sendpubuser?_format=json&fields=_ meta").build();
        var responses = client.newCall(request).execute();
        var jsonData = responses.body()!!.string();
        var Jobject = JSONObject(jsonData);
        val Jarray = Jobject.getJSONObject("_meta");
        return Jarray
    }

    fun getPubuser(URL:String): MutableList<Pubuser>{
            val url = URL
            val json = HttpHelper.get(url)
            val carros = fromJson<MutableList<Pubuser>>(json)
            return carros
    }

    fun getPubuserparasoUmresultado(URL:String): Pubuser{
        val url = URL
        val json = HttpHelper.get2(url)
        val carros = fromJson<Pubuser>(json)
        return carros
    }


    fun getLogin(jsonObject_de_user_com_o_msm_nome: ArrayList<JSONObject>,senha_md5_digitada:String):JSONObject? {
        var i = jsonObject_de_user_com_o_msm_nome.size
        for (jsonObject in jsonObject_de_user_com_o_msm_nome) {
           val senhacurrent :String = jsonObject["senha"].toString().toUpperCase()
            if(senhacurrent == senha_md5_digitada){
                return jsonObject;
            }
        }
        return null;
    }

    fun getelemento(url:String):ArrayList<JSONObject>{
        val json = HttpHelper.get2(url)
    //    val json2 = "[" + json + "]"
        val arrayList = parserJson(json)
        if(arrayList.size == 0){
            val r:ArrayList<JSONObject> = arrayListOf()
            return r
        }
        return arrayList
    }


    fun getPubuserid(URL:String): Pubuser {
        val url = URL
        val json = HttpHelper.get(url)
        val carros = fromJson<Pubuser>(json)
        return carros
    }



    // Salva uma publicação
    fun save(pub: JSONObject): Response {
        // Faz POST do JSON da pub
        val json = HttpHelper.post(BASE_URLpesq, pub.toString())
        val response = fromJson<Response>(json)
        return response
    }

    fun savePubuserclass(pubuser: Pubuser):Response{
        val json2 = toJsonobjuser(pubuser).toString()
        val json = HttpHelper.post(BASE_URLuser, json2)
        val response = fromJson<Response>(json)
        return response
    }
    fun savePubupesqclass(pubpesq: Pubpesq):Response{
        val json2 = toJsonobjpesq(pubpesq).toString()
        val json = HttpHelper.post(BASE_URLpesq, json2)
        val response = fromJson<Response>(json)
        return response
    }

  /*  fun getPub(cond:String):ArrayList<JSONObject>{
        try {
            val url = cond
            val json = HttpHelper.get(url)
            val arrayList = parserJson(json)
            return arrayList
        }catch (e:Exception){
            val a:ArrayList<JSONObject> = arrayListOf()
            return a
        }
    } */

    fun toJsonobjpesq(pubpesq: Pubpesq):JSONObject{
        val data = JSONObject();
        data.put("nome", pubpesq.nome)
        data.put("redesocial", pubpesq.redesocial)
        data.put("endereco", pubpesq.endereco)
        data.put("contato",pubpesq.contato)
        data.put("email", pubpesq.email)
        data.put("categoria", pubpesq.categoria)
        if(pubpesq.atvexercida == ""){
            data.put("atvexercida", pubpesq.categoria)
        }else {
            data.put("atvexercida",pubpesq.atvexercida)
        }
        data.put("anoinicio", pubpesq.anoinicio)
        data.put("cnpj", pubpesq.cnpj)
        data.put("representacao", pubpesq.representacao)
        data.put("recurso", pubpesq.recurso)
        data.put("aprovado", "N")
        data.put("latitude", pubpesq.latitude)
        data.put("longitude", pubpesq.longitude)
        data.put("pesquisador", pubpesq.pesquisador)
        data.put("img1", pubpesq.img1)
        data.put("img2", pubpesq.img2)
        data.put("img3", pubpesq.img3)
        data.put("img4", pubpesq.img4)
        data.put("campo1", pubpesq.campo1)
        data.put("campo2", pubpesq.campo2)
        data.put("campo3", pubpesq.campo3)
        data.put("campo4", pubpesq.campo4)
        data.put("campo5", pubpesq.campo5)
        return data
    }

    fun toJsonobjuser(pubpesq: Pubuser):JSONObject{
        val data = JSONObject();
        data.put("nome", pubpesq.nome)
        data.put("redesocial", pubpesq.redesocial)
        data.put("endereco", pubpesq.endereco)
        data.put("contato",pubpesq.contato)
        data.put("email", pubpesq.email)
        data.put("categoria", pubpesq.categoria)
        if(pubpesq.atvexercida == ""){
            data.put("atvexercida", pubpesq.categoria)
        }else {
            data.put("atvexercida",pubpesq.atvexercida)
        }
        data.put("aprovado", "N")
        data.put("latitude", pubpesq.latitude)
        data.put("longitude", pubpesq.longitude)
        data.put("img1", pubpesq.img1)
        data.put("img2", pubpesq.img2)
        data.put("img3", pubpesq.img3)
        data.put("img4", pubpesq.img4)
        data.put("campo1", pubpesq.campo1)
        data.put("campo2", pubpesq.campo2)
        data.put("campo3", pubpesq.campo3)
        data.put("campo4", pubpesq.campo4)
        data.put("campo5", pubpesq.campo5)
        return data
    }



    fun getPub2(cond:String):ArrayList<JSONObject>{
        try {
            val url = cond
            val json = HttpHelper.get(url)
            val json2 = "[" + json + "]"
            val arrayList = parserJson(json2)
            return arrayList
        }catch (e:Exception){
            val a:ArrayList<JSONObject> = arrayListOf()
            return a
        }
    }

    fun getMap(cond: String): List<String> {
        val url = cond
        val json = HttpHelper.get(url)
        val list = fromJson<List<String>>(json)
        return list
    }




    fun saveAvaliacaouser(avl: JSONObject): Response{
        val json = HttpHelper.post("http://orbeapp.com/web/sendavlpubuser?_format=json", avl.toString())
        val response = fromJson<Response>(json)
        return response
    }

    fun save_candidato_pesquisador(candidato: JSONObject): Response{
        val json = HttpHelper.post("http://orbeapp.com/web/sendpesquisador?_format=json", candidato.toString())
        val response = fromJson<Response>(json)
        return response
    }



    fun saveAvaliacaopesq(avl: JSONObject): Response{
        val json = HttpHelper.post("http://orbeapp.com/web/sendavlpubpesq?_format=json", avl.toString())
        val response = fromJson<Response>(json)
        return response
    }


    fun postFoto(file: File?): String {
        // Converte para Base64
        val bytes = file!!.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return base64
    }


    //equivalente ao codigo fromJson em extensions->Json.ky
    public fun parserJson(json :String):ArrayList<JSONObject>{
        val aList = ArrayList<JSONObject>()
        val array = JSONArray(json)
        for(i in 0..array.length() -1){
            aList.add(array.getJSONObject(i))
         }
        return  aList
    }
}