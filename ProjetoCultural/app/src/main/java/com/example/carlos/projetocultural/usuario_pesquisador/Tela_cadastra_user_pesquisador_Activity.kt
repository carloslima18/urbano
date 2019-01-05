package com.example.carlos.projetocultural.usuario_pesquisador

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.AndroidUtils

import kotlinx.android.synthetic.main.activity_tela_cadastra_user_pesquisador.*
import kotlinx.android.synthetic.main.content_tela_cadastra_user_pesquisador.*
import org.json.JSONObject
import com.example.carlos.projetocultural.utils.Validacpf


class Tela_cadastra_user_pesquisador_Activity : AppCompatActivity() {

    var nomecad :String = ""
    var cpfcad :String = ""
    var emailcad :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_cadastra_user_pesquisador)
        setSupportActionBar(toolbarcadastrapesq)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

       /* fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        } */
    }

    override fun onResume() {
        super.onResume()
        buttoncadastropesq.setOnClickListener {
            nomecad = nomeedcad.text.toString()
            cpfcad = cpfedcad.text.toString()
            emailcad = emailedcd.text.toString()
            if( Validacpf().validateEmailFormat(emailcad)) {
                if (Validacpf().isCPF(cpfcad)) {
                    if (nomecad != "" && cpfcad != "" && emailcad != "") {
                        if(AndroidUtils.isNetworkAvailable(applicationContext)) {
                            onClickaceitacontrato_para_ser_pesquisador()
                        }else{
                            toast("Sem conexão.")
                        }
                    } else {
                        toast("todos dados devem ser preenchidos")
                    }
                } else {
                    toast("CPF INVALIDO!!")
                }
            }else{
                toast("E-MAIL INVALIDO!!")
            }

        }
    }


    fun envia_pedido_de_usuario_para_ser_pesquisador(){
        toast(nomecad)
        val dialog = ProgressDialog.show(this, "Um momento", "Enviando requisição", false, true)
        dialog.setCancelable(false);
            val data = JSONObject();
            data.put("nome", nomecad)
            data.put("email", emailcad)
            data.put("senha", cpfcad)
            data.put("campo1", cpfcad)
            data.put("campo2", "N")
            toast("Um momento, aguarde enquanto enviamos sua requisisção..", Toast.LENGTH_LONG)
            val handle = Handler()
            Thread {
                //PARA MANTER QUE O USUARIO SÓ AVALIE UMA VEZ, VOU FAZER UMA TABELA COM OS IDS DAS PUBLICAÇAOE QUE ELE JÁ AVALIOU NO BANCO LOCAL E VERIFICAR
                pubService.save_candidato_pesquisador(data)
                runOnUiThread {
                    toast("Pedido enviado, aguarde nosso contato. Obrigado.", Toast.LENGTH_LONG)
                    dialog.dismiss()
                    finish()
                }
            }.start()
    }

    fun onClickaceitacontrato_para_ser_pesquisador() {
        if (AndroidUtils.isNetworkAvailable(applicationContext)) {
            val ft = supportFragmentManager.beginTransaction()
            val fragAnterior = supportFragmentManager.findFragmentByTag(Tela_mensagem_pedido_cadastro_user_pesquisador_DialogFragment.KEY)
            if (fragAnterior != null) {
                ft.remove(fragAnterior)
            }
            ft.addToBackStack(null)
            val bundle = Bundle()//bundle para passagem de parametros(dados a baixo) para outra atv
            bundle.putString("nome", nomecad.toString())
            bundle.putString("email", emailcad.toString())
            bundle.putString("senha", cpfcad.toString())
            bundle.putString("campo1", cpfcad.toString())
            //bundle.putString("campo2", "N")
            val dialog = Tela_mensagem_pedido_cadastro_user_pesquisador_DialogFragment()//estancia o fragment para passar os dados do bundle
            dialog.arguments = bundle
            dialog.show(ft, Tela_mensagem_pedido_cadastro_user_pesquisador_DialogFragment.KEY)//chama o fragment
        }else{
            toast("Sem Conexão")
        }
    }


}
