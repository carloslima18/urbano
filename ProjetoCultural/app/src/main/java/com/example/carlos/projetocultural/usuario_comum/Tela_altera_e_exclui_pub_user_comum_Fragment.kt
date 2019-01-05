package com.example.carlos.projetocultural.usuario_comum


//AIzaSyBnfiLIhSwZD1JLxn4W-x5PK8ouSKYXVJI

import android.app.Activity
import android.app.Dialog
import android.content.*
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.PubuserService
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.OperacoesEconfiguracoesCameraImagemCameraHelper

import com.google.android.gms.maps.*

//import com.google.android.gms.plus.PlusOneButton
import kotlinx.android.synthetic.main.tela_altera_e_exclui_pub_user_comum_fragment.*

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import com.bumptech.glide.request.RequestOptions
import com.example.carlos.projetocultural.Configuracao_google_maps_Activity
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.utils.Validacpf


/**
 * A fragment with a Google +1 button.
 */
//esta classe cuida quando acontece do usuario clicar em um item do list view, aonde ficam as publicações salvas pela classe Tela_add_pub_user_comum_Fragment
//esta classe edita e exclui os dados de uma publicação já salva
class Tela_altera_e_exclui_pub_user_comum_Fragment : DialogFragment(), OnMapReadyCallback, AdapterView.OnItemSelectedListener  {

    //variaveis para receber os dados da principalActivity
    var nome:String= ""
    var categoria:String= ""
    var endereco:String= ""
    var redesc:String= ""
    var contato:String= ""
    var atvex:String= ""
    var email:String= ""
    var campo1:String= ""
    var campo2:String= ""
    var campo3:String= ""
    var campo4:String= ""
    var campo5:String= ""
   //  var img1:String= ""
    var idx:Int = 0
    var idBD:Int = 0
    val cursor:Cursor? = null
    var width:Int= 0
    var height:Int= 0

    //variaveis para receber as imagens do BD para coloca-las para a edição
    var base64_1:String ?= ""
    var base64_2:String ?= ""
    var base64_3:String ?= ""
    var base64_4:String ?= ""

    val camera = OperacoesEconfiguracoesCameraImagemCameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)

    //variavel para operação de captura de imagem
    val PICK_FROM_FILE = 2;
    val REQUEST_IMAGE_CODE = 1;

    var latitude:Double?=null
    var longitude:Double?=null

    var mapView : MapView?=null

    val mappub = Configuracao_google_maps_Activity()
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    //create dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
    val categories = ArrayList<String>();
    var dataAdapter:ArrayAdapter<String>?=null
    var spinner:Spinner?=null

    var pubuser :Pubuser ?= null
    //createView! infla a view do dialogFragment para o usuario no app
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //recebe os dados da principalActivity atrevez do bundle
        categoria = arguments!!.getString("categorialr")
        idx = arguments!!.getInt("idx")
        retainInstance

        doAsync {
             pubuser = PubuserService.getPubuserId(idx)

            //val tes2 = PubuserService.getPubuser()

            uiThread {
                preechetinterface(pubuser)
            }
        }


        val view = inflater!!.inflate(R.layout.tela_altera_e_exclui_pub_user_comum_fragment, container, false)


        att()
        getlocation()



        // Spinner element
        spinner = view?.findViewById<Spinner>(R.id.categoriaop) as Spinner
        //  var button = view?.findViewById<Button>(R.id.button)
        // Spinner click listener
        spinner?.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        categories.add("ESCOLA PÚBLICA");
        categories.add("PRAÇA");
        categories.add("ESPAÇO PÚBLICO DE CULURA");
        categories.add("FEIRA");
        categories.add("OUTRO");
        // Creating adapter for spinner
        dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.setAdapter(dataAdapter);


        //ativa o mapView do Fragment
        mapView = view?.findViewById<MapView>(R.id.figMapAddop) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        context?.registerReceiver(broadcastReceive(), IntentFilter(Configuracao_google_maps_Activity.loc_receiver))

        return view
    }


    fun att(){
        val handler= Handler()
        Thread {
            pubuser = PubuserService.getPubuserId(idx)
            handler.post{
                nomeop.setText(pubuser?.nome)
                enderecoop.setText(pubuser?.endereco)
                redesocialop.setText(pubuser?.redesocial)
                contatoop.setText(pubuser?.contato)
                atvexop.setText(pubuser?.atvexercida)
                emailop.setText(pubuser?.email)
           /*     campo1op.setText(pubuser?.campo1)
                campo2op.setText(pubuser?.campo2)
                campo3op.setText(pubuser?.campo3)
                campo4op.setText(pubuser?.campo4)
                campo5op.setText(pubuser?.campo5)  */
            }
        }
    }

    fun preechetinterface(pubuser:Pubuser?){
        nomeop.setText(pubuser?.nome)
        enderecoop.setText(pubuser?.endereco)
        redesocialop.setText(pubuser?.redesocial)
        contatoop.setText(pubuser?.contato)
        atvexop.setText(pubuser?.atvexercida)
        emailop.setText(pubuser?.email)
     /*   campo1op.setText(pubuser?.campo1)
        campo2op.setText(pubuser?.campo2)
        campo3op.setText(pubuser?.campo3)
        campo4op.setText(pubuser?.campo4)
        campo5op.setText(pubuser?.campo5) */
    }



    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // On selecting a spinner item
        var item = parent?.getItemAtPosition(position).toString();
        if(item == "OUTRO") {
            atvexllop.visibility = View.VISIBLE
        }else{
            atvexllop.visibility = View.GONE
        }

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
    var database: MyDatabaseOpenHelper?=null
    fun Buscaimg() {
        val mainact = activity as Tela_listagem_pub_user_comum_Activity
        base64_1 = pubuser?.img1
        base64_2 =  pubuser?.img2
        base64_3 =  pubuser?.img3
        base64_4 =  pubuser?.img4
    }


    fun getlocation(){
        val mainact = activity as Tela_listagem_pub_user_comum_Activity
        val latitudeS = pubuser?.latitude
        val longitudeS =  pubuser?.longitude
        latitude = latitudeS?.toDouble()
        longitude = longitudeS?.toDouble()
    }

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
        if(pubuser?.img1 != null) {
            if (numImg == 1 || numImg == 0) {
                //img1op.setImageURI(null)
              //  img1op.setImageURI(Uri.parse(base64_1))
               // editimg1.setImageBitmap(camera.base64ToBitmap(base64_1))
               // Glide.with(context).load(pubuser?.img1).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img1op)
                Glide.with(context)
                        .asBitmap()
                        .load(pubuser?.img1)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img1op)
            }
        }
        if(pubuser?.img2 != null) {
            if (numImg == 2 || numImg == 0) {
               // img2op.setImageURI(null)
              //  img2op.setImageURI(Uri.parse(base64_2))
                //editimg2.setImageURI(Uri.parse(imgB))
              //  Glide.with(context).load(pubuser?.img2).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img2op)
                Glide.with(context)
                        .asBitmap()
                        .load(pubuser?.img2)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img2op)

            }
        }
        if(pubuser?.img3 != null) {
            if (numImg == 3 || numImg == 0) {
              //  img3op.setImageURI(null)
              //  img3op.setImageURI(Uri.parse(base64_3))
                //Glide.with(context).asBitmap().load(pubuser?.img3).override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img3op)
                Glide.with(context)
                        .asBitmap()
                        .load(pubuser?.img3)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img3op)
            }
        }
        if(pubuser?.img4 != null) {
            if (numImg == 4 || numImg == 0) {
              //  img4op.setImageURI(null)
              //  img4op.setImageURI(Uri.parse(base64_4))
               // Glide.with(context).load(pubuser?.img4).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(img4op)
                Glide.with(context)
                        .asBitmap()
                        .load(pubuser?.img4)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img4op)
            }
        }
        // Put asBitmap() right after Glide.with(context) ,,. 4.0+

    }



    // vai executar a cada volta do fragment para o atv principal e vice versa
    override fun onResume() {
        super.onResume()
        //seta os nomes nos editText do fragment para sujeito a edição
        var spinnerPostion :Int?= 0
        spinnerPostion = 0
        if (!categoria.equals(null)) {
            spinnerPostion = dataAdapter!!.getPosition(categoria)
        }
        categoriaop.setSelection(spinnerPostion)

        att()

        mapView?.onResume();
        layoutaddcamposop.visibility = View.GONE
       //

        editMapop.setOnClickListener(){
            getlocation()
            val contexto: Activity
            contexto = requireActivity() // mudança, estava so activity
            val intent = Intent(contexto, Configuracao_google_maps_Activity::class.java)
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

        //atualiza as imagens no fragment para edição

         atualizaIMG(0)


        //para quando clicar nas imgs chama o metodo tirafoto para caso necessite a troca(edição)
       /* img1op.setOnClickListener(){tirafoto(1)}
        img2op.setOnClickListener(){tirafoto(2)}
        img3op.setOnClickListener(){tirafoto(3)}
        img4op.setOnClickListener(){tirafoto(4)}
*/
        //ações para os botoes delete e salvar (referente as edições) no fragment. Chamando os reespectivos metodos
        atteditop.setOnClickListener(){onClickupdate()}
        attdeleteop.setOnClickListener(){onClickdelete(pubuser)}



        val actt = activity as Tela_listagem_pub_user_comum_Activity
        val fragment = this@Tela_altera_e_exclui_pub_user_comum_Fragment
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
    }

    //variavel para indicar qual foto o usuario vai editar
    var numImgx:Int = 0

    //seleciona uma foto dos arquivos (albuns do celular)
  /*  fun tirafoto(numImg:Int){
        numImgx = numImg
        dispatchTakePictureIntent()             //caso tiver permissao usa metodo tira a foto
    }

    // ESTARTA A ATIVIDADE PARA TIRAR A FOTO OU ABRIR OS ARQUIVOS DE IMAGENS PARA SELECIONAR
    fun dispatchTakePictureIntent(){
        val act = activity as Tela_listagem_pub_user_comum_Activity
        act.alert("") {
            title = "Escolher fotografia"
            negativeButton("Tirar foto") {
                if(false) {
                    try {
                        val ms = System.currentTimeMillis()
                        val fileName = "fotopub_${ms}.jpg"
                        val intent = camera.open(context, fileName) // chama da classe cameraHeper as funções para tirar foto
                        startActivityForResult(intent, REQUEST_IMAGE_CODE) //estarta o intent enviando para o metodo activityResult para tratar o resultado

                    } catch (e: Exception) {
                        Toast.makeText(context, "could not create file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            positiveButton("Importar") {
                try {

                    val ms = System.currentTimeMillis()
                    val fileName = "fotopub_${ms}.jpg"
                    val intent = camera.openFoto(context,fileName)
                    startActivityForResult(Intent.createChooser(intent,"escolha uma imagem"),PICK_FROM_FILE)

                }catch (e:Exception){
                    Toast.makeText(context, "could not create file", Toast.LENGTH_SHORT).show()
                }
            }
        }.show()

    }
*/
   /* fun onclickupdate2(edt:String,urix:String?){
        val mainact = activity as Tela_listagem_pub_user_comum_Activity
        //val ID = mainact.lvPubPrinc.getAdapter().getItemId(idx)
        mainact.database?.use {
            update("publicacao2", ("img" + edt) to urix
            )
                    .where("_id = \""+idx+"\"")
                    .exec()
        }
        val todoCursor=  mainact.database!!.writableDatabase.rawQuery("Select * from publicacao2",null)
        mainact.CA?.swapCursor(todoCursor)
    } */



    //https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //referencia: https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //ARMAZENA SE TIVER TIRADO OU SELECIONADO EM UM IMAGE VIEW, metodo que trata o resultado da camera ou seleção de imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        camera.supportForOnActivityResult(requireContext(),null,pubuser,data,resultCode,requestCode,"update")//0 pq só vai ter que passar o valor do id, se for em caso de update/alterar o dado, e add, pq quando é no caso tbm de alteração..
    }

    fun onclickupdate3(edt:Int,urix:String?){


        pubuser?.id = idx
        if(edt == 1) { pubuser?.img1 = urix }
        if(edt == 2) { pubuser?.img2 = urix }
        if(edt == 3) { pubuser?.img3 = urix }
        if(edt == 4) { pubuser?.img4 = urix }


        val mainact = activity as Tela_listagem_pub_user_comum_Activity
        doAsync {
            val tes = PubuserService.updateuser(pubuser)
            //val tes2 = PubuserService.getPubuser()
            uiThread {
                // Alerta de sucesso
                // Dispara um evento para atualizar a lista
                mainact.taskCarros()
            }
        }
    }

    fun onClickupdate(){

        pubuser?.id = idx
        pubuser?.nome = nomeop.text.toString()
        pubuser?.endereco = enderecoop.text.toString()
        pubuser?.contato =  contatoop.text.toString()
        pubuser?.email =  emailop.text.toString()
        pubuser?.redesocial =  redesocialop.text.toString()
        pubuser?.atvexercida =  atvexop.text.toString()
        pubuser?.categoria = spinner?.selectedItem.toString()
    //    pubuser?.campo1 =  campo1op.text.toString()
    //    pubuser?.campo2 =  campo2op.text.toString()
    //    pubuser?.campo3 =  campo3op.text.toString()
    //    pubuser?.campo4 =  campo4op.text.toString()
    //    pubuser?.campo5 =  campo5op.text.toString()
        pubuser?.img1 =  base64_1
        pubuser?.img2 =  base64_2
        pubuser?.img3 =  base64_3
        pubuser?.img4 =  base64_4
        pubuser?.latitude =  latitude.toString()
        pubuser?.longitude =  longitude.toString()
        if(pubuser?.img1 != "" && pubuser?.img2 != "" && pubuser?.img3 != "" && pubuser?.img4 != "" && pubuser?.nome != ""  && pubuser?.categoria != "") {
            if (Validacpf().validateEmailFormat(email) || email == "") {


                val mainact = activity as Tela_listagem_pub_user_comum_Activity
                doAsync {
                    val tes = PubuserService.updateuser(pubuser)
                    //val tes2 = PubuserService.getPubuser()
                    uiThread {
                        // Alerta de sucesso
                        // Dispara um evento para atualizar a lista
                        mainact.taskCarros()
                        dismiss() //fecha o fragment
                    }
                }
            } else {
                toast("Email inválido")
            }
        }else{
            toast("Dados como nome, imagens, categoria devem ser preenchidos")
        }
    }


    //realiza o salvamento de todos os dados editados pelo usuario no fragment
   /* fun onClickupdate(){
        val mainact = activity as Tela_listagem_pub_user_comum_Activity
       // val ID = mainact.lvPubPrinc.getAdapter().getItemId(idx)

        val categoria:String = spinner?.selectedItem.toString()
        mainact.database?.use {
            update("publicacao2",
                    "nome" to nomeop.text.toString(),
                    "redesocial" to redesocialop.text.toString(),
                    "endereco" to enderecoop.text.toString(),
                    "contato" to contatoop.text.toString(),
                    "email" to emailop.text.toString(),
                    "atvexercida" to atvexop.text.toString(),
                    "categoria" to categoria,
                    "campo1" to campo1op.text.toString(),
                    "campo2" to campo2op.text.toString(),
                    "campo3" to campo3op.text.toString(),
                    "campo4" to campo4op.text.toString(),
                    "campo5" to campo5op.text.toString(),
                    "img1" to base64_1,
                    "img2" to base64_2,
                    "img3" to base64_3,
                    "img4" to base64_4,
                    "latitude" to latitude,
                    "longitude" to longitude
                    )
                    .where("_id = \""+idx+"\"")
                    .exec()
        }
        val todoCursor=  mainact.database!!.writableDatabase.rawQuery("Select * from publicacao2",null)
        mainact.CA?.swapCursor(todoCursor)
        dismiss() //fecha o fragment
    } */

    //exclui toda a publicação
    /*fun onClickdelete(){
        val mainact = activity as Tela_listagem_pub_user_comum_Activity
        mainact.alert("deseja apagar esta publicação") {
            title = "Ação"
            negativeButton("deletar") {
             //   val ID = mainact.lvPubPrinc.getAdapter().getItemId(idx)
                mainact.database?.use {
                    delete("publicacao2", "_id = \"" + idx + "\"")
                }
                Toast.makeText(context,"Item Deletado!!", Toast.LENGTH_SHORT).show()
                val todoCursor = mainact.database!!.writableDatabase.rawQuery("Select * from publicacao2", null)
                mainact.CA?.swapCursor(todoCursor)
                dismiss()
            }
            positiveButton("voltar"){
                dismiss()
            }
        }.show()
    }*/

    fun onClickdelete(pubuser: Pubuser?){
        val mainact = activity as Tela_listagem_pub_user_comum_Activity
        doAsync {
            val tes = PubuserService.deletepubuser(pubuser)
            //val tes2 = PubuserService.getPubuser()
            uiThread {
                // Alerta de sucesso
                // Dispara um evento para atualizar a lista
                mainact.taskCarros()
                dismiss() //fecha o fragment
            }
        }
    }

    //variavel abstrata para identificar o fragemnt na atv principalActivity
    companion object {
        val KEY2 = "operacao_fragment"
    }

}
