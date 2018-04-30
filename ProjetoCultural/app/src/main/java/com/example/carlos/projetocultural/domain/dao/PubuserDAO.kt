package com.example.carlos.projetocultural.domain.dao

/**
 * Created by carlo on 16/03/2018.
 */


import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.carlos.projetocultural.domain.Pubuser

@Dao
interface PubuserDAO {
    @Query("SELECT * FROM pubuser where id = :id")
    fun getById(id: Int): Pubuser?

    @Query("SELECT * FROM pubuser")
    fun findAll(): List<Pubuser>


    //@Query("SELECT latitude,latitude,nome,img1 FROM pubuser")
    //fun findMapa(): List<Pubuser>

    @Update(onConflict = REPLACE)
    fun updatePubuser(pubuser: Pubuser?)

    @Insert
    fun insert(pubuser: Pubuser)

    @Delete
    fun delete(pubuser: Pubuser?)
}