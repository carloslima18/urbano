package com.example.carlos.projetocultural.domain

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.ListView
import com.example.carlos.projetocultural.MainActivity
import com.example.carlos.projetocultural.R
import org.jetbrains.anko.toast
import org.json.JSONObject
import android.widget.Toast
import com.example.carlos.projetocultural.adapters.HomeAdapter


/**
 * Created by carlo on 16/02/2018.
 */
class GetRequisitaPub(context: Context, listV: RecyclerView, param:String) : AsyncTask<Unit, Unit, String>() {

    val lv = listV
    var context = context
    var param = param//192.168.137.113
    var id = MainActivity().ipconfig
    val URL = "http://orbeapp.com/web/sendpubuser?_format=json$param"

    override fun onPreExecute(){
        super.onPreExecute()
        context.toast("aguarde..")
    }
    var listItems :List<Pubpesq> = arrayListOf()
    override fun doInBackground(vararg params: Unit?): String? {
     //   listItems = pubService.getPub(URL)
        return null
    }
    override fun onPostExecute(result: String?):Unit {
        super.onPostExecute(result)
        if(listItems.size != 0) {

           // lv.adapter = HomeAdapter(listItems,context, R.layout.list_row_main, R.id.textViewnomeM, listItems)
            context.toast("Concluido")
        }
        else{
            context.toast("nenhum item dísponivel!")
        }

    }


/*
    fun moreData(listapub:Listadapter) {
        try {


                    if (listapub.size() > 0) {
                        if (noticiaLA == null) {
                            listaNoticias = lista
                            noticiaLA = NoticiaListAdajpter(getView().getContext(), listaNoticias)
                            lvNoticias.setAdapter(noticiaLA)
                        } else {
                            noticiaLA.changeLista(lista)
                        }
                        Log.w("MORE_DATA->", noticiaLA.getCount() + "")
                    } else {
                        Toast.makeText(getView().getContext(), "Nenhuma noticia encontrada", Toast.LENGTH_SHORT).show()
                    }


            CustomVolleySingleton.getInstance().addToRequestQueue(app)
        } catch (e: Exception) {
            Log.e("ERROR: " + AppCompatActivity.TAG, "Method: " + "getAllNoticias: " + e)
        }

    } */























}