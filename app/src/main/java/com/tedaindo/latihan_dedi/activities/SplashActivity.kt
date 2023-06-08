package com.tedaindo.latihan_dedi.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.tedaindo.latihan_dedi.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val SPLASHTIMEOUT: Long = 800

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, UtamaActivity::class.java))
            finish()
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }, SPLASHTIMEOUT)

    }
}