package com.example.carlos.projetocultural

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.PubpesqService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

import kotlinx.android.synthetic.main.activity_tela_vizualiza_pub_pesquisador.*
import kotlinx.android.synthetic.main.content_tela_vizualiza_pub_pesquisador.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class Tela_vizualiza_pub_pesquisador_Activity : AppCompatActivity() , OnMapReadyCallback {
    var idx:Int = 0
    var pubpesq:Pubpesq?=null
    var mapView : MapView?=null
    val mappub = Configuracao_google_maps_Activity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_vizualiza_pub_pesquisador)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);


        val extras = intent.extras
        idx = extras.getInt("idx")

        doAsync {
            pubpesq = PubpesqService.getPubpesqId(idx)
            //val tes2 = PubuserService.getPubuser()
            uiThread {
                preechetinterface(pubpesq)
            }
        }
        llimg3.visibility = View.GONE

        mapView = findViewById<MapView>(R.id.figMapAddav) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap?) {
        if(pubpesq?.latitude != null) {
            mappub.atualizaCoordenadasNoMapView(googleMap,pubpesq!!.latitude.toDouble(),pubpesq!!.longitude!!.toDouble(),pubpesq!!.nome,pubpesq!!.atvexercida)
            googleMap?.setOnMapClickListener() {
                mappub.atualizaCoordenadasNoMapView(googleMap,pubpesq!!.latitude.toDouble(),pubpesq!!.longitude!!.toDouble(),pubpesq!!.nome,pubpesq!!.atvexercida)
            }
        }
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

        titulov.setText(pubpesq?.nome.toString())

        var addcampo = 1
        //para esconder todos os campos perguntas respostas;

        btescondeperguntas.setOnClickListener {
             ll12.visibility = View.GONE
             ll13.visibility = View.GONE
             ll14.visibility = View.GONE
             ll15.visibility = View.GONE
             ll16.visibility = View.GONE
             ll17.visibility = View.GONE
             ll18.visibility = View.GONE
             ll19.visibility = View.GONE
             ll20.visibility = View.GONE
             ll21.visibility = View.GONE
             view1.visibility = View.GONE
             view2.visibility = View.GONE
             view3.visibility = View.GONE
             view4.visibility = View.GONE
             view5.visibility = View.GONE
             view6.visibility = View.GONE
             view7.visibility = View.GONE
             view8.visibility = View.GONE
             view9.visibility = View.GONE
             view10.visibility = View.GONE
             addcampo= 1
        }


        var controla_button_map = 1



        var controla_button_foto = 1
        btmostraimg.setOnClickListener {
            when(controla_button_foto) {
                1 -> {
                    llimg3.visibility = View.VISIBLE
                    controla_button_foto++
                }
                2 -> {
                    llimg3.visibility = View.GONE
                    controla_button_foto--
                }
            }
        }

        bigimvv.setOnClickListener {
            bigimvv.visibility = View.GONE
            img1vv.visibility = View.VISIBLE
            img2vv.visibility = View.VISIBLE
            img3vv.visibility = View.VISIBLE
            img4vv.visibility = View.VISIBLE
        }
        img1vv.setOnClickListener {
            //Glide.with(this).load(pubpesq?.img1).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(bigim)

            Glide.with(applicationContext)
                    .asBitmap()
                    .load(pubpesq?.img1)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(bigim)


            bigimvv.visibility = View.VISIBLE
            img1vv.visibility = View.GONE
            img2vv.visibility = View.GONE
            img3vv.visibility = View.GONE
            img4vv.visibility = View.GONE
        }
        img2vv.setOnClickListener {
           // Glide.with(this).load(pubpesq?.img2).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(bigim)

            Glide.with(applicationContext)
                    .asBitmap()
                    .load(pubpesq?.img2)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(bigim)
            bigimvv.visibility = View.VISIBLE
            img1vv.visibility = View.GONE
            img2vv.visibility = View.GONE
            img3vv.visibility = View.GONE
            img4vv.visibility = View.GONE
        }
        img3vv.setOnClickListener {
           // Glide.with(this).load(pubpesq?.img3).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(bigim)
            Glide.with(applicationContext)
                    .asBitmap()
                    .load(pubpesq?.img3)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(bigim)

            bigimvv.visibility = View.VISIBLE
            img1vv.visibility = View.GONE
            img2vv.visibility = View.GONE
            img3vv.visibility = View.GONE
            img4vv.visibility = View.GONE
        }
        img4vv.setOnClickListener {
           // Glide.with(this).load(pubpesq?.img4).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(bigim)
            Glide.with(applicationContext)
                    .asBitmap()
                    .load(pubpesq?.img4)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(bigim)

            bigimvv.visibility = View.VISIBLE
            img1vv.visibility = View.GONE
            img2vv.visibility = View.GONE
            img3vv.visibility = View.GONE
            img4vv.visibility = View.GONE
        }





        btmostramapa.setOnClickListener {
            when(controla_button_map) {
                1 -> {
                    mostramapa.visibility = View.VISIBLE
                    figMapAddav.visibility = View.VISIBLE
                    controla_button_map++
                }
                2 -> {
                    mostramapa.visibility = View.GONE
                    figMapAddav.visibility = View.GONE
                    controla_button_map--
                }
            }
        }

        btperguntaserespostas.setOnClickListener {
            when(addcampo){
                1 -> {
                    if(pgt1v.text != "") {
                        ll12.visibility = View.VISIBLE
                        ll13.visibility = View.VISIBLE
                        view1.visibility = View.VISIBLE
                        view2.visibility = View.VISIBLE
                    }else{
                        toast("pgt 1 e 2 vazia")
                    }
                    addcampo++
                }
                2 -> {
                    if (pgt3v.text != "") {
                        ll14.visibility = View.VISIBLE
                        ll15.visibility = View.VISIBLE
                        view3.visibility = View.VISIBLE
                        view4.visibility = View.VISIBLE
                    } else{
                        toast("pgt 3 e 4 vazio")
                    }
                    addcampo++
                }
                3 -> {
                    if (pgt5v.text != "") {
                        ll16.visibility = View.VISIBLE
                        ll17.visibility = View.VISIBLE
                        view5.visibility = View.VISIBLE
                        view6.visibility = View.VISIBLE
                    } else{
                        toast("pgt 5 e 6 vazio")
                    }
                    addcampo++
                }
                4 -> {
                    if (pgt7v.text != "") {
                        ll18.visibility = View.VISIBLE
                        ll19.visibility = View.VISIBLE
                        view7.visibility = View.VISIBLE
                        view8.visibility = View.VISIBLE
                    } else{
                        toast("pgt 7 e 8 vazio")
                    }
                    addcampo++
                }
                5 -> {
                    if (pgt9v.text != "") {
                        ll20.visibility = View.VISIBLE
                        ll21.visibility = View.VISIBLE
                        view9.visibility = View.VISIBLE
                        view10.visibility = View.VISIBLE
                    } else{
                        toast("pgt 9 e 10 vazio")
                    }

                    addcampo++
                }
            }

        }
    }



        fun preechetinterface(pubuser:Pubpesq?){
            nomev.setText(pubuser?.nome)
            enderecov.setText(pubuser?.endereco)
            redecoailv.setText(pubuser?.redesocial)
            contatov.setText(pubuser?.contato.toString())
            //atvexv.setText(pubuser?.atvexercida.toString())
            emailv.setText(pubuser?.email)


            cnpjv.setText(pubuser?.cnpj.toString())
            anoiniciov.setText(pubuser?.anoinicio.toString())
            representacaov.setText(pubuser?.representacao)
            recursov.setText(pubuser?.recurso)

            pgt1v.setText(pubuser?.campo1)
            resp1vv.setText(pubuser?.campo2)
            pgt2v.setText(pubuser?.campo3)
            resp2vv.setText(pubuser?.campo4)
            pgt3v.setText(pubuser?.campo5)
            resp3vv.setText(pubuser?.campo6)
            pgt4v.setText(pubuser?.campo7)
            resp4vv.setText(pubuser?.campo8)
            pgt5v.setText(pubuser?.campo9)
            resp5vv.setText(pubuser?.campo10)
            pgt6v.setText(pubuser?.campo11)
            resp6vv.setText(pubuser?.campo12)
            pgt7v.setText(pubuser?.campo13)
            resp7vv.setText(pubuser?.campo14)
            pgt8v.setText(pubuser?.campo15)
            resp8vv.setText(pubuser?.campo16)
            pgt9v.setText(pubuser?.campo17)
            resp9vv.setText(pubuser?.campo18)
            pgt10v.setText(pubuser?.campo19)
            resp10vv.setText(pubuser?.campo20)





            if(pubuser?.img1 != null){
               //  Glide.with(this).load(pubpesq?.img1).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(img1v)
                Glide.with(applicationContext)
                        .asBitmap()
                        .load(pubpesq?.img1)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img1v)
            }else{
                img1v.visibility = View.GONE
            }
            if(pubuser?.img2 != null){
                //Glide.with(this).load(pubpesq?.img2).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(img2v)
                Glide.with(applicationContext)
                        .asBitmap()
                        .load(pubpesq?.img2)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img2v)

            }else{
                img2v.visibility = View.GONE
            }
            if(pubuser?.img3 != null){
               // Glide.with(this).load(pubpesq?.img3).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(img3v)
                Glide.with(applicationContext)
                        .asBitmap()
                        .load(pubpesq?.img3)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img3v)

            }else{
                img3v.visibility = View.GONE
            }
            if(pubuser?.img4 != null){
                //Glide.with(this).load(pubpesq?.img4).asBitmap().override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(img4v)
                Glide.with(applicationContext)
                        .asBitmap()
                        .load(pubpesq?.img4)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(img4v)

            }else{
                img4v.visibility = View.GONE
            }

            /*
            val imgspubs:MutableList<Uri> = mutableListOf()
            imgspubs.add(Uri.parse(pubpesq!!.img1.toString()))
            imgspubs.add(Uri.parse(pubpesq!!.img2.toString()))
            imgspubs.add(Uri.parse(pubpesq!!.img3.toString()))
            imgspubs.add(Uri.parse(pubpesq!!.img4.toString()))
            Gallery.setAdapter(AdapterImgs(this,imgspubs)); */
    }
}
