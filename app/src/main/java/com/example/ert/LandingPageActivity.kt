package com.example.ert

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.ert.User.ProfilActivity
import com.example.ert.databinding.ActivityLandingPageBinding

class LandingPageActivity : AppCompatActivity() {
    private lateinit var b: ActivityLandingPageBinding
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var urlClass: UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Dashboard")

        urlClass = UrlClass()

        toggle = ActionBarDrawerToggle(this, b.drawerLayout, R.string.open, R.string.close)
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        b.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_homeLogin -> {
                    b.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_akunLogin -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_registrasiLogin -> {
                    startActivity(Intent(this, RegisterActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_kegiatanLogin -> {
                    dialog()
                }
                R.id.nav_keuanganLogin -> {
                    dialog()
                }
                R.id.nav_laporanLogin -> {
                    dialog()
                }
                R.id.nav_keluhanLogin -> {
                    dialog()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (b.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            b.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun dialog() {
        AlertDialog.Builder(this)
            .setTitle("Peringatan!")
            .setIcon(R.drawable.warning)
            .setMessage("Silahkan login terlebih dahulu untuk melihat fitur aplikasi!")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                null
            })
            .show()
    }
}