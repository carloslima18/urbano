package com.example.carlos.projetocultural.usuario_comum

import android.Manifest
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics

import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.adapters.Tela_listagem_pub_user_comum_Adapter
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.PubuserService
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.example.carlos.projetocultural.utils.OperacoesEconfiguracoesCameraImagemCameraHelper
import kotlinx.android.synthetic.main.activity_tela_listagem_pub_user_comum.*
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.content_tela_listagem_pub_user_comum.*
//import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.*
import android.content.IntentSender
import com.example.carlos.projetocultural.MainActivity
import com.google.android.gms.common.api.ResolvableApiException



//CUIDA DAS PUBLICAÇÕES DO USUARIO (AONDE SALVA ELA, EDITA ELAS)
class Tela_listagem_pub_user_comum_Activity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks{
    var permission = false  //variavel para verificar permissão
    val REQUEST_PESMISSION_GPS=1 //para GPS
    var googleApiClient : GoogleApiClient ?= null //..
    var latitude: Double = 0.0//variaveis para as coordenadas
    var longitude: Double = 0.0


    private var locationManager : LocationManager? = null

    var avisa_gps_ativo_para_add_pub = null
    val camera =OperacoesEconfiguracoesCameraImagemCameraHelper()
    val pubuser = Pubuser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_listagem_pub_user_comum)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        avisa_gps_ativo_para_add_pub!= false
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@Tela_listagem_pub_user_comum_Activity, MainActivity::class.java)
            intent.putExtra("sem splash","0")
            startActivity(intent)
        }

         FAB_Principal.setOnClickListener {
                addFragment(applicationContext) //chama a função addFragment(que cuida da insersão de uma publicação)

        }
        recyclerViewc.layoutManager = LinearLayoutManager(applicationContext)
        //recyclerViewc.itemAnimator = DefaultItemAnimator()
        recyclerViewc.setHasFixedSize(true)
        GetPermission()
        taskCarros()
        //googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()



        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;

        try {
            // Solicitar atualizações de localização
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
        } catch(ex: SecurityException) {
           toast("erro de gps");
        }
    }
    protected var carros = listOf<Pubuser>()




    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //toast("" + location.longitude + ":" + location.latitude)
            //toast("Lozalização encontrada")
            latitude = location.latitude
            longitude = location.longitude
            //thetext.setText("" + location.longitude + ":" + location.latitude);
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    open fun taskCarros() {
        Thread{
            carros = PubuserService.getPubuser()
            runOnUiThread {
                recyclerViewc.adapter = Tela_listagem_pub_user_comum_Adapter(carros, {onClickCarro(it)},{SendServer(it)})
            }
        }.start()
    }



    open fun onClickCarro(carro: Pubuser) {
        val ft= supportFragmentManager.beginTransaction()
        val fragAnterior = supportFragmentManager.findFragmentByTag(Tela_altera_e_exclui_pub_user_comum_Fragment.KEY2)
        if (fragAnterior != null) {
            ft.remove(fragAnterior)
        }
        ft.addToBackStack(null)
        val bundle= Bundle()//bundle para passagem de parametros(dados a baixo) para outra atv
        bundle.putString("categorialr",carro.categoria)
        bundle.putInt("idx",carro.id)
        val dialog = Tela_altera_e_exclui_pub_user_comum_Fragment()//estancia o fragment para passar os dados do bundle
        dialog.arguments= bundle
        dialog.show(ft, Tela_altera_e_exclui_pub_user_comum_Fragment.KEY2)//chama o fragment
    }

    //para estarta a função onConnected quando abrir a atv para assim pegar as coordenadas
    override fun onStart() {
        super.onStart()
        //if (googleApiClient != null) {
       //     googleApiClient?.connect()
        //}
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /*função para conectar no serviço do GPS para obter a posição atual do usuario(lat e long)
    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //verifica se o gps ta ligado, se n tiver vai da problema e cair no catch
            val provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider == null || provider.length == 0) {
                toast("Ligue seu GPS")
                finish()
            } else {
                  //googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()

              /*   try {//pega a latitude e longitude atual da pessoa para assim enviar quando for adicionar uma publicação já tiver os dados
                    val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                     latitude = lastLocation.latitude //
                    longitude = lastLocation.longitude
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Conexão com gps falha", Toast.LENGTH_SHORT).show()
                    finish()
                }*/
            }
        }else{
            toast("Permissão para GPS negada!")
        }
    }*/

    //broadcast vai ser usado quando ativar o serviço (que foi ativado no oncrate para receber as coordenadas do usuario na hora que estarta essa atividade)
    public inner class broadcastReceiver: BroadcastReceiver(){
        public var newLat:String?=null
        public var newLog:String?=null
        override fun onReceive(context: Context?,intent: Intent?){
            newLat = intent?.getStringExtra("latMap")//foi passado esse intent que esta sendo recebido aq na Configuracao_google_maps_Activity activity
            newLog = intent?.getStringExtra("logMap")
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }

    //para verificação de permissão de acesso ao GPS
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PESMISSION_GPS -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permission=true
                }
            }
        }
    }



    //chama o fragment de adicionar a publicação
    private fun addFragment(context: Context){
        val provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider == null || provider.length == 0 || latitude == 0.0 || longitude == 0.0){//||
            toast("Veririque se o GPS está ativo, Aguarde seu posicionamento e tente novamente")

        }else {
            val ft = supportFragmentManager.beginTransaction()
            val fragAnterior = supportFragmentManager.findFragmentByTag(Tela_add_pub_user_comum_Fragment.KEY)
            if (fragAnterior != null) {
                ft.remove(fragAnterior)
            }

            // para pegar a altura e largura do dispositivo em relação a tela para estabelecer alt e larg na tela do fragment de addPublicação
            val metrics = DisplayMetrics()
            var windowmag = context.getSystemService(Context.WINDOW_SERVICE)//não sei para que eu coloquei isso aq
            windowManager.getDefaultDisplay().getMetrics(metrics);//tbm para pegar o tam da tela
            val height = metrics.heightPixels;//pega o tamanho em pixeis
            val width = metrics.widthPixels;

            val bundle = Bundle()//cria um bundle para enviar os dados necessarios para o fragment
            //onConnected(bundle ) // ativa a função onConnecte para atualizar os dados da localização do usuario
            bundle.putDouble("latitude", latitude)//pega os dados da localização
            bundle.putDouble("longitude", longitude)
            bundle.putInt("height", height)//pega os daddos da tela
            bundle.putInt("width", width)
            ft.addToBackStack(null)//padrao, adicionado a atv a pilha do android
            val dialog = Tela_add_pub_user_comum_Fragment()//estancia o fragment que sera chamado numa variavel para passar o bundle como parametro
            dialog.arguments = bundle// passa o bundle como parametro
            dialog.show(ft, Tela_add_pub_user_comum_Fragment.KEY) //chama o add fragment para ser vizualizado paara o usuario.
        }
    }



    //essa função cuida de envia os dados da publicação salva no banco de dados da aplicação, para o webService
    //acionada quando clica e segura na publiação estando na listView
    fun SendServer(pubuser: Pubuser): Boolean{
        act.alert("Todos dados corretos?") {

            title = "Enviar publicação"
            positiveButton("Enviar") {
                Enviadados(pubuser)
            }
            negativeButton("Cancelar"){ // botão para caso a pessoa apertar em cancelar o envio da publicação
                toast("Envio cancelado")
            }
        }.show()
        return true
    }


    fun Enviadados(pubuser:Pubuser){
        if(AndroidUtils.isNetworkAvailable(applicationContext)) {
            if(pubuser.img1 != "" && pubuser.img2 != "" && pubuser.img3 != "" && pubuser.img4 != "" && pubuser.nome != "" &&
                    pubuser.categoria != "" && pubuser.latitude != "" && pubuser.longitude != "") {
                val dialog = ProgressDialog.show( this, "Um momento", "Enviando sua publicação", false, true)
                dialog.setCancelable(false);
                try {
                    Thread {
                        pubuser.img1 = camera.uriForBase64(this, pubuser.img1)
                        pubuser.img2 = camera.uriForBase64(this, pubuser.img2)
                        pubuser.img3 = camera.uriForBase64(this, pubuser.img3)
                        pubuser.img4 = camera.uriForBase64(this, pubuser.img4)
                        pubService.savePubuserclass(pubuser)
                        PubuserService.deletepubuser(pubuser)
                        taskCarros()
                        runOnUiThread {
                            dialog.dismiss()
                            toast("publicação enviada")
                        }
                    }.start()
                } catch (e: Exception) {
                    dialog.dismiss()
                    toast("falha no envio tentar novamente")
                }
            }else{
                toast("Campos ou fotos nulos")
            }


        }else{
            toast("sem conexão")
        }
    }



    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    val MY_PERMISSIONS_REQUEST_PHONE_CALL = 1
    public fun GetPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 10)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.MANAGE_DOCUMENTS), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
    }
}
