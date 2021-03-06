package com.example.carlos.projetocultural.publicacaoes_recentes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.example.carlos.projetocultural.Configuracao_google_maps_Activity
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.example.carlos.projetocultural.utils.OperacoesEconfiguracoesCameraImagemCameraHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_tela_vizualiza_publicacoes_recentes.*
import kotlinx.android.synthetic.main.content_tela_vizualiza_publicacoes_recentes.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.jetbrains.anko.browse


class Tela_vizualiza_publicacoes_recentes_Activity : AppCompatActivity(), OnMapReadyCallback {

    var id:String = ""
    var camera = OperacoesEconfiguracoesCameraImagemCameraHelper()
    var mapView : MapView?=null
    var latitude:Double?= null // variaveis para pegar via intentPut extra a long e lat enviadas..
    var longitude: Double?= null
    var recurso : String = ""
    var nome : String = ""
    var atvex : String = ""
    var representacao : String = ""
    var cnpj : String = ""
    var anoinicio : String = ""
    var pesquisador : String = ""
    var redesocial : String = ""
    var endereco : String = ""
    var contato : String = ""
    var img1 : String = ""
    var categoria : String = ""
    var campo1 : String = ""
    var campo2 : String = ""
    var campo3 : String = ""
    var campo4 : String = ""
    var campo5 : String = ""
    val mappub = Configuracao_google_maps_Activity()
    var base641:String?=null
    var base642:String?=null
    var base643:String?=null
    var base644:String?=null
    var e_pesquisador:String = ""
    var save :Bundle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_vizualiza_publicacoes_recentes)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);




         mapViewPub.visibility = View.INVISIBLE
        var mappubOUhomeact:String = "homeactivity"
        //pega dados do intent
        val extras = intent.extras
        if(extras != null) {
            mappubOUhomeact = extras.getString("vemdomappubOUvemdohomeactivity")

            id = extras.getString("idpub")
            save = savedInstanceState
            getinfouser(intent.extras)
            categoria = extras.getString("categoria")
            setacor(categoria)
            if (mappubOUhomeact == "mappub") {//SE FOR MAPPUB
                e_pesquisador = extras.getString("e_pesquisador")
                if (e_pesquisador != "0") {
                    getinfopesquisador(intent.extras)
                }
            } else if (mappubOUhomeact == "homeactivity") {
                pesquisador = extras.getString("pesquisador")
                if (pesquisador != "") {
                    getinfopesquisador(intent.extras)
                }
            }

            //MainActivity().GetPermission()
            mapViewPub.visibility = View.GONE
            if (pesquisador != "" && e_pesquisador != "0") {
                // mostrarinfo.visibility = View.VISIBLE
            }


            //preenche os itens da view dessa activity, com os dados capturados do webService
            if (AndroidUtils.isNetworkAvailable(applicationContext)) {
                getViewPub()
            } else {
                toast("sem coneçcão")
                finish()
            }
            ////para o MAPview
            mapView = findViewById<MapView>(R.id.mapViewPub) as MapView
            mapView?.onCreate(savedInstanceState)
            mapView?.getMapAsync(this)
        }

    }

    //para visualizar os dados a partir do click de uma view da listView na atividade principal
    fun getViewPub(){
        val titulo = findViewById<TextView>(R.id.tituloPub) as TextView
        //val dialog = ProgressDialog.show(this, "Um momento","buscando dados",false,true)
        carregaimg(pesquisador)
        carrega_alv(pesquisador)

        titulo.text = nome


        if(contato == "" || contato == "não informado" ){
            tablerow_contato.visibility = View.GONE
            viewcontato.visibility = View.GONE
        }else{
            Contato2.text = contato
        }
        if(atvex == categoria){
            tablerow_atividadeex.visibility = View.GONE
            viewatv.visibility = View.GONE
        }else{
            atividadeexercida2.text = atvex
        }
        if(endereco == "" || endereco == "não informado" ){
            tablerow_endereco.visibility = View.GONE
            viewend.visibility = View.GONE
        }else{
            endereco2.text = endereco
        }
        categoria2.text = categoria


        infodepub.text = "O ano de inicío das atividades deste local ocorreu em $anoinicio \n " +
                        "O local tem recurso retirado de maneira $recurso  \n " +
                        "e tal lugar tem como representação $representacao" + " " + "\n"
    }


    fun carrega_alv(pesquisador:String){
        var jsonObject_nota:ArrayList<JSONObject> = ArrayList()
        var urlnota:String = ""
        var x = -1;
        if(pesquisador != ""){
            x = 1;
            urlnota = "http://orbeapp.com/web/sendavlpubpesq?_format=json&AvaliacaopubpesqSearch[idpubpesq]=$id&fields=nota"
        }else{
            x = 0;
            urlnota = "http://orbeapp.com/web/sendavlpubuser?_format=json&AvaliacaopubuserSearch[idpubpesq]=$id&fields=nota"
        }
        Thread {
            if (x == 0) {
                jsonObject_nota = pubService.getelemento(urlnota)
            } else {
                jsonObject_nota = pubService.getelemento(urlnota)
            }
            runOnUiThread {
                val tam = jsonObject_nota.size
                var i = 0
                var notas: Int = 0
                while (i < tam) {
                    val row = jsonObject_nota.get(i);
                    notas += row.getString("nota").toInt()
                    i++
                }
                if (tam != 0) {
                    val notafinal = notas / tam
                    textviewAvl.text = ("Avaliação: $notafinal")
                }else{
                    textviewAvl.text = ("Avaliação: indisponivel")
                }

            }
        }.start()
    }
    fun carregaimg(pesquisador:String){
        progressbaract1.visibility = View.VISIBLE
        progressbaract2.visibility = View.VISIBLE
        progressbaract3.visibility = View.VISIBLE
        progressbaract4.visibility = View.VISIBLE

        //var obj:ArrayList<JSONObject> = arrayListOf()
        var url:String = ""

        var x = -1;
        if(pesquisador != ""){
             x = 1;
             url =  "http://orbeapp.com/web/sendpubpesq/$id?_format=json&fields=img1,img2,img3,img4"
        }else{
            x = 0;
             url =  "http://orbeapp.com/web/sendpubuser/$id?_format=json&fields=img1,img2,img3,img4"
        }
        Thread{
            //   listItems = PubuserService.getPubuserId(id.toInt())
            var tpesq :Pubpesq?=null
            var tuser :Pubuser?=null
            if(x==0){
                tuser = pubService.getPubuserparasoUmresultado(url)
            }else{
                tpesq = pubService.getPubpesqparasoUmresultado(url)
            }
            runOnUiThread {
                if(x==0){
                    base641 = tuser?.img1;base642 = tuser?.img2;base643 = tuser?.img3;base644 = tuser?.img4
                }else{
                    base641 = tpesq?.img1;base642 = tpesq?.img2;base643 = tpesq?.img3;base644 = tpesq?.img4
                }
                imgViewPub1.setImageBitmap(null);     imgViewPub2.setImageBitmap(null);     imgViewPub3.setImageBitmap(null);     imgViewPub4.setImageBitmap(null)
                if(base641 != null) {
                    imgViewPub1.setImageBitmap(camera.base64ForBitmap2(base641))
                }else{
                    imgViewPub1.visibility = View.GONE
                    toast("foto 1 não encontrada")
                }
                if(base642 != null) {
                    imgViewPub2.setImageBitmap(camera.base64ForBitmap2(base642))
                }else{
                    imgViewPub2.visibility = View.GONE
                    toast("foto 2 não encontrada")
                }
                if(base643 != null) {
                    imgViewPub3.setImageBitmap(camera.base64ForBitmap2(base643))
                }else{
                    imgViewPub3.visibility = View.GONE
                    toast("foto 3 não encontrada")
                }
                if(base644 != null) {
                    imgViewPub4.setImageBitmap(camera.base64ForBitmap2(base644))
                }else{
                    imgViewPub4.visibility = View.GONE
                    toast("foto 4 não encontrada")
                }
                progressbaract1.visibility = View.GONE
                progressbaract2.visibility = View.GONE
                progressbaract3.visibility = View.GONE
                progressbaract4.visibility = View.GONE

                //carrega os botes de clic das img logo apos ter img, se n da erro

                cardviewimgbig.setOnClickListener {
                    cardviewimgbig.visibility = View.GONE
                    llimg12.visibility = View.VISIBLE
                    llimg34.visibility = View.VISIBLE
                }
                rimg1.setOnClickListener {
                    imgViewPubbig.setImageBitmap(camera.base64ForBitmap2(base642))
                    // Glide.with(this).load(base641).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgViewPubbig)
                    cardviewimgbig.visibility = View.VISIBLE
                    llimg12.visibility = View.GONE
                    llimg34.visibility = View.GONE
                }
                rimg2.setOnClickListener {
                    //  Glide.with(this).load(base642).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgViewPubbig)
                    imgViewPubbig.setImageBitmap(camera.base64ForBitmap2(base641))
                    cardviewimgbig.visibility = View.VISIBLE
                    llimg12.visibility = View.GONE
                    llimg34.visibility = View.GONE
                }

                rimg3.setOnClickListener {
                    //  Glide.with(this).load(base643).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgViewPubbig)
                    imgViewPubbig.setImageBitmap(camera.base64ForBitmap2(base643))
                    cardviewimgbig.visibility = View.VISIBLE
                    llimg12.visibility = View.GONE
                    llimg34.visibility = View.GONE
                }
                rimg4.setOnClickListener {
                    imgViewPubbig.setImageBitmap(camera.base64ForBitmap2(base644))
                    // Glide.with(this).load(base644).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgViewPubbig)
                    cardviewimgbig.visibility = View.VISIBLE
                    llimg12.visibility = View.GONE
                    llimg34.visibility = View.GONE
                }

             /*   var imgspubs:ArrayList<Bitmap> = ArrayList()
                imgspubs.add(camera.base64ForBitmap2(base641))
                imgspubs.add(camera.base64ForBitmap2(base642))
                imgspubs.add(camera.base64ForBitmap2(base643))
                imgspubs.add(camera.base64ForBitmap2(base644))


                Gallery.setAdapter(AdapterImgs(this,imgspubs)); */


            }
        }.start()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState) //padrão para o mapView
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart();//padrão para o mapView
    }
    override fun onStop() {
        super.onStop()
        mapView?.onStop();//padrão para o mapView
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()//padrão para o mapView
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()//padrão para o mapView
    }
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume();

        if(redesocial == "" || redesocial == "não informado"){
            button_redesocial.visibility = View.GONE
        }else{
            button_redesocial.setOnClickListener {
                browse(redesocial)
            }
        }



        //botao de avaliaçao
        buttonAvl.setOnClickListener {
            val idx = id
            //viewPubFragment(savedInstanceState)
            val intent = Intent(applicationContext, Tela_avaliacao_publicaoes_Activity::class.java)
            intent.putExtra("idpub", idx.toString())
            intent.putExtra("pesquisador", pesquisador)
            startActivity(intent)
        }

        var manipula_exibicao_mapa = 1
        mostramapa.setOnClickListener{
            if(manipula_exibicao_mapa == 1) {
                mapViewPub.visibility = View.VISIBLE
                llmapa.visibility = View.VISIBLE
                manipula_exibicao_mapa++
            }
            else{
                mapViewPub.visibility = View.GONE
                mapViewPub.visibility = View.GONE
                manipula_exibicao_mapa--
            }
        }

        var manipula_exibicao_infoPesquisador = 1
        mostrarinfo.setOnClickListener {
            if(manipula_exibicao_infoPesquisador == 1){
                infodepub.visibility = View.VISIBLE
                manipula_exibicao_infoPesquisador++
            }else{
                infodepub.visibility = View.GONE
                manipula_exibicao_infoPesquisador--
            }

        }


        mapViewPub.setOnClickListener {
            val intent = Intent(applicationContext, Configuracao_google_maps_Activity::class.java)
        }
    }


    fun setacor(categoria:String){
        var color = ""
        if(categoria == "ESCOLA" || categoria == "escola"){
            relativell.setBackgroundColor((Color.rgb(255, 255, 0)))//amarelho
            ll1.setBackgroundColor((Color.rgb(255, 255, 0)))
        }
        if(categoria == "TEATRO" || categoria == "teatro"){
            relativell.setBackgroundColor((Color.rgb(150, 97, 111)))//marrom
            ll1.setBackgroundColor((Color.rgb(150, 97, 111)))
        }
        if(categoria == "MUSEU" || categoria == "museu"){
            relativell.setBackgroundColor((Color.rgb(249, 251, 98)))//amarelo
            ll1.setBackgroundColor((Color.rgb(249, 251, 98)))
        }
        if(categoria == "PRAÇA" || categoria == "praça"){
            relativell.setBackgroundColor((Color.rgb(12, 231, 61)))//verde
            ll1.setBackgroundColor((Color.rgb(12, 231, 61)))
        }
        if(categoria == "FEIRA" || categoria == "feira"){
            relativell.setBackgroundColor((Color.rgb(0, 128, 255)))//azul
            ll1.setBackgroundColor((Color.rgb(0, 128, 255)))
        }
        if(categoria == "SAUDE" || categoria == "SAUDE"){
            relativell.setBackgroundColor((Color.rgb(249, 251, 98)))//amarelo
            ll1.setBackgroundColor((Color.rgb(249, 251, 98)))
        }
        if(categoria == "OUTRO" || categoria == "OUTRO" || categoria == "outro" || categoria == "outros"){
            relativell.setBackgroundColor((Color.rgb(145, 30, 213)))//roxo
            ll1.setBackgroundColor((Color.rgb(145, 30, 213)))
        }

    }
    // função obrigatoria para a extensão "OnMapReadyCallback" feita nessa atv para mostrar a coordenada no mapView
    override fun onMapReady(googleMap: GoogleMap?) {
        /*já mostra as coordenadas no mapView ao abrir a actividade, que foram passadas
        //pela Tela_listagem_pub_user_comum_Activity por parametro (essas coordenadas) foram adquiridas
        //automaticamente la na Prin.Act referente ao local atual da pessoa quando abre o app(para caso for add um publicação aq)*/
        mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)

        //aq só faz a atualização do marcador, dps que a pessoa confirma a atualização no mapa pela outra função responsavel, (quando clica no MapView)*/
        googleMap?.setOnMapClickListener(){
            mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)
        }
    }



    fun getinfopesquisador( extras:Bundle){
        representacao = extras.getString("representacao")
        cnpj = extras.getString("cnpj")
        anoinicio = extras.getString("anoinicio")
        recurso = extras.getString("recurso")
        pesquisador = extras.getString("pesquisador")
    }
    fun getinfouser(extras: Bundle){
        nome = extras.getString("nome")
        atvex = extras.getString("atvex")
        latitude = extras.getDouble("lat")
        longitude = extras.getDouble("log")
        redesocial = extras.getString("redesocial")
        endereco = extras.getString("endereco")
        contato = extras.getString("contato")
        categoria = extras.getString("categoria")
    }
}
