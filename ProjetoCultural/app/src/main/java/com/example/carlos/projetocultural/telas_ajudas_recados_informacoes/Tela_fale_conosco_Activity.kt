package com.example.carlos.projetocultural.telas_ajudas_recados_informacoes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.carlos.projetocultural.MainActivity
import com.example.carlos.projetocultural.R

import kotlinx.android.synthetic.main.activity_tela_fale_conosco.*
import org.jetbrains.anko.email

class Tela_fale_conosco_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_fale_conosco)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@Tela_fale_conosco_Activity, MainActivity::class.java)
            intent.putExtra("sem splash","0")
            startActivity(intent)
        }

        vermais.visibility = View.VISIBLE
        texto2.visibility = View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()
        buttonComentario.setOnClickListener {
            val assunto = "Contato pelo projeto cultural mobile"
            email("carloslima519@outlook.com", assunto)
        }



        vermais.setOnClickListener{
            texto2.visibility = View.VISIBLE
            texto2.text = resources.getString(R.string.maintext2)
            vermais.visibility = View.INVISIBLE
        }

    }



}

