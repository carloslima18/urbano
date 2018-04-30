package com.example.carlos.projetocultural.domain.dao

/**
 * Created by carlo on 16/03/2018.
 */
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.carlos.projetocultural.domain.Pubuser
import com.example.carlos.projetocultural.domain.Pubpesq


// Define as classes que precisam ser persistidas e a versão do banco
@Database(entities = arrayOf(Pubuser::class), version = 3)
abstract class PubuserDatabase :RoomDatabase() {
    abstract fun pubuserDAO(): PubuserDAO
}
