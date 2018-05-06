package com.example.carlos.projetocultural

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper

import com.google.android.gms.maps.OnMapReadyCallback


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import android.text.Html
import android.view.View
import android.widget.*
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.extensions.setupToolbar
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map_pub.*
import kotlinx.android.synthetic.main.content_avaliacao.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast


class MapPub : AppCompatActivity(), OnMapReadyCallback{

    //USE O CONTENT PARA CADA LOCAL, VC USA, O SE OE SENAO PARA USAR OU NAO USAR DETERMINADOS METODOS
    //private var mapFragment: SupportMapFragment? = null
    //var todoCursor: Cursor?=null
    //public var nometest:Array<String>? =null
    private lateinit var mMap: GoogleMap
    var marker : Marker ?= null
    var database: MyDatabaseOpenHelper?=null
    var mostrar:String ?=null
    var intentEnviaBroadcast:Intent?= null
    var semnet:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_pub)
        setSupportActionBar(toolbarmap)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        toolbarmap.setNavigationOnClickListener {
            finish()
        }
        //setSupportActionBar(toolbar)
        //Obtenha SupportMapFragment e receba notificações quando o mapa estiver pronto para ser usado.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        intentEnviaBroadcast = Intent(loc_receiver) //envia o intent para o broadcast
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        //botao para voltar(fechar a tela do mapa e retorna para atividade anterior)
     //   voltarMap.setOnClickListener(){finish()}
     }


    override fun onMapReady(mMap: GoogleMap) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        val mHashMap = HashMap<Marker, Int>()
        val mHashMapidpesq = HashMap<Marker, String>()
        // mMap.setMyLocationEnabled(true); //para localização atual
        database = MyDatabaseOpenHelper.getInstance(applicationContext)

        //pegas os dados enviados por intentes de outras atividades quando faz requisição dessa atividade
        val extras = intent.extras
        val longitude = extras.getStringArray("longitude")
        val latitude  = extras.getStringArray("latitude")
         val nome = extras.getStringArray("nome")
        val pesq = extras.getStringArray("pesq")
        val atvex = extras.getStringArray("atvex")
        val idpub = extras.getStringArray("idpub")
        mostrar = extras.getString("mostrar")   //determina se vai executar certas funções desse metodo (ex, colocamos uma cond. para editar o marcador, ou n),, depende dessa variavel
        semnet = extras.getString("semnet")
        //os valores passados ocmo long lat, e os demais, podem estar em um array, sendo assim pode conter mais de um valor
        //.. aq se pega o "tamanho do array" passado, para caso tiver, mostre mais marcações no mapa com a iteração
        val tam = longitude!!.size
        var i = 0;

        try {

            while (i < tam) {  //pega to_do o conteudo passado das coordenadas e coloca no mapa
                val sydney = LatLng(latitude?.get(i)!!.toDouble(), longitude[i].toDouble()) //coordenadas
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f));// trata o quão perto mostra a coordenada             //...mMap.animateCamera(CameraUpdateFactory.zoomTo(20f), 20000, null);
                val marker = mMap.addMarker(MarkerOptions().position(sydney).title(nome?.get(i)).snippet(atvex.get(i)).flat(true).rotation(245f))
                if(idpub != null) {
                    mHashMap.put(marker, idpub.get(i).toInt())
                    mHashMapidpesq.put(marker,pesq.get(i).toString())//verificar se é publicacao de pesquisador na hora que
                    //o usuario clicar e redirecionar para viewAct
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)) //esqueci
                //definees propriedades da posicao da camera
                val cameraPosition = CameraPosition.builder().target(sydney).zoom(13f).bearing(90f).build();
                // Animate the change in camera view over 2 seconds
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                marker?.showInfoWindow()
                marker?.hideInfoWindow()
                i++;
            }



            //para fazer as caixas de texto que mostra ao clicar, ou.., ou poder excluir uma marcação
            mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoContents(marker: Marker): View {
                    val tv = TextView(this@MapPub)//vermelho
                    tv.text = Html.fromHtml("<b><font color=\"#F44336\">" + marker.title + ":</font></b> " + marker.snippet)
                    return tv
                }
                override fun getInfoWindow(marker: Marker): View {

                    val ll = LinearLayout(this@MapPub)
                    ll.setPadding(10, 10, 10, 10)
                    ll.setBackgroundColor(Color.LTGRAY)
                    val tv = TextView(this@MapPub)//preto
                    tv.text = Html.fromHtml("<b><font color=\"#000000\">" + marker.title + ":</font></b> " + marker.snippet)
                    ll.addView(tv)

                    //pode confirmar e enviar a localização do marcador
                    if(mostrar == "atualiza") {
                        alert("Está localização está correta?") {
                            title = "Atenção"
                            negativeButton("Sim") {
                                val newLat = marker.position.latitude
                                val newLog = marker.position.longitude
                                intentEnviaBroadcast?.putExtra("latMap", newLat.toString())
                                intentEnviaBroadcast?.putExtra("logMap", newLog.toString())
                                //ATENTE QUE SO EXECUTA ESSE CODIGO SE A VAR 'MOSTRAR" ESTIVER SETADA COMO ATUALIZAE
                                sendBroadcast(intentEnviaBroadcast)  //AQ ENVIA VIA BROADCAST QUANDO O USUARIO MUDA A POSIÇÃO
                                toast("local confirmado")
                            }
                            positiveButton("Remarcar local") {
                                marker.remove()
                                toast("escolha outro local, tocando tela e segurando, a posição no mapa")
                            }
                        }.show()
                    }
                    return ll
                }
            })

            mMap.setOnCameraChangeListener { cameraPosition ->
                // Log.i("Scritp", "setOnCameraChangeListener")
                if (marker != null) {
                    marker?.remove()
                }
                //quando a camera se meche ele coloca um marcador!!! com o codigo abaixo
                /*var latlng = LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
                  mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13f));
                 mMap?.addMarker(MarkerOptions().position(latlng).title("carlos").snippet("informaçao adicional").flat(true).rotation(245f))
                 mMap?.moveCamera(CameraUpdateFactory.newLatLng(latlng)) */
            }

            if(mostrar == "atualiza") {
                mMap.setOnMapLongClickListener() { LatLng ->
                    Log.i("Script", "setOnMapClickListener()")
                    if (marker != null) {
                        marker?.remove()
                    }
                    val latlng = LatLng(LatLng.latitude, LatLng.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
                    mMap.addMarker(MarkerOptions().position(latlng).title("novo Local").snippet("clique em confirmar").flat(true)
                            .rotation(245f))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                }
            }

            //para quando vc clica no marcador
            mMap.setOnMarkerClickListener { marker ->
               // toast("clicado no marcador")
                false
            }
            val ipx = MainActivity().ipconfig
            //realizar algo quando toca na etiqueta (caixa de texto) sobre a marcação
            if(semnet != "sim" && mostrar != "atualiza") {//esta sem net
                mMap.setOnInfoWindowClickListener { marker ->

                    startActivity(intent)
                    toast("Seu local")
                    var id = ""
                    if (idpub != null) {
                        id = mHashMap.get(marker).toString()
                    }
                    val e_pesquisador = mHashMapidpesq.get(marker).toString()

                    val handle = Handler()
                    var url:String = ""
                    val intent = Intent(applicationContext, actViewPub::class.java)
                    var t= mHashMapidpesq.get(marker).toString()
                    intent.putExtra("e_pesquisador", mHashMapidpesq.get(marker).toString())
                    intent.putExtra("vemdomappubOUvemdohomeactivity", "mappub")
                    try {
                        if(e_pesquisador != "0"){
                            url = "http://orbeapp.com/web/sendpubpesq/$id?_format=json&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,anoinicio,cnpj,representacao,recurso,latitude,longitude,pesquisador,campo1,campo2,campo3,campo4,campo5"
                            Thread {
                                val pubpesq = pubService.getPubpesqparasoUmresultado(url)
                                handle.post {
                                    intent.putExtra("idpub", id)
                                    intent.putExtra("nome", marker.title.toString())
                                    intent.putExtra("atvex", marker.snippet.toString())
                                    intent.putExtra("lat",pubpesq.latitude.toDouble())
                                    intent.putExtra("log",pubpesq.longitude?.toDouble())
                                    intent.putExtra("redesocial",pubpesq.redesocial)
                                    intent.putExtra("endereco",pubpesq.endereco)
                                    intent.putExtra("contato",pubpesq.contato)
                                    intent.putExtra("categoria",pubpesq.categoria)
                                    intent.putExtra("representacao",pubpesq.representacao)
                                    intent.putExtra("cnpj",pubpesq.cnpj)
                                    intent.putExtra("anoinicio",pubpesq.anoinicio)
                                    intent.putExtra("recurso",pubpesq.recurso)
                                    intent.putExtra("campo1",pubpesq.campo1)
                                    intent.putExtra("campo2",pubpesq.campo2)
                                    intent.putExtra("campo3",pubpesq.campo3)
                                    intent.putExtra("campo4",pubpesq.campo4)
                                    intent.putExtra("campo5",pubpesq.campo5)
                                    intent.putExtra("pesquisador",pubpesq.pesquisador.toString())
                                    startActivity(intent)
                                }
                            }.start()
                        }else{
                            url = "http://orbeapp.com/web/sendpubuser/$id?_format=json&fields=id,nome,redesocial,endereco,contato,email,atvexercida,categoria,latitude,longitude"
                            Thread {
                                val pubuser = pubService.getPubuserparasoUmresultado(url)
                                handle.post {
                                    intent.putExtra("idpub", id)
                                    intent.putExtra("nome", pubuser.nome.toString())
                                    intent.putExtra("atvex", marker.snippet.toString())
                                    intent.putExtra("lat",pubuser.latitude.toDouble())
                                    intent.putExtra("log",pubuser.longitude?.toDouble())
                                    intent.putExtra("redesocial",pubuser.redesocial)
                                    intent.putExtra("endereco",pubuser.endereco)
                                    intent.putExtra("contato",pubuser.contato)
                                    intent.putExtra("categoria",pubuser.categoria)
                                    startActivity(intent)
                                }
                            }.start()
                        }
                    }catch (e:Exception){
                        toast("error, tente novamente")
                    }

                }
            }else{
                toast("")
                //vizualiza suas pubs e pesquisas salvas no banco de dados
            }

        }catch (e:Exception){
            Toast.makeText(applicationContext, "publicação sem cordenadas", Toast.LENGTH_SHORT).show()
        }
    }



    public fun atualizaCoordenadasNoMapView(googleMap: GoogleMap?,latitude :Double?,longitude:Double?,title: String, subtexto:String){
        val latlng = LatLng(latitude!!.toDouble(),longitude!!.toDouble())
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
        googleMap?.addMarker(MarkerOptions().position(latlng).title(title).snippet(subtexto).flat(true)
                .rotation(245f))
    }

    companion object {
        val loc_receiver = "APPMAP"
    }

    // nao utilizado pq n deu certo
    public fun customAddMarker(latlng: LatLng, title:String,snippet:String){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13f));
        mMap.addMarker(MarkerOptions().position(latlng).title("carlos").snippet("informaçao adicional").flat(true)
                .rotation(245f))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
    }
}
