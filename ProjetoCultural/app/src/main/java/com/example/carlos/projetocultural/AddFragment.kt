package com.example.carlos.projetocultural
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v4.app.DialogFragment
//import kotlinx.android.synthetic.main.content_add.*
import android.provider.MediaStore
import android.net.Uri
import android.os.Environment
import org.jetbrains.anko.alert
import java.io.File
import kotlinx.android.synthetic.main.fragment_add.*
import android.support.v4.content.FileProvider
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import android.util.Base64
import android.widget.*
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.PubuserService
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.Validacpf
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URI

//import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;

import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.content_vizualizapesq.*
import org.jetbrains.anko.toast

// e talvez você precisa em alguns ocations
/**
 * A fragment with a Google +1 button.
 */
// fragment para adicionar uma publicação
class AddFragment() : DialogFragment(), OnMapReadyCallback , AdapterView.OnItemSelectedListener {

    val MY_PERMISSIONS_REQUEST = 3 // para permissões
    var base64_1:String?=null  //variaveis que recebe a string BASE64 das imagens para..
    var base64_2:String?=null  //..salvar no banco de dados
    var base64_3:String?=null
    var base64_4:String?=null
    private val REQUEST_IMAGE_CODE = 1888; //variaveis obrigatorias para parametro da funcao "startActivityForResult"
    val PICK_FROM_FILE = 2;     //para tirar foto, ou importa da galeria
    var latitude:Double?= null // variaveis para pegar via intentPut extra a long e lat enviadas..
    var longitude: Double?= null //..pela PrincipalActivity
    var mapView : MapView?=null //variavel para amostragem do mapa no neste fragment
    val camera = CameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)
    var numImgx:Int= 0 //variavel para identificar qual imageView que o usuario clicou la no layout
    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)

    val mappub = MapPub()
    //create dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    //infla a view! joga na tela o fragment para o usuario
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        retainInstance//para salvar a instancia do fragment caso a tela seja virada
        val view = inflater.inflate(R.layout.fragment_add, container, false)



        // Spinner element
       val spinner = view?.findViewById<Spinner>(R.id.spinneradd) as Spinner
        //  var button = view?.findViewById<Button>(R.id.button)
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        val categories = ArrayList<String>();
        categories.add("ESCOLA");
        categories.add("PRAÇA");
        categories.add("MUSEU");
        categories.add("TEATRO");
        categories.add("FEIRA");
        categories.add("OUTRO");
        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(dataAdapter);



        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrincipalActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrincipalActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrincipalActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }



        //recebe os dados via bundle da PrincipalActivity
        latitude = arguments?.getDouble("latitude")
        longitude = arguments?.getDouble("longitude")

        //recebe a instancia do "obj" mapView que se encontra no layout desse fragment
        mapView = view.findViewById<MapView>(R.id.figMapAdda) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)




        //retorna a view com os dados adquiridos
        return view
    }



    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
         // On selecting a spinner item
        val item = parent?.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent?.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        spinneradd.setOnItemSelectedListener(this);

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState) //padrão para o mapView
    }
    override fun onStart() {
        super.onStart()
        mapView?.onStart();//padrão para o mapView
    }
    override fun onStop() {
        super.onStop()
        try {
            context?.unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        mapView?.onStop();//padrão para o mapView
    }
    override fun onDestroy() {

        try {
            if (broadcastReceive() != null)
                context?.unregisterReceiver(broadcastReceive())

        } catch (e: Exception) {
        }

        super.onDestroy()
        mapView?.onDestroy()//padrão para o mapView
    }





    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()//padrão para o mapView
    }
    override fun onPause() {
        try {
            context?.unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        super.onPause()
        mapView?.onPause()

    }

    override fun onResume() {
        super.onResume()
        layoutaddcampos.visibility = View.GONE


        buttonAddMapa.visibility = View.GONE


        val act = activity as PrincipalActivity
        val fragment = this@AddFragment
        img1a.setOnClickListener {
            camera.tirafoto(1,act,fragment,img1a,img2a,img3a,img4a)
            numImgx = 1
        }
        img2a.setOnClickListener {
            camera.tirafoto(2,act,fragment,img1a,img2a,img3a,img4a)
            numImgx = 2
        }
        img3a.setOnClickListener {
            camera.tirafoto(3,act,fragment,img1a,img2a,img3a,img4a)
            numImgx = 3
        }
        img4a.setOnClickListener {
            camera.tirafoto(4,act,fragment,img1a,img2a,img3a,img4a)
            numImgx = 4
        }

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

        var controla_button_map = 1
        addmapadd.setOnClickListener {
            when(controla_button_map) {
                1 -> {
                    buttonAddMapa.visibility = View.GONE
                    mostramapaadd.visibility = View.VISIBLE
                    figMapAdda.visibility = View.VISIBLE
                    controla_button_map++
                }
                2 -> {
                    buttonAddMapa.visibility = View.GONE
                    mostramapaadd.visibility = View.GONE
                    figMapAdda.visibility = View.GONE
                    controla_button_map--
                }
            }
        }


        /*fica esperando o brosCast quando a função que atualiza a coordenada no mapView na fragment e usada, e retorna
        //as novas coordenadas que o usuario selecionou no mapa*/
        context?.registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))
        //padrao
        mapView?.onResume();


        /*vizualizar localização da publicação no mapa, e da a opção de vc adicionar no mapa outra coordenada caso o usuario n esteja no local
        //.. da publicacao, quando for adicionar a mesma*/
      /*  buttonAddMapa.setOnClickListener(){
            //envia a localização da onde a pessoa esta para o MapPub e da a opção caso ela queira mudar o marcador
            val contexto: Activity
            contexto = activity
            val intent = Intent(contexto, MapPub::class.java)
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
        } */

        buttonSalvara.setOnClickListener(){onClickCreate()} // para salvar a publicação, adicionando os dados no banco de dados
        // Refresh the state of the +1 button each time the activity receives focus.
    }


    // função obrigatoria para a extensão "OnMapReadyCallback" feita nessa atv para mostrar a coordenada no mapView
    override fun onMapReady(googleMap: GoogleMap?) {
        /*já mostra as coordenadas no mapView ao abrir a actividade, que foram passadas
        //pela PrincipalActivity por parametro (essas coordenadas) foram adquiridas
        //automaticamente la na Prin.Act referente ao local atual da pessoa quando abre o app(para caso for add um publicação aq)*/
        mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,"newPub","subtexto")

         //aq só faz a atualização do marcador, dps que a pessoa confirma a atualização no mapa pela outra função responsavel, (quando clica no MapView)*/
        googleMap?.setOnMapClickListener(){
            mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,"newPub","subtexto")
        }
    }

    //OBSERVAÇÃO::::::::::::;; verificar o unreceiver no onPause
    //atualiza as variaveis com as coordenadas novas, selecionadas pelo usuario la no atv MapPub
    inner class broadcastReceive:BroadcastReceiver(){
        override fun onReceive(context: Context?,intent: Intent?){
            latitude = intent?.getStringExtra("latMap")?.toDouble()
            longitude = intent?.getStringExtra("logMap")?.toDouble()
  //          context?.unregisterReceiver(broadcastReceive())
        }
    }
    val pubuser = Pubuser()

    //CHAMA A FUNCAO CASO O DISPOSITIVO NÃO TENHA PERMISSAO
    //USA PARA TIRAR A FOTO ATRAVES DA CAMERA
  /*  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) { //permissao para tirar a foto
        when(requestCode){
            MY_PERMISSIONS_REQUEST -> {
                try {
                    if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent()                                         //usa metodo tira a foto
                    }
                }catch (e:Exception){
                    Toast.makeText(context, "permissão para foto negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }  */


    //referencia foi para salvar a imagem e fica salvo na galeria
    //https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //referencia: https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //ARMAZENA SE TIVER TIRADO OU SELECIONADO EM UM IMAGE VIEW, metodo que trata o resultado da camera ou seleção de imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        camera.supportForOnActivityResult(requireContext(),null,pubuser,data,resultCode,requestCode,"add")//0 pq só vai ter que passar o valor do id, se for em caso de update/alterar o dado, e add, pq quando é no caso tbm de alteração..
//a mudança nesse arquivo foi o requeire context
    }


    // quando clica no salvar, para salvar os dados da publicação
    //insere os dados da publicação no banco de dados
     fun onClickCreate(){

                val nome = nomea.text.toString()
                val redesocial = redesociala.text.toString()
                val endereco = enderecoa.text.toString()
                val contato = contatoa.text.toString()
                val email = emaila.text.toString()
                val atvexercida = atvexa.text.toString()
                val categoria = spinneradd.selectedItem.toString()
                //val aprovado = categoriaa.aprovadoa.toString()
                val campo1 = campo1a.text.toString()
                val campo2 = campo2a.text.toString()
                val campo3 = campo3a.text.toString()
                val campo4 = campo4a.text.toString()
                val campo5 = campo5a.text.toString()
        if(pubuser.img1 != "" || pubuser.img2 != "" || pubuser.img3 != "" || pubuser.img4 != ""
        || nome != "" || redesocial != "" || endereco != "" || contato != ""|| atvexercida != "" ||
                categoria != "") {
                if (Validacpf().validateEmailFormat(email)) {
                    pubuser.nome = nome;
                    pubuser.redesocial = redesocial
                    pubuser.endereco = endereco
                    pubuser.contato = contato
                    pubuser.email = email
                    pubuser.atvexercida = atvexercida
                    pubuser.categoria = categoria
                    pubuser.campo1 = campo1
                    pubuser.campo2 = campo2
                    pubuser.campo3 = campo3
                    pubuser.campo4 = campo4
                    pubuser.campo5 = campo5
                    pubuser.longitude = longitude.toString()
                    pubuser.latitude = latitude.toString()

                    if (pubuser.latitude == "0.0") {
                        toast("localização indisponivel, ligue o gps")
                        dismiss()
                    } else {

                        val mainact = activity as PrincipalActivity
                        doAsync {
                            val tes = PubuserService.salvar(pubuser)
                            //val tes2 = PubuserService.getPubuser()
                            uiThread {
                                // Alerta de sucesso
                                // Dispara um evento para atualizar a lista
                                mainact.taskCarros()
                            }
                        }
                        dismiss()
                    }
                }else{
                    toast("E-mail invalido!")
                }
            } else {
                toast("Preencha todas informações/fotos")
            }


    }

    companion object {
        val KEY = "add_fragment"
    }
}
