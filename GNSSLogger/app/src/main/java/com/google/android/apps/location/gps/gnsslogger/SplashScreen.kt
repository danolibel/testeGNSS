package com.google.android.apps.location.gps.gnsslogger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.apps.location.gps.gnsslogger.ui.login.LoginActivity


class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val handler = Handler()
        handler.postDelayed({ mostrarMainActivity() }, 2000)
    }

    private fun mostrarMainActivity() {
        val intent = Intent(
            this@SplashScreen, LoginActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}