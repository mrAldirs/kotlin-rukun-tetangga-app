package com.example.ert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.ert.User.MainActivity
import com.example.ert.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var b: ActivitySplashBinding

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val LOGIN = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()

        // Cek apakah pengguna sudah login
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val isLoggedIn = preferences.getBoolean(LOGIN, false)

        // Redirect ke activity yang sesuai
        val targetActivity = if (isLoggedIn) MainActivity::class.java else LandingPageActivity::class.java
        val intent = Intent(this, targetActivity)
        startActivity(intent)
        finish()
    }
}