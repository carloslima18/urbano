package com.example.carlos.projetocultural.domain.dao

/**
 * Created by carlo on 16/03/2018.
 */

import android.arch.persistence.room.Room
import com.example.carlos.projetocultural.PubuserApplication

//import br.com.livroandroid.carros.CarrosApplication

object DatabaseManager {
    // Singleton do Room: banco de dados
    private var dbInstance: PubuserDatabase
    private var dbInstance2: PubpesqDatabase

    init {
        val appContext = PubuserApplication.getInstance().applicationContext

        // Configura o Room
        dbInstance = Room.databaseBuilder(
                appContext,
                PubuserDatabase::class.java,
                "database-pubuser")
                .build()

        // Configura o Room
        dbInstance2 = Room.databaseBuilder(
                appContext,
                PubpesqDatabase::class.java,
                "database-pubpesq")
                .build()
    }

    fun getPubuserDAO(): PubuserDAO {
        return dbInstance.pubuserDAO()
    }

    fun getPubpesqDAO(): PubpesqDAO {
        return dbInstance2.pubpesqDAO()
    }

}