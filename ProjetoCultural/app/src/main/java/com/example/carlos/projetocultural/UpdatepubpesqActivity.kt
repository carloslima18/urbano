package com.example.carlos.projetocultural

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import org.jetbrains.anko.alert
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import org.jetbrains.anko.db.*
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
//import android.app.Fragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.PubpesqService
import com.example.carlos.projetocultural.domain.PubuserService
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.AndroidUtils

import kotlinx.android.synthetic.main.activity_listviewpubpesq.*
import kotlinx.android.synthetic.main.content_principal.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class UpdatepubpesqActivity : DialogFragment(), OnMapReadyCallback, AdapterView.OnItemSelectedListener  {


    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)
    //variaveis para receber os dados da principalActivity
    var nome:String= "" ;var categoria:String= "" ;var endereco:String= "" ;var redesc:String= "" ;var contato:String= "" ;var atvex:String= "" ;var email:String= ""
    var cnpj:String = "" ;var representacao:String = "" ;var recurso:String = "" ;var anoinicio:String = "" ;var campo1:String= "" ;var campo2:String= ""
    var campo3:String= "" ;var campo4:String= "" ;var campo5:String= ""
    //  var img1:String= ""
    var idx:Int = 0 ;val cursor: Cursor? = null ;var width:Int= 0 ;var height:Int= 0
    //variaveis para receber as imagens do BD para coloca-las para a edição
    var base64_1:String ?= "" ;var base64_2:String ?= "" ;var base64_3:String ?= "" ;var base64_4:String ?= "" ;val camera = CameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)
    //variavel para operação de captura de imagem
    var latitude:Double?=null ;var longitude:Double?=null ;var mapView : MapView?=null ;val mappub = MapPub() ;val categories = ArrayList<String>();
    var dataAdapter: ArrayAdapter<String>?=null ;var spinner: Spinner?=null ;var database: MyDatabaseOpenHelper?=null


    var pubpesq: Pubpesq?= null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        categoria = arguments!!.getString("categorialr")
        idx = arguments!!.getInt("idx")
        retainInstance

        doAsync {
            pubpesq = PubpesqService.getPubpesqId(idx)
            //val tes2 = PubuserService.getPubuser()
            uiThread {
                preechetinterface(pubpesq)
            }
        }

        val view = inflater!!.inflate(R.layout.fragment_operacao, container, false)



        //ativa a visibilidade do botão para o usuario adicionar um campo para obter mais informação

        //att()
        getlocation()

        spinner = view?.findViewById<Spinner>(R.id.categoriaop) as Spinner
        //  var button = view?.findViewById<Button>(R.id.button)
        // Spinner click listener
        spinner?.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        categories.add("ESCOLA");categories.add("PRAÇA");categories.add("MUSEU");categories.add("TEATRO");categories.add("FEIRA");categories.add("OUTRO");
        // Creating adapter for spinner
        dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.setAdapter(dataAdapter);

        //ativa o mapView do Fragment
        mapView = view?.findViewById<MapView>(R.id.figMapAddop) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        //mudei essa linha do onresume para ca
        context?.registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))

        return view

    }


    fun preechetinterface(pubuser:Pubpesq?){
        nomeop.setText(pubuser?.nome)
        enderecoop.setText(pubuser?.endereco)
        redesocialop.setText(pubuser?.redesocial)
        contatoop.setText(pubuser?.contato)
        atvexop.setText(pubuser?.atvexercida)
        emailop.setText(pubuser?.email)


        etcnpjop.setText(pubuser?.cnpj)
        etanoinicioop.setText(pubuser?.anoinicio)
        etrepresentacaoop.setText(pubuser?.representacao)
        etrecursoop.setText(pubuser?.recurso)

        campo1aaop.setText(pubuser?.campo1)
        campo1aop.setText(pubuser?.campo2)
        campo2aaop.setText(pubuser?.campo3)
        campo2aop.setText(pubuser?.campo4)
        campo3aaop.setText(pubuser?.campo5)
        campo3aop.setText(pubuser?.campo6)
        campo4aaop.setText(pubuser?.campo7)
        campo4aop.setText(pubuser?.campo8)
        campo5aaop.setText(pubuser?.campo9)
        campo5aop.setText(pubuser?.campo10)
        campo6aaop.setText(pubuser?.campo11)
        campo6aop.setText(pubuser?.campo12)
        campo7aaop.setText(pubuser?.campo13)
        campo7aop.setText(pubuser?.campo14)
        campo8aaop.setText(pubuser?.campo15)
        campo8aop.setText(pubuser?.campo16)
        campo9aaop.setText(pubuser?.campo17)
        campo9aop.setText(pubuser?.campo18)
        campo10aaop.setText(pubuser?.campo19)
        campo10aop.setText(pubuser?.campo20)
    }


    fun att(){//eu acho que eu fiz para quando salvar um dado, atualziar ele buscando do banco da daddos local do app novamente
        val handler= Handler()
        Thread {
            pubpesq = PubpesqService.getPubpesqId(idx)
            handler.post {
                nomeop.setText(pubpesq?.nome)
                enderecoop.setText(pubpesq?.endereco)
                redesocialop.setText(pubpesq?.redesocial)
                contatoop.setText(pubpesq?.contato)
                atvexop.setText(pubpesq?.atvexercida)
                emailop.setText(pubpesq?.email)

                campo1aaop.setText(pubpesq?.campo1)
                campo1aop.setText(pubpesq?.campo2)
                campo2aaop.setText(pubpesq?.campo3)
                campo2aop.setText(pubpesq?.campo4)
                campo3aaop.setText(pubpesq?.campo5)
                campo3aop.setText(pubpesq?.campo6)
                campo4aaop.setText(pubpesq?.campo7)
                campo4aop.setText(pubpesq?.campo8)
                campo5aaop.setText(pubpesq?.campo9)
                campo5aop.setText(pubpesq?.campo10)
                campo6aaop.setText(pubpesq?.campo11)
                campo6aop.setText(pubpesq?.campo12)
                campo7aaop.setText(pubpesq?.campo13)
                campo7aop.setText(pubpesq?.campo14)
                campo8aaop.setText(pubpesq?.campo15)
                campo8aop.setText(pubpesq?.campo16)
                campo9aaop.setText(pubpesq?.campo17)
                campo9aop.setText(pubpesq?.campo18)
                campo10aaop.setText(pubpesq?.campo19)
                campo10aop.setText(pubpesq?.campo20)

                etcnpjop.setText(pubpesq?.cnpj)
                etanoinicioop.setText(pubpesq?.anoinicio)
                etrepresentacaoop.setText(pubpesq?.representacao)
                etrecursoop.setText(pubpesq?.representacao)


            }
        }
    }
    //para o spinner (eu acho)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // On selecting a spinner item
        val item = parent?.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent?.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }


    //fara quando estarta o fragment
    override fun onStart() {
        super.onStart()
        mapView?.onStart();

        atualizaIMG(0)

        //seta os nomes nos editText do fragment para sujeito a edição
        var spinnerPostion :Int?= 0
        spinnerPostion = 0
        if (!categoria.equals(null)) {
            spinnerPostion = dataAdapter!!.getPosition(categoria)
        }

        categoriaop.setSelection(spinnerPostion)
        att()






        // dialog.window.setLayout(width,height)
    }

    override fun onStop() {
        super.onStop()
        try {
            context?.unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        mapView?.onStop();
    }

    override fun onPause() {
        try {
            context?.unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        super.onPause()


        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    /*fun atualizaCoordenadasNoMapView(googleMap: GoogleMap?){
        val latlng = LatLng(latitude!!.toDouble(),longitude!!.toDouble())
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
        googleMap?.addMarker(MarkerOptions().position(latlng).title("newpub").snippet("informaçao adicional").flat(true)
                .rotation(245f))
    }*/

    override fun onMapReady(googleMap: GoogleMap?) {
        if(latitude != null) {
            mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)
            googleMap?.setOnMapClickListener() {
                mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)
            }
        }
    }

    //para capturar as imagens do banco de dados e coloca-las nas variaveis globais para serem usadas
    fun Buscaimg() {
        base64_1 = pubpesq?.img1
        base64_2 =  pubpesq?.img2
        base64_3 =  pubpesq?.img3
        base64_4 =  pubpesq?.img4}

    fun getlocation(){
        val latitudeS = pubpesq?.latitude
        val longitudeS =  pubpesq?.longitude
        latitude = latitudeS?.toDouble()
        longitude = longitudeS?.toDouble() }

    //observação (EU NAO FECHE O BROADCAST NO ONPAUSE)
    inner class broadcastReceive: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?){
            latitude = intent?.getStringExtra("latMap")?.toDouble()
            longitude = intent?.getStringExtra("logMap")?.toDouble()
//            context?.unregisterReceiver(broadcastReceive())
        }
    }

    //atializa as imageView do fragment
    fun atualizaIMG(numImg: Int){
        Buscaimg()
        if(pubpesq?.img1 != null) {
            if (numImg == 1 || numImg == 0) {
                //img1op.setImageURI(null)
                //  img1op.setImageURI(Uri.parse(base64_1))
                // editimg1.setImageBitmap(camera.base64ToBitmap(base64_1))
               // Glide.with(context).load(pubpesq?.img1).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img1op)
                Glide
                        .with(context)
                        .load(pubpesq?.img1)
                        .into(img1op);
            }
        }
        if(pubpesq?.img2 != null) {
            if (numImg == 2 || numImg == 0) {
                // img2op.setImageURI(null)
                //  img2op.setImageURI(Uri.parse(base64_2))
                //editimg2.setImageURI(Uri.parse(imgB))
                //Glide.with(requireContext()).load(pubpesq?.img2).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img2op)
                Glide
                        .with(context)
                        .load(pubpesq?.img2)
                        .into(img2op);
            }
        }
        if(pubpesq?.img3 != null) {
            if (numImg == 3 || numImg == 0) {
                //  img3op.setImageURI(null)
                //  img3op.setImageURI(Uri.parse(base64_3))
               // Glide.with(requireContext()).load(pubpesq?.img3).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img3op)
                Glide
                        .with(context)
                        .load(pubpesq?.img3)
                        .into(img3op);
            }
        }
        if(pubpesq?.img4 != null) {
            if (numImg == 4 || numImg == 0) {
                //  img4op.setImageURI(null)
                //  img4op.setImageURI(Uri.parse(base64_4))
              //  Glide.with(context).load(pubpesq?.img3).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img4op)
                Glide.with(context)
                        .asBitmap()
                        .load(pubpesq?.img4)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img4op)

            }
        }
    }

    // vai executar a cada volta do fragment para o atv principal e vice versa
    override fun onResume() {
        super.onResume()
        mapView?.onResume();


        cnpjllop.visibility = View.VISIBLE
        anoiniciollop.visibility = View.VISIBLE
        representacaollop.visibility = View.VISIBLE
        recursollop.visibility = View.VISIBLE

        att()

        val width = getResources().getDisplayMetrics().widthPixels
        //val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = (getResources().getDisplayMetrics().heightPixels * 0.8)
        //val height = resources.getDimensionPixelSize(R.dimen.)
        dialog.window!!.setLayout(width, height.toInt())

        //mundança dia 6/05 mudei essa linha la para o oncreate
       // context?.registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))

        editMapop.setOnClickListener(){
            getlocation()
            val intent = Intent(context, MapPub::class.java)
            //val locations:MutableList<String> = mutableListOf(longitude.toString(),latitude.toString())
            val longitudex:List<String> ?= mutableListOf(longitude.toString())
            val latitudex:List<String> ?= mutableListOf(latitude.toString())
            val nomex:List<String> ?= mutableListOf(nome)
            val atvex:List<String> ?= mutableListOf(atvex)
            intent.putExtra("mostrar","atualiza")
            intent.putExtra("longitude",longitudex?.toTypedArray())
            intent.putExtra("latitude",latitudex?.toTypedArray())
            intent.putExtra("nome",nomex?.toTypedArray())
            intent.putExtra("atvex",nomex?.toTypedArray())
            startActivity(intent)
        }

        val img1op = view?.findViewById<ImageView>(R.id.img1op) as ImageView
        val img2op = view?.findViewById<ImageView>(R.id.img2op) as ImageView
        val img3op = view?.findViewById<ImageView>(R.id.img3op) as ImageView
        val img4op = view?.findViewById<ImageView>(R.id.img4op) as ImageView

        val actt = activity as ListviewpubpesqActivity
        val fragment = this@UpdatepubpesqActivity
        //para quando clicar nas imgs chama o metodo tirafoto para caso necessite a troca(edição)
        img1op.setOnClickListener(){
            camera.tirafoto(1,actt,fragment,img1op,img2op,img3op,img4op)
        }
        img2op.setOnClickListener(){
            camera.tirafoto(2,actt,fragment,img1op,img2op,img3op,img4op)}
        img3op.setOnClickListener(){
            camera.tirafoto(3,actt,fragment,img1op,img2op,img3op,img4op)
        }
        img4op.setOnClickListener(){
            camera.tirafoto(4,actt,fragment,img1op,img2op,img3op,img4op)
        }



        //ações para os botoes delete e salvar (referente as edições) no fragment. Chamando os reespectivos metodos
        atteditop.setOnClickListener(){onClickupdate()}
        attdeleteop.setOnClickListener(){onClickdelete(pubpesq)}










        var addcampo = 1
        //para esconder todos os campos perguntas respostas;

        removecampolrop.setOnClickListener {
            campo1layoutlrop.visibility = View.GONE
            campo2layoutlrop.visibility = View.GONE
            campo3layoutrop.visibility = View.GONE
            campo4layoutlrop.visibility = View.GONE
            campo5layoutlrop.visibility = View.GONE
            campo6ayoutlrop.visibility = View.GONE
            campo7layoutlrop.visibility = View.GONE
            campo8layoutlrop.visibility = View.GONE
            campo9layoutlrop.visibility = View.GONE
            campo10layoutlrop.visibility = View.GONE
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


        addcampolrop.setOnClickListener {
            when(addcampo){
                1 -> {
                    campo1layoutlrop.visibility = View.VISIBLE
                    campo2layoutlrop.visibility = View.VISIBLE
                    addcampo++
                }
                2 -> {
                    campo3layoutrop.visibility = View.VISIBLE
                    campo4layoutlrop.visibility = View.VISIBLE
                    addcampo++
                }
                3 -> {
                    campo5layoutlrop.visibility = View.VISIBLE
                    campo6ayoutlrop.visibility = View.VISIBLE
                    addcampo++
                }
                4 -> {
                    campo7layoutlrop.visibility = View.VISIBLE
                    campo8layoutlrop.visibility = View.VISIBLE
                    addcampo++
                }
                5 -> {
                    campo9layoutlrop.visibility = View.VISIBLE
                    campo10layoutlrop.visibility = View.VISIBLE
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
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)//mudança era context
        camera.supportForOnActivityResult(requireContext(),pubpesq,null,data,resultCode,requestCode,"update")//0 pq só vai ter que passar o valor do id, se for em caso de update/alterar o dado, e add, pq quando é no caso tbm de alteração..
        //precisa mudar o caminho na classe camera!
    }

    val androidutils = AndroidUtils
    //variavel para indicar qual foto o usuario vai editar
    fun onClickupdate(){
        pubpesq?.id = idx
        pubpesq?.nome = nomeop.text.toString()
        pubpesq?.endereco = enderecoop.text.toString()
        pubpesq?.contato =  contatoop.text.toString()
        pubpesq?.email =  emailop.text.toString()
        pubpesq?.atvexercida =  atvexop.text.toString()
        pubpesq?.categoria = spinner?.getSelectedItem().toString();

        pubpesq?.cnpj = etcnpjop.text.toString()
        pubpesq?.representacao = etrepresentacaoop.text.toString()
        pubpesq?.recurso = etrecursoop.text.toString()
        pubpesq?.anoinicio = etanoinicioop.text.toString()

        pubpesq?.campo1 =  campo1aaop.text.toString()
        pubpesq?.campo2 =  campo1aop.text.toString()
        pubpesq?.campo3 =  campo2aaop.text.toString()
        pubpesq?.campo4 =  campo2aop.text.toString()
        pubpesq?.campo5 =  campo3aaop.text.toString()
        pubpesq?.campo6 =  campo3aop.text.toString()
        pubpesq?.campo7 =  campo4aaop.text.toString()
        pubpesq?.campo8 =  campo4aop.text.toString()
        pubpesq?.campo9 =  campo5aaop.text.toString()
        pubpesq?.campo10 =  campo5aop.text.toString()
        pubpesq?.campo11 =  campo6aaop.text.toString()
        pubpesq?.campo12 =  campo6aop.text.toString()
        pubpesq?.campo13 =  campo7aaop.text.toString()
        pubpesq?.campo14 =  campo7aop.text.toString()
        pubpesq?.campo15 =  campo8aaop.text.toString()
        pubpesq?.campo16 =  campo8aop.text.toString()
        pubpesq?.campo17 =  campo9aaop.text.toString()
        pubpesq?.campo18 =  campo9aop.text.toString()
        pubpesq?.campo19 =  campo10aaop.text.toString()
        pubpesq?.campo20 =  campo10aop.text.toString()



        pubpesq?.latitude =  latitude.toString()
        pubpesq?.longitude =  longitude.toString()

        val handle = Handler()
        Thread{
            val mainact = activity as ListviewpubpesqActivity
            val tes = PubpesqService.updatepesq(pubpesq)
            handle.post{
                toast("editado")
                mainact.taskPubpesq()
                dismiss()
            }
        }.start()

    }


    //exclui toda a publicação
    fun onClickdelete(pubpesq: Pubpesq?){
         // val mainact = activity as PrincipalActivity
         context?.alert("deseja apagar esta publicação") {
            title = "Ação"
            negativeButton("deletar") {
                val handle = Handler()
                Thread{
                    val mainact = activity as ListviewpubpesqActivity
                    val tes = PubpesqService.deletepesquser(pubpesq)
                    handle.post{
                        toast("deletado")
                        mainact.taskPubpesq()
                        dismiss()
                    }
                }.start()
            }
            positiveButton("voltar"){

            }
        }!!.show()



    }

    companion object {
        val KEY2 = "update_fragment"
    }
}
