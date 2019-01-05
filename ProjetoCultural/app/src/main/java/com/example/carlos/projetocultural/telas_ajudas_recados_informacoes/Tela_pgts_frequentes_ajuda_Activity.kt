package com.example.carlos.projetocultural.telas_ajudas_recados_informacoes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.carlos.projetocultural.R
import kotlinx.android.synthetic.main.activity_tela_pgts_frequentes_ajuda.*
import org.jetbrains.anko.email

class Tela_pgts_frequentes_ajuda_Activity : AppCompatActivity() {

    var pert1:Int = 1;
    var pert2:Int = 1;
    var pert3:Int = 1;
    var pert4:Int = 1;
    var pert5:Int = 1;
    var pert6:Int = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_pgts_frequentes_ajuda)
        setSupportActionBar(toolbarnoticias)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

    }

    override fun onResume() {
        super.onResume()

        pgt1.setOnClickListener {
            if(pert1 == 1) {
                resp1.visibility = View.VISIBLE
                pert1--
            }else{
                resp1.visibility = View.GONE
                pert1++
            }
        }
        pgt2.setOnClickListener {
            if(pert2 == 1) {
                resp2.visibility = View.VISIBLE
                pert2--
            }else{
                resp2.visibility = View.GONE
                pert2++
            }
        }

        pgt3.setOnClickListener {
            if(pert3 == 1) {
                resp3.visibility = View.VISIBLE
                pert3--
            }else{
                resp3.visibility = View.GONE
                pert3++
            }
        }

        pgt4.setOnClickListener {
            if(pert4 == 1) {
                resp4.visibility = View.VISIBLE
                pert4--
            }else{
                resp4.visibility = View.GONE
                pert4++
            }
        }

        pgt5.setOnClickListener {
            if(pert5 == 1) {
                resp5.visibility = View.VISIBLE
                pert5--
            }else{
                resp5.visibility = View.GONE
                pert5++
            }
        }

        pgt10.setOnClickListener {
            val assunto = "Dúvidas Urbano"
            email("carloslima519@outlook.com", assunto)
        }


    }
}
