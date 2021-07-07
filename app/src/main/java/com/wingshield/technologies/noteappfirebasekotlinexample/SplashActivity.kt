package com.wingshield.technologies.noteappfirebasekotlinexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.viewbinding.library.activity.viewBinding
import com.wingshield.technologies.noteappfirebasekotlinexample.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding:ActivitySplashBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            startActivity(Intent(this,MainActivity::class.java))
        },2000)
    }
}