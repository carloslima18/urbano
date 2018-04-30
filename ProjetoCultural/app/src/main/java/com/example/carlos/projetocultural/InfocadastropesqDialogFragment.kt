package com.example.carlos.projetocultural


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.AndroidUtils

import kotlinx.android.synthetic.main.fragment_infocadastropesq_dialog.*
import org.json.JSONObject


/**
 * A fragment with a Google +1 button.
 */
class InfocadastropesqDialogFragment : DialogFragment() {

    var dialogvariavel:Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_infocadastropesq_dialog, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        btaceitar.setOnClickListener {
            (context as Cadastrapesq_Activity).envia_pedido_de_usuario_para_ser_pesquisador()
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
