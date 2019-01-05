package com.example.carlos.projetocultural.publicacaoes_recentes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tela_listagem_pub_recentes.*
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.example.carlos.projetocultural.adapters.Tela_listagem_pub_recentes_Adapter
import com.example.carlos.projetocultural.domain.*
import com.example.carlos.projetocultural.utils.AndroidUtils
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.content_tela_listagem_pub_recentes.*
import android.os.Parcelable
import android.view.View
import android.widget.SearchView;
import com.example.carlos.projetocultural.MainActivity
import com.example.carlos.projetocultural.R


class Tela_listagem_pub_recentes_Activity : AppCompatActivity(), SearchView.OnQueryTextListener {


    val MY_PERMISSIONS_REQUEST_PHONE_CALL = 1 //variaveis para permissões
    val MY_WRITE_EXTERNAL_STORAGE = 1
    val MY_READ_EXTERNAL_STORAGE = 1
    val MY_MANEGER_DOCUMENT = 1
    val MY_INTERNET = 1
    val MY_ACCESS_FINE_LOCATION = 1
    val STATE_LIST = "State Adapter Data"

//para armazenar a quantidade de paginas que tem la no servidor
    var pageCount_user:Int= 1;
    var pageCount_pesq:Int= 1;
    var pageuser = 1;
    var pagepesq = 1;

    val TAG = Tela_listagem_pub_recentes_Activity::class.java!!.getSimpleName() //para pesquisa no recycleView
    private var mInstance: Tela_listagem_pub_recentes_Activity = getInstance() //para pesquisa no recycleView(ainda n terminado)
    private var parceble: Parcelable ?= null
    private var chave_state = "recycler_state"
    var verifica_se_esta_buscando_publicacoes = 0;
    var simpleSearchView :SearchView?=null
    var textoPesquisa:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_listagem_pub_recentes)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@Tela_listagem_pub_recentes_Activity, MainActivity::class.java)
            intent.putExtra("sem splash","0") // para quando clicaar na seta de voltar, n rode o splash
            startActivity(intent)
        }


        recyclerViewhome.layoutManager = LinearLayoutManager(applicationContext)
       // recyclerViewhome.itemAnimator = DefaultItemAnimator()
        recyclerViewhome.setHasFixedSize(true)

        GetPermission()
            val extras = intent.extras //verifica se tem algo pois o mainActivitu, manda as requisicoes especificas de escolas etc etc etc.
            if (extras != null) {//se n tiver alguma restrição vinda de main actvity para pegar alguma publicação pespecifica
                PreenchePubFirst(extras.getString("param"),"","")
            }else{
                PreenchePubFirst(null,"","") //busca todas as publicações sem restriçÕes
            }


        //  recyclerViewhome.addOnScrollListener(recyclerViewOnScrollListener);
        // val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        // StrictMode.setThreadPolicy(policy)*/
        simpleSearchView = findViewById(R.id.searchView) as SearchView // caixa para pesquisa especifica

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }

        //pega a quantidade de paginas que tem no servidor e armazena nas variaveis globais mostradas acima
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

    //para paginação (eu acho) ainda n terminado
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

        //se o usuario buscar mais publicações na seta (->)
        FAB_att.setOnClickListener(){
            if(verifica_se_esta_buscando_publicacoes == 0) {
                //addaoadapter(3)
                pagepesq++//se estiver buscando, adiciona ++ a cada pagina
                pageuser++
                PreenchePubFirst(null,"","")
            }else{
                toast("Aguarde um momento")
            }
            if (pagepesq == pageCount_pesq) {
                //toast("")
                pagepesq = 0
            }
            if( pageuser == pageCount_user){
                //toast("Pagina final de usuários")
                pageuser = 0
            }
        }
        //para manter a instancia da pagina (eu acho)

        if (parceble != null) {
            recyclerViewhome.layoutManager.onRestoreInstanceState(parceble)
        }

        //se o usuario clicar em voltar
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


        // para pesquisar
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
    fun getInstance(): Tela_listagem_pub_recentes_Activity {
        return mInstance
    }



    // para clicar nas pub recebtes e ir para ActViewPub (onde vizualiza as publicações clicadas)
    open fun onClickact(pubpesq: Pubpesq?):Unit {
        if(AndroidUtils.isNetworkAvailable(applicationContext)) {
            //viewPubFragment(savedInstanceState)
            val intent = Intent(baseContext, Tela_vizualiza_publicacoes_recentes_Activity::class.java)
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

    //busca os dados de quantas paginas tem no servidor
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

    //junta as publicações de pesquisadores
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


    //faz a busca para preenchimento do recyclview
    fun PreenchePubFirst(categoria:String?,searchuser:String,searchpesq:String){
        verifica_se_esta_buscando_publicacoes = 1
        //comentadox // faz a busca
        var URL = "http://orbeapp.com/web/sendpubuser?_format=json&PublicacaouserSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,latitude,longitude,img1&page=$pageuser"
        var URLpesq = "http://orbeapp.com/web/sendpubpesq?_format=json&PublicacaopesqSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,anoinicio,cnpj,representacao,recurso,latitude,longitude,pesquisador,campo1,campo2,campo3,campo4,campo5,img1&page=$pagepesq"

        //verifica se a categoria ta com alguma coisa, pois se tiver, ele faz a busca por categoria
        if(categoria != null) {
            if (categoria != null) {
                URL = "http://orbeapp.com/web/sendpubuser?_format=json&PublicacaouserSearch[categoria]=$categoria&PublicacaouserSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,latitude,longitude,img1&page=$pageuser"
                URLpesq = "http://orbeapp.com/web/sendpubpesq?_format=json&PublicacaopesqSearch[categoria]=$categoria&PublicacaopesqSearch[aprovado]=S&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,anoinicio,cnpj,representacao,recurso,latitude,longitude,pesquisador,campo1,campo2,campo3,campo4,campo5,img1&page=$pagepesq"
            }
        }
        //verifica se existe alguma coisa na caixa de pesquisa, para caso tiver, ele buscar especificadamente
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
                        if(pubuser.size != 0 || pubpesq.size != 0){
                            pubpesq = juntaPubpesqWithPubuser(pubuser, pubpesq)
                            if (pubpesq.size != 0) {//0 era para fala se tinha paginaçao ou n(qual pagina que era?)
                                recyclerViewhome.adapter = Tela_listagem_pub_recentes_Adapter(pubpesq, 0, { onClickact(it) }, { SendServer(it) })
                                progressBarHome.visibility = View.GONE
                                verifica_se_esta_buscando_publicacoes = 0 // seta com 0, pois se estiver buscando uma pub, e o usuario requisitar mais, vai mostra a msg "aguarde" pois já esta buscando
                            }else {
                                toast("Nenhuma publicação referente encontrada!")
                                finish()
                            }
                        }else{
                            progressBarHome.visibility = View.GONE
                            toast("Nenhuma publicação referente encontrada")
                            finish()
                        }
                    }
                }.start()
            }catch (e:Exception){
                toast("error, tente novamente")
            }
        }else{
            toast("sem conecção")
            finish()
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
