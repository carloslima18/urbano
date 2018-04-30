package com.example.carlos.projetocultural
import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.AndroidUtils
import kotlinx.android.synthetic.main.content_avaliacao.*
import kotlinx.android.synthetic.main.list_row_main.*
import org.json.JSONObject

class AvaliacaoActivity : AppCompatActivity() {

    private var ratingBar: RatingBar? = null
    private var txtValorAvaliacao: TextView? = null
    private var btnSubmit: Button? = null


    var id :String ?=null
    var pesquisador :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_avaliacao)
        setSupportActionBar(toolbaravl)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        toolbaravl.setNavigationOnClickListener {
            finish()
        }
        val extras = intent.extras
        id = extras.getString("idpub")
        pesquisador = extras.getString("pesquisador")

    }

    override fun onResume() {
        super.onResume()
        val bt = findViewById<Button>(R.id.btnSubmit) as Button
        val rating = findViewById<RatingBar>(R.id.ratingBar) as RatingBar

            bt.setOnClickListener {
                if (AndroidUtils.isNetworkAvailable(applicationContext)) {
                val valor = rating.getRating()
                val data = JSONObject();
                val dialog = ProgressDialog.show(this, "Um momento", "Enviando sua avaliação", false, true)
                dialog.setCancelable(false);

                if (pesquisador == "") {
                    data.put("nota", valor.toInt())
                    data.put("idpubpesq", id?.toInt())

                    Thread {
                        //PARA MANTER QUE O USUARIO SÓ AVALIE UMA VEZ, VOU FAZER UMA TABELA COM OS IDS DAS PUBLICAÇAOE QUE ELE JÁ AVALIOU NO BANCO LOCAL E VERIFICAR
                        pubService.saveAvaliacaouser(data)
                        runOnUiThread {
                            dialog.dismiss()
                            toast("avaliação enviada. Obrigado.")
                            finish()
                        }
                    }.start()

                } else {
                    Thread {
                        //PARA MANTER QUE O USUARIO SÓ AVALIE UMA VEZ, VOU FAZER UMA TABELA COM OS IDS DAS PUBLICAÇAOE QUE ELE JÁ AVALIOU NO BANCO LOCAL E VERIFICAR
                        data.put("nota", valor.toInt())
                        data.put("idpubpesq", id?.toInt())
                        pubService.saveAvaliacaopesq(data)
                        runOnUiThread {
                            dialog.dismiss()
                            toast("avaliação enviada. Obrigado Pesquisador.")
                            finish()
                        }
                    }.start()
                }

            }else{
                    toast("Sem conexão.")
                }

        }
    }



}
