package com.rmgstudios.hapori

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        val SPLASH_TIME_OUT = 2000
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

}