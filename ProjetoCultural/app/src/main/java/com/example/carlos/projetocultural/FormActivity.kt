package com.example.carlos.projetocultural

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor


import org.jetbrains.anko.db.*

import android.graphics.Bitmap
import android.graphics.Camera
import android.location.Location
import android.media.Image
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.PubpesqService
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.PubuserService
import com.example.carlos.projetocultural.extensions.toast

import com.example.carlos.projetocultural.utils.CameraHelper
import com.example.carlos.projetocultural.utils.ActionsForMaps
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.example.carlos.projetocultural.utils.Validacpf
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import kotlinx.android.synthetic.main.list_row_main.*
import org.jetbrains.anko.act
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*
import java.util.Arrays.asList



//PESQUISADOR AONDE ADICIONA
class FormActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , OnMapReadyCallback, AdapterView.OnItemSelectedListener  {

    val camera = CameraHelper()
   // var database: MyDatabaseOpenHelper?=null //para usar o BD
    var googleApiClient : GoogleApiClient ?= null
    var latitude: Double ?= 0.0//variaveis para as coordenadas
    var longitude: Double ?= 0.0
    var permission = false  //variavel para verificar permissão
    val REQUEST_PESMISSION_GPS=1 //para GPS
    val mappub = MapPub()
    var mapView : MapView?=null
    var numImgx:Int ?= 0
    var idpesquisador:String="0"
    var pubpesq= Pubpesq()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        setContentView(R.layout.fragment_add)

        //para identificar o pesquisador logado, que vai ser amndado da outra act
        val extras = intent.extras
        idpesquisador = extras.getString("idpesq")
        if(idpesquisador == "0" || idpesquisador == ""){
            toast("problema de pesquisador")
        }

        //como aq é publicacao de pesquisador, abilita os campos adicionais
        anoinicioa.visibility = View.VISIBLE
        cnpja.visibility = View.VISIBLE
        representacaoa.visibility = View.VISIBLE
        recursoa.visibility = View.VISIBLE
        buttonAddMapa.visibility = View.GONE
        layoutaddcampos.visibility = View.VISIBLE
        //para o googlemap
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        // Spinner click listener, que pega a lista do arquivo 'strings'
        val spinner=findViewById<Spinner>(R.id.spinneradd) as Spinner
        val adapter = ArrayAdapter.createFromResource(this,
        R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //recebe a instancia do "obj" mapView que se encontra no layout desse fragment
        mapView = findViewById<MapView>(R.id.figMapAdda) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString();
        spinneradd.setOnItemSelectedListener(this);
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState) //padrão para o mapView
    }
    override fun onStop() {
        super.onStop()
       // googleApiClient?.disconnect()
        try {
            unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
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
        try {
            unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        super.onPause()
        mapView?.onPause()

    }
    //para estarta a função onConnected quando abrir a atv para assim pegar as coordenadas
    override fun onStart() {
        super.onStart()
        if (googleApiClient != null) {
            googleApiClient?.connect()
        }
        mapView?.onStart();//padrão para o mapView
    }
    //função para conectar no serviço do GPS para obter a posição atual do usuario(lat e long)
    //CONECTADO AO GOOGLE PLAY SERVICE, PODEMOS USAR QUALQUER API AGORA
    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //verifica se o gps ta ligado, se n tiver vai da problema e cair no catch
            val provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider == null || provider.length == 0) {
                toast("gps desabilitado")
            } else {
                try {//pega a latitude e longitude atual da pessoa para assim enviar quando for adicionar uma publicação já tiver os dados
                    val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    latitude = lastLocation.latitude
                    longitude = lastLocation.longitude
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Conexão com gps falha", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }else{
            toast("Permissão para GPS negada!")
        }
    }




    //broadcast vai ser usado quando ativar o serviço (que foi ativado no oncrate para receber as coordenadas do usuario na hora que estarta essa atividade)
    //OBSERVAÇÃO::::::::::::;; verificar o unreceiver no onPause
    //atualiza as variaveis com as coordenadas novas, selecionadas pelo usuario la no atv MapPub
    inner class broadcastReceive:BroadcastReceiver(){
        override fun onReceive(context: Context?,intent: Intent?){
            latitude = intent?.getStringExtra("latMap")?.toDouble()
            longitude = intent?.getStringExtra("logMap")?.toDouble()
            //          context?.unregisterReceiver(broadcastReceive())
        }
    }
    //para verificação de permissão de acesso ao GPS
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      /*  when(requestCode){
            REQUEST_PESMISSION_GPS -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permission=true
                }
            }
        }*/
        for (result in grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                //A;GUMA PERMISSÃO FOI NEGADA
                alertAndFinish()
                return
            }
        }
    }


    private fun alertAndFinish(){
        run{
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name).setMessage("para utilizar este aplicativo, você precisa aceitas" +
                    "as permissões.")
            //add os butoes
            builder.setPositiveButton("OK"){
                dialog, which -> finish()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }


    //ERRO NA CONEXÃO, PODE SER UMA CONFIGURAÇÃO INVÁLIDA OU FALTA DE CONECTIVIDADE NO DISPOSITIVO
    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("falha na conexão do mapa")
        toast("PODE SER UMA CONFIGURAÇÃO INVÁLIDA OU FALTA DE CONECTIVIDADE NO DISPOSITIVO")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //A CONEXÃO FOI INTERROPMIDA
    // A APLICAÇÃO PRECISA AGUARDAR ATÉ A CONEXÃO SER RESTABELECIDA
    override fun onConnectionSuspended(p0: Int) {
        toast("conexão suspensa")
        toast("A APLICAÇÃO PRECISA AGUARDAR ATÉ A CONEXÃO SER RESTABELECIDA")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    var map:GoogleMap?=null
    // função obrigatoria para a extensão "OnMapReadyCallback" feita nessa atv para mostrar a coordenada no mapView
    override fun onMapReady(googleMap: GoogleMap?) {
        /*já mostra as coordenadas no mapView ao abrir a actividade, que foram passadas
        //pela PrincipalActivity por parametro (essas coordenadas) foram adquiridas
        //automaticamente la na Prin.Act referente ao local atual da pessoa quando abre o app(para caso for add um publicação aq)*/
         mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,"NOVA PUB","SEU LOCAL")

        //aq só faz a atualização do marcador, dps que a pessoa confirma a atualização no mapa pela outra função responsavel, (quando clica no MapView)*/
        googleMap?.setOnMapClickListener(){
               mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,"NOVA PUB","SEU LOCAL")
        }
    }


    override fun onResume() {
        super.onResume()
        var controla_button_foto = 1
        addfoto.setOnClickListener {
            when(controla_button_foto) {
                1 -> {
                    llcunjuntoimg1and2.visibility = View.VISIBLE
                    llcunjuntoimg3nd4.visibility = View.VISIBLE
                    controla_button_foto++
                }
                2 -> {
                    llcunjuntoimg1and2.visibility = View.GONE
                    llcunjuntoimg3nd4.visibility = View.GONE
                    controla_button_foto--
                }
            }
        }

        /*fica esperando o brosCast quando a função que atualiza a coordenada no mapView na fragment e usada, e retorna
        //as novas coordenadas que o usuario selecionou no mapa*/
        try {
            unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }

        registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))
        //padrao

        try {
            unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        mapView?.onResume();

        val act = this
        val img1a = findViewById<ImageView>(R.id.img1a) as ImageView
        val img2a = findViewById<ImageView>(R.id.img2a) as ImageView
        val img3a = findViewById<ImageView>(R.id.img3a) as ImageView
        val img4a = findViewById<ImageView>(R.id.img4a) as ImageView
        img1a.setOnClickListener {
            camera.tirafoto(1,act,null,img1a,img2a,img3a,img4a)
            numImgx = 1
        }
        img2a.setOnClickListener {
            camera.tirafoto(2,act,null,img1a,img2a,img3a,img4a)
            numImgx = 2
        }
        img3a.setOnClickListener {
            camera.tirafoto(3,act,null,img1a,img2a,img3a,img4a)
            numImgx = 3
        }
        img4a.setOnClickListener {
            camera.tirafoto(4,act,null,img1a,img2a,img3a,img4a)
            numImgx = 4
        }

        buttonSalvara.setOnClickListener {
            onClickCreate()
        }

        var controla_button_map = 1
        addmapadd.setOnClickListener {
            when(controla_button_map) {
                1 -> {
                    mostramapaadd.visibility = View.VISIBLE
                    figMapAdda.visibility = View.VISIBLE
                    buttonAddMapa.visibility = View.VISIBLE
                    controla_button_map++
                }
                2 -> {
                    mostramapaadd.visibility = View.GONE
                    figMapAdda.visibility = View.GONE
                    buttonAddMapa.visibility = View.GONE
                    controla_button_map--
                }
            }
        }



        var addcampo = 1
        //para esconder todos os campos perguntas respostas;

        removecampolr.setOnClickListener {
            campo1layoutlr.visibility = View.GONE
            campo2layoutlr.visibility = View.GONE
            campo3layoutr.visibility = View.GONE
            campo4layoutlr.visibility = View.GONE
            campo5layoutlr.visibility = View.GONE
            campo6ayoutlr.visibility = View.GONE
            campo7layoutlr.visibility = View.GONE
            campo8layoutlr.visibility = View.GONE
            campo9layoutlr.visibility = View.GONE
            campo10layoutlr.visibility = View.GONE
            /*campo11layoutlr.visibility = View.GONE
            campo12layoutlr.visibility = View.GONE
            campo13layoutlr.visibility = View.GONE
            campo14layoutlr.visibility = View.GONE
            campo15layoutlr.visibility = View.GONE
            campo16layoutlr.visibility = View.GONE
            campo17layoutlr.visibility = View.GONE
            campo18layoutlr.visibility = View.GONE
            campo19layoutlr.visibility = View.GONE
            campo20layoutlr.visibility = View.GONE*/
            addcampo= 1
        }


        addcampolr.setOnClickListener {
            when(addcampo){
                1 -> {
                    campo1layoutlr.visibility = View.VISIBLE
                    campo2layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                2 -> {
                    campo3layoutr.visibility = View.VISIBLE
                    campo4layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                3 -> {
                    campo5layoutlr.visibility = View.VISIBLE
                    campo6ayoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                4 -> {
                    campo7layoutlr.visibility = View.VISIBLE
                    campo8layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                5 -> {
                    campo9layoutlr.visibility = View.VISIBLE
                    campo10layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
               /* 6 -> {
                    campo11layoutlr.visibility = View.VISIBLE
                    campo12layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                7 -> {
                    campo13layoutlr.visibility = View.VISIBLE
                    campo14layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                8 -> {
                    campo15layoutlr.visibility = View.VISIBLE
                    campo16layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                9 -> {
                    campo17layoutlr.visibility = View.VISIBLE
                    campo18layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                10 -> {
                    campo19layoutlr.visibility = View.VISIBLE
                    campo20layoutlr.visibility = View.VISIBLE
                    addcampo++
                }*/
            }

        }


        /*vizualizar localização da publicação no mapa, e da a opção de vc adicionar no mapa outra coordenada caso o usuario n esteja no local
        //.. da publicacao, quando for adicionar a mesma*/
        buttonAddMapa.setOnClickListener(){
            //envia a localização da onde a pessoa esta para o MapPub e da a opção caso ela queira mudar o marcador
            val intent = Intent(applicationContext, MapPub::class.java)
            //val locations:MutableList<String> = mutableListOf(longitude.toString(),latitude.toString())
            val longitudex:List<String> ?= mutableListOf(longitude.toString())
            val latitudex:List<String> ?= mutableListOf(latitude.toString())
            val nomex:List<String> ?= mutableListOf("nova localização")
            val atvex:List<String> ?= mutableListOf("nova publicação")
            intent.putExtra("mostrar","atualiza")//para enviar os dados via intent
            intent.putExtra("longitude",longitudex?.toTypedArray())
            intent.putExtra("latitude",latitudex?.toTypedArray())
            intent.putExtra("nome",nomex?.toTypedArray())
            intent.putExtra("atvex",atvex?.toTypedArray())
            startActivity(intent)//estarta a atividade MapPub que mostra o mapa e da opcao da pessoa troca o marcador
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@FormActivity, PesquisadorhomeActivity::class.java)
        intent.putExtra("idpesq",idpesquisador)
        startActivity(intent)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        camera.supportForOnActivityResult(applicationContext,pubpesq,null,data,resultCode,requestCode,"add")//0 pq só vai ter que passar o valor do id, se for em caso de update/alterar o dado, e add, pq quando é no caso tbm de alteração..
        //precisa mudar o caminho na classe camera!
    }



   // var database:MyDatabaseOpenHelper?=null
    //tem como vc chamar a funcao savePubpesq la da camera...
    fun onClickCreate(){
//val tees = nomea.text.toString()
       // val idpesquisadorx2 = logininfo.singleton
       pubpesq.nome = nomea.text.toString()
       pubpesq.redesocial = redesociala.text.toString()
       pubpesq.endereco = enderecoa.text.toString()
       pubpesq.contato = contatoa.text.toString()
       pubpesq.email = emaila.text.toString()
       pubpesq.atvexercida = atvexa.text.toString()
       pubpesq.categoria = spinneradd.selectedItem.toString()
       pubpesq.anoinicio = anoinicio.text.toString()
       pubpesq.cnpj = cnpj.text.toString()
       pubpesq.representacao = representacao.text.toString()
       pubpesq.recurso = recurso.text.toString()

       //val aprovado = categoriaa.aprovadoa.toString()
       pubpesq.campo1 = campo1a.text.toString()
       pubpesq.campo2 = campo2a.text.toString()
       pubpesq.campo3 = campo3a.text.toString()
       pubpesq.campo4 = campo4a.text.toString()
       pubpesq.campo5 = campo5a.text.toString()
       pubpesq.campo6 = campo6a.text.toString()
       pubpesq.campo7 = campo7a.text.toString()
       pubpesq.campo8 = campo8a.text.toString()
       pubpesq.campo9 = campo9a.text.toString()
       pubpesq.campo10 = campo10a.text.toString()


       pubpesq.campo11 = campo11a.text.toString()
       pubpesq.campo12 = campo12a.text.toString()
       pubpesq.campo13 = campo13a.text.toString()
       pubpesq.campo14 = campo14a.text.toString()
       pubpesq.campo15 = campo15a.text.toString()

       pubpesq.campo16 = campo16a.text.toString()
       pubpesq.campo17 = campo17a.text.toString()
       pubpesq.campo18 = campo18a.text.toString()
       pubpesq.campo19 = campo19a.text.toString()
       pubpesq.campo20 = campo20a.text.toString()


               // val tes=   pubpesq?.img1
       pubpesq.longitude = longitude.toString()
       pubpesq.latitude = latitude.toString()
       if (pubpesq.latitude != "0.0") {
           val handle = Handler()
           Thread {
               val tes = PubpesqService.salvar(pubpesq)
               handle.post {
                   toast("salvo")
                   val intent = Intent(this@FormActivity, ListviewpubpesqActivity::class.java)
                   intent.putExtra("idpesq", idpesquisador)
                   startActivity(intent)
                   finish()
               }
           }.start()
       } else {
           toast("localização indisponivel, ligue o gps")
       }
   }

}
