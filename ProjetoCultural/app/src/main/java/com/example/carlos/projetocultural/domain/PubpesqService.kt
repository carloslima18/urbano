package com.example.carlos.projetocultural.domain

/**
 * Created by carlo on 18/03/2018.
 */


import com.example.carlos.projetocultural.domain.dao.DatabaseManager


object PubpesqService {
    fun getPubpesq(): List<Pubpesq> {
        val dao = DatabaseManager.getPubpesqDAO()
        val pubpesq = dao.findAll()
        return pubpesq
    }

    fun getPubpesqId(id: Int): Pubpesq? {
        val dao = DatabaseManager.getPubpesqDAO()
        val pubpesq = dao.getById(id)
        return pubpesq
    }

    fun updatepesq(pubpesq: Pubpesq?): Boolean? {
        val dao = DatabaseManager.getPubpesqDAO()
        if (pubpesq != null) {
            val pubt = dao.updatePubpesq(pubpesq)
            return true
        } else {
            return false
        }
    }

    // Verifica se uma publicacao existe
    fun isExiste(pubpesq: Pubpesq?): Boolean {
        val dao = DatabaseManager.getPubpesqDAO()
        val exists = dao.getById(pubpesq!!.id) != null
        return exists
    }

    // Salva a pubuser
    fun salvar(pubpesq: Pubpesq?): Boolean {
        val dao = DatabaseManager.getPubpesqDAO()
        val favorito = isExiste(pubpesq)
        if (favorito) {
            // já existe
            return false
        }
        // Adiciona nos favoritos
        dao.insert(pubpesq)
        return true
    }

    //deleta uma pubuser
    fun deletepesquser(pubpesq: Pubpesq?): Boolean {
        val dao = DatabaseManager.getPubpesqDAO()
        val favorito = isExiste(pubpesq)
        if (favorito) {
            // Remove dos favoritos
            dao.delete(pubpesq)
            return true
        } else {
            return false
        }
    }
}
