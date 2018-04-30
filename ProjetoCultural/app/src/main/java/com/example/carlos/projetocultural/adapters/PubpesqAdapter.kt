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
import com.bumptech.glide.request.RequestOptions
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.domain.Pubpesq
/**
 * Created by carlo on 18/03/2018.
 */
// Define o construtor que recebe (carros,onClick)
class PubpesqAdapter(val listpubpesq: List<Pubpesq>, val onClick: (Pubpesq) -> Unit, val onLongClickListener: (Pubpesq) -> Boolean) : RecyclerView.Adapter<PubpesqAdapter.ViewHolder>() {
    // Retorna a quantidade de carros na lista
    override fun getItemCount(): Int {
        return this.listpubpesq.size
    }
    // Infla o layout do adapter e retorna o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla a view do adapter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        // Retorna o ViewHolder que contém todas as views
        val holder = ViewHolder(view)
        return holder
    }
    // Faz o bind para atualizar o valor das views com os dados do Carro
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Recupera o objeto carro
        val listpub = listpubpesq[position]
        val view = holder.itemView
        with(view) {

            if(listpub.img1 != null) {
                val path1 = Uri.parse(listpub.img1)
                val imga = view?.findViewById<ImageView>(R.id.img1lr)
                //imga?.setImageURI(path1)
               // Glide.with(context).load(path1).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imga)
                Glide.with(context)
                        .asBitmap()
                        .load(path1)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imga)
            }
            if(listpub.img2 != null) {
                val path2 = Uri.parse(listpub.img2)
                val imgb = view?.findViewById<ImageView>(R.id.img2lr)
             //   Glide.with(context).load(path2).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgb)
                //imgb?.setImageURI(path2)
                Glide.with(context)
                        .asBitmap()
                        .load(path2)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imgb)
            }
            if(listpub.img3 != null){
                val path3 = Uri.parse(listpub.img3)
                val imgc = view?.findViewById<ImageView>(R.id.img3lr)
                //Glide.with(context).load(path3).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgc)
                //   imgc?.setImageURI(path3)
                Glide.with(context)
                        .asBitmap()
                        .load(path3)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imgc)
            }
            if(listpub.img4 != null){
                val path4 = Uri.parse(listpub.img4)
                val imgd = view?.findViewById<ImageView>(R.id.img4lr)
               // Glide.with(context).load(path4).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgd)
                //imgd?.setImageURI(path4)
                Glide.with(context)
                        .asBitmap()
                        .load(path4)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imgd)
            }

            (view?.findViewById<TextView>(R.id.nomelr))?.text= listpub.nome
            (view?.findViewById<TextView>(R.id.categorialr))?.text=listpub.categoria
            (view?.findViewById<TextView>(R.id.enderecolr))?.text=listpub.endereco
            (view?.findViewById<TextView>(R.id.contatolr))?.text=listpub.contato
            (view?.findViewById<TextView>(R.id.redesociallr))?.text=listpub.redesocial
            (view?.findViewById<TextView>(R.id.atvexlr))?.text=listpub.atvexercida

            (view?.findViewById<TextView>(R.id.representacaolr))?.text=listpub.representacao
            (view?.findViewById<TextView>(R.id.recursolr))?.text=listpub.recurso
            (view?.findViewById<TextView>(R.id.anoiniciolr))?.text=listpub.anoinicio
            (view?.findViewById<TextView>(R.id.cnpjlr))?.text=listpub.cnpj
            (view?.findViewById<TextView>(R.id.pesquisadorlr))?.text=listpub.pesquisador.toString()

            (view?.findViewById<TextView>(R.id.emaillr))?.text=listpub.email

            (view?.findViewById<TextView>(R.id.campo1lr))?.text=listpub.campo1
            (view?.findViewById<TextView>(R.id.campo2lr))?.text=listpub.campo2
            (view?.findViewById<TextView>(R.id.campo3lr))?.text=listpub.campo3
            (view?.findViewById<TextView>(R.id.campo4lr))?.text=listpub.campo4
            (view?.findViewById<TextView>(R.id.campo5lr))?.text=listpub.campo5

            (view?.findViewById<TextView>(R.id.campo6lr))?.text=listpub.campo6
            (view?.findViewById<TextView>(R.id.campo7lr))?.text=listpub.campo7
            (view?.findViewById<TextView>(R.id.campo8lr))?.text=listpub.campo8
            (view?.findViewById<TextView>(R.id.campo9lr))?.text=listpub.campo9
            (view?.findViewById<TextView>(R.id.campo10lr))?.text=listpub.campo10

            (view?.findViewById<TextView>(R.id.campo11lr))?.text=listpub.campo11
            (view?.findViewById<TextView>(R.id.campo12lr))?.text=listpub.campo12
            (view?.findViewById<TextView>(R.id.campo13lr))?.text=listpub.campo13
            (view?.findViewById<TextView>(R.id.campo14lr))?.text=listpub.campo14
            (view?.findViewById<TextView>(R.id.campo15lr))?.text=listpub.campo15

            (view?.findViewById<TextView>(R.id.campo16lr))?.text=listpub.campo16
            (view?.findViewById<TextView>(R.id.campo17lr))?.text=listpub.campo17
            (view?.findViewById<TextView>(R.id.campol8lr))?.text=listpub.campo18
            (view?.findViewById<TextView>(R.id.campol9lr))?.text=listpub.campo19
            (view?.findViewById<TextView>(R.id.campo20lr))?.text=listpub.campo20



            // Adiciona o evento de clique na linha
            setOnClickListener { onClick(listpub) }
            setOnLongClickListener { onLongClickListener(listpub) }
        }
    }
    // ViewHolder fica vazio pois usamos o import do Android Kotlin Extensions
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}