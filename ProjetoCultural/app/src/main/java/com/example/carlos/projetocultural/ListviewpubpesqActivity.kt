package com.example.carlos.projetocultural

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Point
import android.net.Uri
import android.opengl.Visibility
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.adapters.PubpesqAdapter
import com.example.carlos.projetocultural.adapters.PubuserAdapter
import com.example.carlos.projetocultural.domain.*
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.example.carlos.projetocultural.utils.CameraHelper
import kotlinx.android.synthetic.main.activity_listviewpubpesq.*
import kotlinx.android.synthetic.main.content_principal.*
import org.jetbrains.anko.act
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.json.JSONObject
import android.os.Parcelable
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.Validacpf
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.list_row_main.*
import org.jetbrains.anko.db.*


class ListviewpubpesqActivity : AppCompatActivity() {
    protected var pubpesqbd = listOf<Pubpesq>()
    var KEY_RECYCLER_STATE:String =""
    var idpesquisador:String = "0";
    var database: MyDatabaseOpenHelper?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listviewpubpesq)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);


        database = MyDatabaseOpenHelper.getInstance(applicationContext)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 2)
        }

        recyclerViewpesq.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewpesq.itemAnimator = DefaultItemAnimator()
        recyclerViewpesq.setHasFixedSize(true)
        val extras = intent.extras
        val pes = database!!.use {
            select("pesquisador", "idweb").exec { parseList<String>(classParser()) }
        }
        idpesquisador = pes.get(0)
        if(idpesquisador == "0" || idpesquisador == ""){
            toast("problema de pesquisador")
        }

        taskPubpesq()
    }

    /*
    var mListState :String?=null

    override fun onSaveInstanceState(state: Bundle?) {
        super.onSaveInstanceState(state)
        val listState = recyclerViewpesq.getLayoutManager().onSaveInstanceState()
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState)

    }

    override fun onRestoreInstanceState(state: Bundle?) {
        super.onRestoreInstanceState(state)
        if (mBundleRecyclerViewState != null) {
            val listState = mBundleRecyclerViewState.getParcelable<Parcelable>(KEY_RECYCLER_STATE)
            recyclerViewpesq.getLayoutManager().onRestoreInstanceState(listState)
        }
    }
    val mBundleRecyclerViewState = Bundle()

    override fun onPause() {
        super.onPause()
        // save RecyclerView state
        val recyclerViewState = recyclerViewpesq.getLayoutManager().onSaveInstanceState();//save
        recyclerViewpesq.getLayoutManager().onRestoreInstanceState(recyclerViewState);//restore

        val listState = recyclerViewpesq.getLayoutManager().onSaveInstanceState()
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState)
    }
*/

    override fun onResume() {
        super.onResume()
        FAB_pesq.setOnClickListener {
            val provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(provider == null){
                toast("Ligue seu GPS")
            }else {
                val intent = Intent(this@ListviewpubpesqActivity, FormActivity::class.java)
            //    intent.putExtra("idpesq", idpesquisador)
                startActivity(intent)
            }
        }
        taskPubpesq()
    }



    open fun taskPubpesq() {
        Thread{
            pubpesqbd = PubpesqService.getPubpesq()
            runOnUiThread {
                recyclerViewpesq.adapter = PubpesqAdapter(pubpesqbd, {operacaoFragment(it)},{SendServer(it)})
            }
        }.start()
    }




    fun SendServer(pubpesq: Pubpesq): Boolean{
        act.alert("Escolha uma ação!") {

            //title = ""
            positiveButton("Visualizar") {
                val intent = Intent(this@ListviewpubpesqActivity, vizualizapesqActivity::class.java)
                intent.putExtra("idx",pubpesq.id)
                startActivity(intent)
            }
            negativeButton("Enviar"){ // botão para caso a pessoa apertar em cancelar o envio da publicação
                Enviadados(pubpesq)
            }
        }.show()
        return true
    }

    val camera =CameraHelper()

    fun Enviadados(pubpesq:Pubpesq){
        val id = database?.use {
            select("pesquisador", "idweb").exec { parseList<String>(classParser()) }
        }
        if(id?.size != 0 && id != null) {
            if (AndroidUtils.isNetworkAvailable(applicationContext)) {
                if (Validacpf().validateEmailFormat(pubpesq.email) || pubpesq.email == "") {
                    if(pubpesq.img1 != "" && pubpesq.img2 != "" && pubpesq.img3 != "" && pubpesq.img4 != "" && pubpesq.contato != "" && pubpesq.endereco != "" && pubpesq.nome != "" && pubpesq.categoria != "") {
                            val dialog = ProgressDialog.show(this, "Um momento", "Enviando sua publicação", false, true)
                            dialog.setCancelable(false);
                            Thread {
                                pubpesq.pesquisador = id[0].toInt()
                                pubpesq.img1 = camera.uriForBase64(baseContext, pubpesq.img1)
                                pubpesq.img2 = camera.uriForBase64(baseContext, pubpesq.img2)
                                pubpesq.img3 = camera.uriForBase64(baseContext, pubpesq.img3)
                                pubpesq.img4 = camera.uriForBase64(baseContext, pubpesq.img4)
                                pubService.savePubupesqclass(pubpesq)
                                PubpesqService.deletepesquser(pubpesq)
                                taskPubpesq()
                                runOnUiThread {
                                    dialog.dismiss()
                                    toast("publicação enviada")
                                }
                            }.start()
                    }else{
                        toast("informações básicas/fotos estão vazias.")
                    }
                }else{
                    toast("Email invalido")
                }
            } else {
                toast("sem conexão")
            }
        }else{
            toast("desculpe, pesquisador não identificado")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ListviewpubpesqActivity, PesquisadorhomeActivity::class.java)
       // intent.putExtra("idpesq",idpesquisador)
        startActivity(intent)
    }



    private fun operacaoFragment(pubpesq: Pubpesq){
        val ft= supportFragmentManager.beginTransaction()
        val fragAnterior = supportFragmentManager.findFragmentByTag(UpdatepubpesqActivity.KEY2)
        if (fragAnterior != null) {
            ft.remove(fragAnterior)
        }
        ft.addToBackStack(null)

        val bundle= Bundle()//bundle para passagem de parametros(dados a baixo) para outra atv
        bundle.putString("categorialr",pubpesq.categoria)
        bundle.putInt("idx",pubpesq.id)
        val dialog = UpdatepubpesqActivity()//estancia o fragment para passar os dados do bundle
        dialog.arguments= bundle
        dialog.show(ft, UpdatepubpesqActivity.KEY2)//chama o fragment

    }

}
