package com.example.carlos.projetocultural.domain

/**
 * Created by carlo on 16/03/2018.
 */

import com.example.carlos.projetocultural.PubuserApplication
import com.example.carlos.projetocultural.domain.dao.DatabaseManager


object PubuserService {
    fun getPubuser(): List<Pubuser> {
        val dao = DatabaseManager.getPubuserDAO()
        val pubuser = dao.findAll()
        return pubuser
    }


    fun getPubuserId(arg0:Int): Pubuser? {
        val dao = DatabaseManager.getPubuserDAO()
        val pubuser = dao.getById(arg0)
        return pubuser
    }


    fun updateuser(pubuser: Pubuser?): Boolean? {
        val dao = DatabaseManager.getPubuserDAO()
        if(pubuser != null){
            val pubt = dao.updatePubuser(pubuser)
            return true
        }else{
            return false
        }
    }

    // Verifica se uma publicacao existe
    fun isExiste(pubuser: Pubuser?) : Boolean {
        val dao = DatabaseManager.getPubuserDAO()
        val arg0 = pubuser!!.id
        val exists = dao.getById(arg0) != null
        return exists
    }
    // Salva a pubuser
    fun salvar(pubuser: Pubuser): Boolean {
        val dao = DatabaseManager.getPubuserDAO()
        val favorito = isExiste(pubuser)
        if(favorito) {
            // já existe
            return false
        }
        // Adiciona nos favoritos
        dao.insert(pubuser)
        return true
    }

    //deleta uma pubuser
    fun deletepubuser(pubuser: Pubuser?):Boolean{
        val dao = DatabaseManager.getPubuserDAO()
        val favorito = isExiste(pubuser)
        if(favorito) {
            // Remove dos favoritos
            dao.delete(pubuser)
            return true
        }else{
            return false
        }
    }
}