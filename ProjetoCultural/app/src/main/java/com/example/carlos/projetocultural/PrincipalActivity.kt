package com.example.carlos.projetocultural

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.widget.Toast
import com.example.carlos.projetocultural.adapters.PubuserAdapter
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.PubuserService
import com.example.carlos.projetocultural.domain.event.SaveCarroEvent
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.common.ConnectionResult
import kotlinx.android.synthetic.main.activity_principal.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.content_principal.*
//import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.*
import org.json.JSONObject

//CUIDA DAS PUBLICAÇÕES DO USUARIO (AONDE SALVA ELA, EDITA ELAS)
class PrincipalActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    var permission = false  //variavel para verificar permissão
    val REQUEST_PESMISSION_GPS=1 //para GPS
    var googleApiClient : GoogleApiClient ?= null //..
    var latitude: Double = 0.0//variaveis para as coordenadas
    var longitude: Double = 0.0

    val camera =CameraHelper()

    val pubuser = Pubuser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@PrincipalActivity, MainActivity::class.java)
            intent.putExtra("sem splash","0")
            startActivity(intent)
        }

         FAB_Principal.setOnClickListener {
                addFragment(applicationContext) //chama a função addFragment(que cuida da insersão de uma publicação)

        }
        recyclerViewc.layoutManager = LinearLayoutManager(applicationContext)
        //recyclerViewc.itemAnimator = DefaultItemAnimator()
        recyclerViewc.setHasFixedSize(true)


        taskCarros()
        googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()

    }
    protected var carros = listOf<Pubuser>()


    open fun taskCarros() {
        Thread{
            carros = PubuserService.getPubuser()
            runOnUiThread {
                recyclerViewc.adapter = PubuserAdapter(carros, {onClickCarro(it)},{SendServer(it)})
            }
        }.start()
    }



    open fun onClickCarro(carro: Pubuser) {
        val ft= supportFragmentManager.beginTransaction()
        val fragAnterior = supportFragmentManager.findFragmentByTag(OperacaoFragment.KEY2)
        if (fragAnterior != null) {
            ft.remove(fragAnterior)
        }
        ft.addToBackStack(null)
        val bundle= Bundle()//bundle para passagem de parametros(dados a baixo) para outra atv
        bundle.putString("categorialr",carro.categoria)
        bundle.putInt("idx",carro.id)
        val dialog = OperacaoFragment()//estancia o fragment para passar os dados do bundle
        dialog.arguments= bundle
        dialog.show(ft, OperacaoFragment.KEY2)//chama o fragment
    }

    //para estarta a função onConnected quando abrir a atv para assim pegar as coordenadas
    override fun onStart() {
        super.onStart()
        if (googleApiClient != null) {
            googleApiClient?.connect()
        }
    }

    //função para conectar no serviço do GPS para obter a posição atual do usuario(lat e long)
    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //verifica se o gps ta ligado, se n tiver vai da problema e cair no catch
            val provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider == null || provider.length == 0) {
                toast("gps desabilitado")
                finish()
            } else {
                try {//pega a latitude e longitude atual da pessoa para assim enviar quando for adicionar uma publicação já tiver os dados
                    val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    latitude = lastLocation.latitude //
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
    public inner class broadcastReceiver: BroadcastReceiver(){
        public var newLat:String?=null
        public var newLog:String?=null
        override fun onReceive(context: Context?,intent: Intent?){
            newLat = intent?.getStringExtra("latMap")//foi passado esse intent que esta sendo recebido aq na MapPub activity
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
        if(provider == null || provider.length == 0){
            toast("gps desabilitado")
        }else {
            val ft = supportFragmentManager.beginTransaction()
            val fragAnterior = supportFragmentManager.findFragmentByTag(AddFragment.KEY)
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
            onConnected(bundle) // ativa a função onConnecte para atualizar os dados da localização do usuario
            bundle.putDouble("latitude", latitude)//pega os dados da localização
            bundle.putDouble("longitude", longitude)
            bundle.putInt("height", height)//pega os daddos da tela
            bundle.putInt("width", width)
            ft.addToBackStack(null)//padrao, adicionado a atv a pilha do android
            val dialog = AddFragment()//estancia o fragment que sera chamado numa variavel para passar o bundle como parametro
            dialog.arguments = bundle// passa o bundle como parametro
            dialog.show(ft, AddFragment.KEY) //chama o add fragment para ser vizualizado paara o usuario.
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
            val dialog = ProgressDialog.show(this, "Um momento", "Enviando sua publicação", false, true)
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
            }catch (e:Exception){
                dialog.dismiss()
                toast("falha no envio tentar novamente")
            }

        }else{
            toast("sem conexão")
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
