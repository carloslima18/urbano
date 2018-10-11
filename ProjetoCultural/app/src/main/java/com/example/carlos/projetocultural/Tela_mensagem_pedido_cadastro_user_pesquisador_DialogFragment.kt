package com.example.carlos.projetocultural


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.tela_mensagem_pedido_cadastro_user_pesquisador_dialogfragment.*


/**
 * A fragment with a Google +1 button.
 */
class Tela_mensagem_pedido_cadastro_user_pesquisador_DialogFragment : DialogFragment() {

    var dialogvariavel:Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.tela_mensagem_pedido_cadastro_user_pesquisador_dialogfragment, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        btaceitar.setOnClickListener {
            (context as Tela_cadastra_user_pesquisador_Activity).envia_pedido_de_usuario_para_ser_pesquisador()
            dismiss()
        }
        btvoltar.setOnClickListener{
            dialogvariavel = false
            dismiss()
        }
    }

    companion object {
        val KEY = "info_cadastra_pesq"
    }


}
