package com.rmgstudios.hapori

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        val splashScreenLogo = findViewById<ImageView>(R.id.splashscreenLogo)
        changeLogoSize(splashScreenLogo)

        val SPLASH_TIME_OUT = 2000
        Handler(Looper.getMainLooper()).postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    private fun changeLogoSize(haporiLogo: ImageView) {
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            this.display!!.getRealMetrics(displayMetrics)
        } else this.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        haporiLogo.layoutParams.width = ((0.8 * screenWidth).toInt())
        haporiLogo.layoutParams.height = ((0.8 * screenHeight).toInt())
    }
}