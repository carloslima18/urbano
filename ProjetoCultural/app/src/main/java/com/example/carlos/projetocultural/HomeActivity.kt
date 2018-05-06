package com.example.carlos.projetocultural

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.adapters.HomeAdapter
import com.example.carlos.projetocultural.domain.*
import com.example.carlos.projetocultural.utils.AndroidUtils
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.content_home.*
import android.os.Parcelable
import android.view.View
import android.support.v7.widget.RecyclerView
import android.widget.SearchView;
import android.support.v4.view.MenuItemCompat.getActionView
import android.text.TextUtils
import android.support.v4.view.MenuItemCompat.getActionView
import android.view.Menu
import org.json.JSONObject


class HomeActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    var database1: MyDatabaseOpenHelper?=null
    val MY_PERMISSIONS_REQUEST_PHONE_CALL = 1 //variaveis para permissões
    val MY_WRITE_EXTERNAL_STORAGE = 1
    val MY_READ_EXTERNAL_STORAGE = 1
    val MY_MANEGER_DOCUMENT = 1
    val MY_INTERNET = 1
    val MY_ACCESS_FINE_LOCATION = 1
    val STATE_LIST = "State Adapter Data"


    var pageCount_user:Int= 1;
    var pageCount_pesq:Int= 1;
    var pageuser = 1;
    var pagepesq = 1;
    var ip = MainActivity().ipconfig

    val TAG = HomeActivity::class.java!!.getSimpleName() //para pesquisa no recycleView
    private var mInstance: HomeActivity = getInstance() //para pesquisa no recycleView


    private var parceble: Parcelable ?= null
    private var chave_state = "recycler_state"

    var verifica_se_esta_buscando_publicacoes = 0;
    var simpleSearchView :SearchView?=null
    var textoPesquisa:String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.putExtra("sem splash","0")
            startActivity(intent)
        }


        recyclerViewhome.layoutManager = LinearLayoutManager(applicationContext)
       // recyclerViewhome.itemAnimator = DefaultItemAnimator()
        recyclerViewhome.setHasFixedSize(true)

        GetPermission()
            val extras = intent.extras //verifica se tem algo pois o mainActivitu, manda as requisicoes especificas de escolas etc etc etc.
            if (extras != null) {
                PreenchePubFirst(extras.getString("param"),"","")
            }else{
                PreenchePubFirst(null,"","")
            }


        /*  } else {
        //      val param = extras.getString("param")
        //      PreenchePubFirst(paramrecyclerViewhome.layoutManager =)
        //  }
        //  recyclerViewhome.addOnScrollListener(recyclerViewOnScrollListener);
        // val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        // StrictMode.setThreadPolicy(policy)*/
        simpleSearchView = findViewById(R.id.searchView) as SearchView

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }

        get_meta_dados()

    }


    override fun onQueryTextSubmit(query: String): Boolean {
        toast("submeteu $query")
        finish()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        toast("mudando")
        finish()
        return true
    }



    override fun onSaveInstanceState(state: Bundle?) {
        super.onSaveInstanceState(state)

        // Save list state
        parceble = recyclerViewhome.layoutManager.onSaveInstanceState()
        state!!.putParcelable(chave_state, parceble)
    }

    override fun onRestoreInstanceState(state: Bundle?) {
        super.onRestoreInstanceState(state)

        // Retrieve list state and list/item positions
        if (state != null)
            parceble = state.getParcelable(chave_state)
    }
    override fun onResume() {
        super.onResume()



        FAB_att.setOnClickListener(){
            if(verifica_se_esta_buscando_publicacoes == 0) {
                //addaoadapter(3)
                pagepesq++
                pageuser++
                PreenchePubFirst(null,"","")
            }else{
                toast("Aguarde um momento")
            }

            if (pagepesq == pageCount_pesq) {
                pagepesq = 0
            }
            if( pageuser == pageCount_user){
                //toast("Pagina final de usuários")
                pageuser = 0
            }
        }
        if (parceble != null) {
            recyclerViewhome.layoutManager.onRestoreInstanceState(parceble)
        }
        imagemback.setOnClickListener {
            if(verifica_se_esta_buscando_publicacoes == 0) {
                if (pagepesq != 1 || pageuser != 1) {
                    if (pageuser != 1) {
                        pageuser--
                    }
                    if (pagepesq != 1) {
                        pagepesq--
                    }
                    PreenchePubFirst(null,"","")
                } else {
                    toast("Inicio")
                }
            }else{
                toast("Aguarde um momento")
            }
        }


        simpleSearchView?.setOnClickListener {
            textoPesquisa = (simpleSearchView!!.query.toString())
            toast("buscando $textoPesquisa")
            var user ="http://orbeapp.com/web/sendpubuser?_format=json&PublicacaouserSearch[nome]=$textoPesquisa&PublicacaouserSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,latitude,longitude,img1"
            var pesq =  "http://orbeapp.com/web/sendpubpesq?_format=json&PublicacaopesqSearch[nome]=$textoPesquisa&PublicacaopesqSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,anoinicio,cnpj,representacao,recurso,latitude,longitude,pesquisador,campo1,campo2,campo3,campo4,campo5,img1"
            PreenchePubFirst(null,user,pesq)
        }
    }


    override fun onPause() {
        super.onPause()

    }




    @Synchronized //para pesquisa no recycleView
    fun getInstance(): HomeActivity {
        return mInstance
    }


    fun addMaisPub(r:RecyclerView,l:MutableList<Pubpesq>){
        val h:HomeAdapter?=null
        h?.addAll(r,l)
    }


    open fun onClickact(pubpesq: Pubpesq?):Unit {
        if(AndroidUtils.isNetworkAvailable(applicationContext)) {
            //viewPubFragment(savedInstanceState)
            val intent = Intent(baseContext, actViewPub::class.java)
            intent.putExtra("idpub", pubpesq?.id.toString())
            intent.putExtra("lat",pubpesq?.latitude?.toDouble())
            intent.putExtra("log",pubpesq?.longitude?.toDouble())
            intent.putExtra("nome", pubpesq?.nome)
            if(pubpesq?.pesquisador == 0){
                intent.putExtra("pesquisador", "")
            }else{
                intent.putExtra("pesquisador", pubpesq?.pesquisador.toString())
            }


            intent.putExtra("atvex",  pubpesq?.atvexercida)
            intent.putExtra("redesocial", pubpesq?.redesocial)
            intent.putExtra("endereco", pubpesq?.endereco)
            intent.putExtra("contato", pubpesq?.contato)
            intent.putExtra("categoria", pubpesq?.categoria)
            intent.putExtra("representacao", pubpesq?.representacao)
            intent.putExtra("cnpj", pubpesq?.cnpj)
            intent.putExtra("anoinicio", pubpesq?.anoinicio)
            intent.putExtra("recurso", pubpesq?.recurso)
            intent.putExtra("campo1", pubpesq?.campo1)
            intent.putExtra("campo2", pubpesq?.campo2)
            intent.putExtra("campo3", pubpesq?.campo3)
            intent.putExtra("campo4", pubpesq?.campo4)
            intent.putExtra("campo5", pubpesq?.campo5)
            intent.putExtra("vemdomappubOUvemdohomeactivity","homeactivity")
            //EU NAO MANDEI A IMG POR CONTA QUE ULTRAPASSA O LIMITE DE TAMANHO
            //intent.putExtra("img1", pubuser?.img1)
            startActivity(intent)
            // toastx()
        }else{
            toast("verifique sua conexão")
        }

    }

    open fun SendServer(pubuser: Pubpesq):Boolean{

        return true
    }




    override fun onBackPressed() {
        finish()
    }


    var pubuser : MutableList<Pubuser> = mutableListOf()
    var pubpesq : MutableList<Pubpesq> = mutableListOf()
    // val longitude: ArrayList<String> = arrayListOf()


    fun get_meta_dados(){
        if(AndroidUtils.isNetworkAvailable(applicationContext) ) {
           // progressBarHome.visibility = View.VISIBLE
            val handle = Handler()
            try {
                Thread {
                    val meta_dados_pesq = pubService.meta_dados_pesq()
                    val meta_dados_user = pubService.meta_dados_user()
                    handle.post {
                        if(meta_dados_pesq != null || meta_dados_user != null) {
                            pageCount_pesq = meta_dados_pesq.getString("pageCount").toInt()
                            pageCount_user = meta_dados_user.getString("pageCount").toInt()
                            //toast("user $pageCount_user and pesq $pageCount_pesq")
                        }else{
                            toast("Meta dados não encontrados")
                        }
                    }
                }.start()
            }catch (e:Exception){
                toast("error, tente novamente")
            }
        }
    }


    fun juntaPubpesqWithPubuser(pubuser: MutableList<Pubuser>,pubpesq: MutableList<Pubpesq>):MutableList<Pubpesq>{
        for (user in pubuser) {
            val pesqAux = Pubpesq()
            pesqAux.id = user.id
            pesqAux.nome = user.nome
            pesqAux.redesocial = user.redesocial
            pesqAux.endereco = user.endereco
            pesqAux.contato = user.contato
            pesqAux.email = user.email
            pesqAux.atvexercida = user.atvexercida
            pesqAux.categoria = user.categoria
            pesqAux.latitude = user.latitude
            pesqAux.longitude = user.longitude
            pesqAux.img1 = user.img1
            pesqAux.campo1 = user.campo1
            pesqAux.campo2 = user.campo2
            pesqAux.campo3 = user.campo3
            pesqAux.campo4 = user.campo4
            pesqAux.campo5 = user.campo5
            pubpesq.add(pesqAux)
        }
        return pubpesq
    }

    fun PreenchePubFirst(categoria:String?,searchuser:String,searchpesq:String){
        verifica_se_esta_buscando_publicacoes = 1
        //comentadox

        var URL = "http://orbeapp.com/web/sendpubuser?_format=json&PublicacaouserSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,latitude,longitude,img1&page=$pageuser"
        var URLpesq = "http://orbeapp.com/web/sendpubpesq?_format=json&PublicacaopesqSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,anoinicio,cnpj,representacao,recurso,latitude,longitude,pesquisador,campo1,campo2,campo3,campo4,campo5,img1&page=$pagepesq"
        if(categoria != null) {
            if (categoria != null) {
                URL = "http://orbeapp.com/web/sendpubuser?_format=json&PublicacaouserSearch[categoria]=$categoria&PublicacaouserSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,latitude,longitude,img1&page=$pageuser"
                URLpesq = "http://orbeapp.com/web/sendpubpesq?_format=json&PublicacaopesqSearch[categoria]=$categoria&PublicacaopesqSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,anoinicio,cnpj,representacao,recurso,latitude,longitude,pesquisador,campo1,campo2,campo3,campo4,campo5,img1&page=$pagepesq"
            }
        }
        if(searchuser != "" || searchpesq != ""){
            URL = searchuser
            URLpesq = searchpesq
        }

        if(AndroidUtils.isNetworkAvailable(applicationContext) ) {
            progressBarHome.visibility = View.VISIBLE
            toast("buscando Publicações")
            val handle = Handler()
            try {
                Thread {
                    pubuser = pubService.getPubuser(URL)
                    pubpesq = pubService.getPubpesq(URLpesq)
                    handle.post {

                        /*  //vefica se a publicação passada veio dnv, se vir, zera a contagem da pagina
                          if(nomedapublicao_passada_pesq != null) {
                              if (pubpesq[0].nome == nomedapublicao_passada_pesq) {
                                  toast("Vontando as primeiras p")
                                  pagepesq = 1
                              }
                          }
                          if(nomedapublicao_passada_user != null) {
                              if (pubuser[0].nome == nomedapublicao_passada_user) {
                                  toast("Vontando as primeiras u")
                                  pagepesq = 1
                              }
                          }

                          if(pubuser.size != 0){
                              nomedapublicao_passada_user = pubuser[0].nome
                          }
                          if(pubpesq.size != 0){
                              nomedapublicao_passada_pesq = pubpesq[0].nome
                          }
                          */
                        if(pubuser.size != 0 || pubpesq.size != 0){
                            pubpesq = juntaPubpesqWithPubuser(pubuser, pubpesq)
                            if (pubpesq.size != 0) {//0 era para fala se tinha paginaçao ou n(qual pagina que era?)
                                recyclerViewhome.adapter = HomeAdapter(pubpesq, 0, { onClickact(it) }, { SendServer(it) })
                                progressBarHome.visibility = View.GONE
                                verifica_se_esta_buscando_publicacoes = 0
                            } else {
                                toast("Nenhuma publicação referente encontrada!")
                                finish()
                            }
                        }else{
                            progressBarHome.visibility = View.GONE
                            toast("Nenhuma publicação referente encontrada")
                        }
                    }
                }.start()
            }catch (e:Exception){
                toast("error, tente novamente")
            }
        }else{
            toast("sem conecção")
        }
    }

    fun GetPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), MY_INTERNET)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.MANAGE_DOCUMENTS), MY_MANEGER_DOCUMENT)
        }
    }



}
