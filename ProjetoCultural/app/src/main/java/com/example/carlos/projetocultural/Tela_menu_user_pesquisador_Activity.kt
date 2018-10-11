package com.example.carlos.projetocultural


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.utils.MapUtils
import kotlinx.android.synthetic.main.activity_tela_menu_user_pesquisador.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.toast

class Tela_menu_user_pesquisador_Activity : AppCompatActivity() {
    var database: MyDatabaseOpenHelper?=null
    var idpesq:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_menu_user_pesquisador)
        setSupportActionBar(toolbarHome)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        toolbarHome.setNavigationOnClickListener {
            finish()
        }
        val extras = intent.extras
     //   idpesq = extras?.getInt("idpesq").toString()
        database = MyDatabaseOpenHelper.getInstance(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        buttonviewpubpesq.setOnClickListener {
            val intent = Intent(this@Tela_menu_user_pesquisador_Activity, Tela_listagem_publicacao_cadastradas_user_pesquisador_Activity::class.java)
         //   intent.putExtra("idpesq",idpesq)
            startActivity(intent)
        }
        buttonaddpubpesq.setOnClickListener {
            val provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(provider == null){
                toast("Ligue seu GPS")
            }else {
                val intent = Intent(this@Tela_menu_user_pesquisador_Activity, Tela_formulario_user_pesquisador_Activity::class.java)
             //   intent.putExtra("idpesq", idpesq)
                startActivity(intent)
            }
        }
        buttonviewmappubpesq.setOnClickListener {
            val mapUtils=MapUtils()
            mapUtils.mapaoffline("pesq",this)
        }
        sair.setOnClickListener {
            val id = database?.use {
                select("pesquisador", "nome").exec { parseList<String>(classParser()) }
            }

            if(id != null && id.size != 0) {
                database?.use {
                    delete("pesquisador", "nome = \"" + id[0] + "\"")
                }
                finish()
            }else{
                toast("erro no BD, comunique o admin")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
