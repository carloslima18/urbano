package com.example.carlos.projetocultural.telas_ajudas_recados_informacoes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent


import android.os.Handler
import com.example.carlos.projetocultural.MainActivity
import com.example.carlos.projetocultural.R

/**
 * A sample splash screen created by devdeeds.com
 * by Jayakrishnan P.M
 */
class Tela_splash_Activity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 2000 //3 seconds

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("x","true")
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}
