package com.example.carlos.projetocultural.domain.dao

/**
 * Created by carlo on 18/03/2018.
 */
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.carlos.projetocultural.domain.Pubpesq

@Dao
interface PubpesqDAO {
    @Query("SELECT * FROM pubpesq where id = :id")
    fun getById(id: Int): Pubpesq?

    @Query("SELECT * FROM pubpesq")
    fun findAll(): List<Pubpesq>

    @Update(onConflict = REPLACE)
    fun updatePubpesq(pubpesq: Pubpesq?)

    @Insert
    fun insert(pubpesq: Pubpesq?)

    @Delete
    fun delete(pubpesq: Pubpesq?)
}