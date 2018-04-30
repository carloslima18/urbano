package com.example.carlos.projetocultural

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.example.carlos.projetocultural.utils.HttpHelper
import kotlinx.android.synthetic.main.activity_cadastrapesq_.*
import kotlinx.android.synthetic.main.activity_login.*
import okio.HashingSink.md5
import org.jetbrains.anko.db.*

import org.jetbrains.anko.db.NULL
import org.json.JSONObject
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    var database: MyDatabaseOpenHelper?=null
    val ip = MainActivity().ipconfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);


        database = MyDatabaseOpenHelper.getInstance(applicationContext)

        buscapesquisadorsalvo()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 1)
        }


        myToolbar.setNavigationOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra("sem splash","0")
            startActivity(intent)
        }

        cadastrapesq.setOnClickListener {
            val intent = Intent(this@LoginActivity, Cadastrapesq_Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

    var arrayList:ArrayList<JSONObject> = ArrayList()
    override fun onResume() {
        super.onResume()
        btnSenha.setOnClickListener {
            conferesenha()
        }

    }

    var dialog :ProgressDialog?= null


    fun conferesenha() {
        val tsenha = findViewById<EditText>(R.id.tSenha)
        val tlogin = findViewById<EditText>(R.id.tLogin)
        if (tsenha.text.toString() == "" || tlogin.text.toString() == "") {
            toast("campo nulo")
        }else{
            dialog = ProgressDialog.show(this, "Um momento", "Autenticando", false, true)

            //var obj :ArrayList<JSONObject> ?= null
            val loguin = tlogin.text.toString()
            val senha = tsenha.text.toString()
            val url = "http://$ip/urbano/sendpesquisador?_format=json&&PesquisadorSearch[campo2]=S&PesquisadorSearch[nome]=$loguin&fields=id,nome,senha" //&PesquisadorSearch[campo2]=S
            if (AndroidUtils.isNetworkAvailable(applicationContext)) {
                Thread {
                    arrayList = pubService.getelemento(url)
                    runOnUiThread {
                        val resul = pubService.getLogin(arrayList, toMD5hash(senha))
                        if (resul != null) {
                            val intent = Intent(this@LoginActivity, PesquisadorhomeActivity::class.java)
                            var idpesquisador = resul.getString("id").toInt()
                            intent.putExtra("idpesq", resul.getString("id").toInt())
                            insere(idpesquisador.toString(),senha,loguin)
                            startActivity(intent)
                            dialog?.dismiss()
                            finish()
                        } else {
                            toast("senha ou usuario incorretos")
                            dialog?.dismiss()
                            finish()
                        }
                        dialog?.dismiss()
                    }
                }.start()
            }else{
                toast("Sem conexão com Internet.")
            }
        }

    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.dismiss()
    }

    private fun bytearraytoHexString(array: Array<Byte>):String{
        val result = StringBuilder(array.size*2)
        for(byte in array){
            val toAppend = String.format("%2X",byte).replace(" ","0")
            result.append(toAppend).append("")
        }
        // result.setLength(result.length - 1)
        return result.toString()
    }
    var result = ""

    private fun toMD5hash(text:String):String{
        try{
            val md5=MessageDigest.getInstance("MD5")
            val md5HashBytes = md5.digest(text.toByteArray()).toTypedArray()
            result = bytearraytoHexString(md5HashBytes)
            return  result
        }catch (e:Exception){
            result = "error: ${e.message}"
        }
        return  result
    }


    fun insere(idpesquisador:String,tSenha:String,tlogin:String) {
        database?.use {
            insert("pesquisador",
                    "nome" to tlogin,
                    "senha" to tSenha,
                    "idweb" to idpesquisador
            )
        }

        val id = database?.use {
            select("pesquisador", "idweb").exec { parseList<String>(classParser()) }
        }
    }

    fun buscapesquisadorsalvo(){

        val id = database?.use {
            select("pesquisador", "nome").exec { parseList<String>(classParser()) }
        }
        if(id?.size != 0 && id != null){
            startActivity(Intent(this, PesquisadorhomeActivity::class.java))
            toast("Bem vindo\"" + id[0] + "\"")
            finish()
        }
    }


    /*   val dialog = ProgressDialog.show(this, "Um momento","Verificando",false,true)
       Thread{
           //&PublicacaoSearch[categoria]=feiras&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1"
           var listItems :ArrayList<JSONObject> = arrayListOf()
           val URL = "http://192.168.15.5/cult/sendpesquisador?_format=json&PesquisadorSearch[nome]=${tlogin.text}&PesquisadorSearch[senha]=${tsenha.text}&fields=campo1"
           listItems = pubService.getPub(URL)
           runOnUiThread {
               val t = listItems[0].getString("campo1")
               if(t == "ativo"){
                   val intent = Intent(applicationContext, formActivity::class.java)
                   toast("bem sucessido")
                   dialog.dismiss()
                   startActivity(intent)
               }else{
                   toast("dados incorretos")
               }
           }
       }.start() */



}
