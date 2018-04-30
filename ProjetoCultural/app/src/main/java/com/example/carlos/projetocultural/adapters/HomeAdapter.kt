package com.example.carlos.projetocultural.adapters


import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.domain.Pubpesq
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.utils.CameraHelper
import android.graphics.Movie
import android.widget.Filterable
import kotlinx.android.synthetic.main.content_home.*
import java.util.*


// Define o construtor que recebe (carros,onClick)


class HomeAdapter(var pubtot:MutableList<Pubpesq>,m:Int, val onClick: (Pubpesq) -> Unit, val onLongClickListener: (Pubpesq) -> Boolean) :RecyclerView.Adapter<HomeAdapter.ViewHolder>(){
    // Retorna a quantidade de carros na lista




    var camera = CameraHelper()

    fun add(h:RecyclerView?,item: Pubpesq) {
        pubtot.add(item)
        if(h !=null) {
            h.adapter?.notifyItemInserted(pubtot.size - 1);
            h.adapter?.notifyDataSetChanged()
        }
    }


    //ta errado recycleview ta null
    fun addAll(h:RecyclerView,mcList: MutableList<Pubpesq>) {
        for (mc in mcList) {
            add(null,mc)
        }
        h.adapter?.notifyItemInserted(pubtot.size - 1);
    }


    override fun getItemCount(): Int {
        return (this.pubtot.size)
    }
    // Infla o layout do adapter e retorna o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla a view do adapter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row_main, parent, false)
        // Retorna o ViewHolder que contém todas as views
        val holder = ViewHolder(view)
        return holder
    }
    // Faz o bind para atualizar o valor das views com os dados do Carro
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Recupera o objeto carro
        val carro = pubtot[position]
        val view = holder.itemView
        with(view) {

            view.findViewById<TextView>(R.id.textViewnomeM)?.text = carro.nome
            view.findViewById<TextView>(R.id.idM)?.text= carro.id.toString()
            view.findViewById<TextView>(R.id.enderecoM)?.text=carro.endereco
            view.findViewById<TextView>(R.id.redesocialM)?.text=carro.redesocial
            view.findViewById<TextView>(R.id.contatoM)?.text=carro.contato
            view.findViewById<TextView>(R.id.emailM)?.text=carro.email
            view.findViewById<TextView>(R.id.anoinicioM)?.text=carro.anoinicio
            view.findViewById<TextView>(R.id.representacaoM)?.text=carro.representacao
            view.findViewById<TextView>(R.id.recursoM)?.text=carro.recurso
            view.findViewById<TextView>(R.id.latitudeM)?.text=carro.latitude
            view.findViewById<TextView>(R.id.longitudeM)?.text=carro.longitude
            view.findViewById<TextView>(R.id.pesquisadorM)?.text=carro.pesquisador.toString()
            view.findViewById<TextView>(R.id.campo1M)?.text=carro.campo1
            view.findViewById<TextView>(R.id.campo2M)?.text=carro.campo2
            view.findViewById<TextView>(R.id.campo3M)?.text=carro.campo3
            view.findViewById<TextView>(R.id.campo4M)?.text=carro.campo4
            view.findViewById<TextView>(R.id.campo5M)?.text=carro.campo5

            val base64_1 = view.findViewById<ImageView>(R.id.imglistrow1M) as ImageView


            if (carro.img1 != "") {
                base64_1.setImageBitmap(camera.base64ForBitmap2(carro.img1.toString()))
                // Glide.with(context).load().asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(base64_1)
            }
            /*
                if(carro.img1 != null) {
             val path1 = Uri.parse(carro.img1)
             val imga = view?.findViewById<ImageView>(R.id.img1lr)
             //imga?.setImageURI(path1)
             Glide.with(context).load(path1).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(imga)
             }

            (view?.findViewById<TextView>(R.id.emaillr))?.text=carro.email
            (view?.findViewById<TextView>(R.id.campo1lr))?.text=carro.campo1
            (view?.findViewById<TextView>(R.id.campo2lr))?.text=carro.campo2
            (view?.findViewById<TextView>(R.id.campo3lr))?.text=carro.campo3
            (view?.findViewById<TextView>(R.id.campo4lr))?.text=carro.campo4
            (view?.findViewById<TextView>(R.id.campo5lr))?.text=carro.campo5
            // Adiciona o evento de clique na linha

            */
            setOnClickListener { onClick(carro) }
            setOnLongClickListener { onLongClickListener(carro) }
        }
    }
    // ViewHolder fica vazio pois usamos o import do Android Kotlin Extensions
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }



}