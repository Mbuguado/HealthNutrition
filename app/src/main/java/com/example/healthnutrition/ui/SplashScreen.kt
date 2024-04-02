package com.example.healthnutrition.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.healthnutrition.R
import com.example.healthnutrition.auth.SignIn

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //HIDE THE ACTION BAR
        supportActionBar?.hide()
        val windowInsetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        //ANIMATE THE LOGO
        animateLogo()

        Handler().postDelayed({
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun animateLogo() {
        val imageView = findViewById<ImageView>(R.id.logo)
        ObjectAnimator.ofFloat(imageView, "scaleX", 0.5f, 1f)
            .apply {
                duration = 1000
                interpolator = AccelerateInterpolator()
            }.start()
        ObjectAnimator.ofFloat(imageView, "scaleY", 0.6f, 1f)
            .apply {
                duration = 1000
                interpolator = AccelerateInterpolator()
            }.start()
    }
}