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
import com.example.carlos.projetocultural.domain.Pubuser

/**
 * Created by carlo on 16/03/2018.
 */
// Define o construtor que recebe (carros,onClick)
class Tela_listagem_pub_user_comum_Adapter(val carros: List<Pubuser>, val onClick: (Pubuser) -> Unit, val onLongClickListener: (Pubuser) -> Boolean) : RecyclerView.Adapter<Tela_listagem_pub_user_comum_Adapter.ViewHolder>() {
    // Retorna a quantidade de carros na lista
    override fun getItemCount(): Int {
        return this.carros.size
    }
    // Infla o layout do adapter e retorna o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla a view do adapter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_da_listagem_pub_user_comum_e_pesquisador, parent, false)
        // Retorna o ViewHolder que contém todas as views
        val holder = ViewHolder(view)
        return holder
    }
    // Faz o bind para atualizar o valor das views com os dados do Carro
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Recupera o objeto carro
        val carro = carros[position]
        val view = holder.itemView
        with(view) {

            if(carro.img1 != null) {
                val path1 = Uri.parse(carro.img1)
                val imga = view?.findViewById<ImageView>(R.id.img1lr)
                //imga?.setImageURI(path1)
               // Glide.with(context).load(path1).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imga)
                Glide.with(context)
                        .asBitmap()
                        .load(path1)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imga)
            }
            if(carro.img2 != null) {
                val path2 = Uri.parse(carro.img2)
                val imgb = view?.findViewById<ImageView>(R.id.img2lr)
              //  Glide.with(context).load(path2).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgb)
                //imgb?.setImageURI(path2)
                Glide.with(context)
                        .asBitmap()
                        .load(path2)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imgb)
            }
            if(carro.img3 != null){
                val path3 = Uri.parse(carro.img3)
                val imgc = view?.findViewById<ImageView>(R.id.img3lr)
              //  Glide.with(context).load(path3).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgc)
                //   imgc?.setImageURI(path3)
                Glide.with(context)
                        .asBitmap()
                        .load(path3)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imgc)
            }
            if(carro.img4 != null){
                val path4 = Uri.parse(carro.img4)
                val imgd = view?.findViewById<ImageView>(R.id.img4lr)
               // Glide.with(context).load(path4).asBitmap().override(100, 100).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgd)
                //imgd?.setImageURI(path4)
                Glide.with(context)
                        .asBitmap()
                        .load(path4)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(imgd)
            }

            (view?.findViewById<TextView>(R.id.nomelr))?.text= carro.nome
            (view?.findViewById<TextView>(R.id.categorialr))?.text=carro.categoria
            (view?.findViewById<TextView>(R.id.enderecolr))?.text=carro.endereco
            (view?.findViewById<TextView>(R.id.contatolr))?.text=carro.contato
            (view?.findViewById<TextView>(R.id.redesociallr))?.text=carro.redesocial
            (view?.findViewById<TextView>(R.id.atvexlr))?.text=carro.atvexercida

            (view?.findViewById<TextView>(R.id.emaillr))?.text=carro.email
            (view?.findViewById<TextView>(R.id.campo1lr))?.text=carro.campo1
            (view?.findViewById<TextView>(R.id.campo2lr))?.text=carro.campo2
            (view?.findViewById<TextView>(R.id.campo3lr))?.text=carro.campo3
            (view?.findViewById<TextView>(R.id.campo4lr))?.text=carro.campo4
            (view?.findViewById<TextView>(R.id.campo5lr))?.text=carro.campo5
            // Adiciona o evento de clique na linha
            setOnClickListener { onClick(carro) }
            setOnLongClickListener { onLongClickListener(carro) }
        }
    }
    // ViewHolder fica vazio pois usamos o import do Android Kotlin Extensions
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}