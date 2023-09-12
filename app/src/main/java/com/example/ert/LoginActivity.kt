package com.example.ert

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.User.MainActivity
import com.example.ert.databinding.ActivityLoginBinding
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""
    val NAMA = "nama"
    val DEF_NAMA = ""
    val LOGIN = "isLoggedIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()

        urlClass = UrlClass()

        b.btnLogin.setOnClickListener {
            if (!b.loginUsername.text.toString().equals("") && !b.loginPassword.text.toString().equals("")){
                validationAccount("login")
            }else{
                Toast.makeText(this,"Username atau Password tidak boleh kosong!", Toast.LENGTH_LONG).show()
            }
        }

        b.btnRegis.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        b.btnNext.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LandingPageActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay)
    }

    private fun validationAccount(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val level = jsonObject.getString("level")
                val kd = jsonObject.getString("kd_user")
                val nm = jsonObject.getString("nama")
                if (level.equals("User")) {
                    preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val prefEditor = preferences.edit()
                    prefEditor.putString(USER, kd)
                    prefEditor.putString(NAMA, nm)
                    prefEditor.putBoolean(LOGIN, true)
                    prefEditor.commit()

                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                    finishAffinity()
                } else if (level.equals("Admin")) {
                    preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val prefEditor = preferences.edit()
                    prefEditor.putString(USER, kd)
                    prefEditor.putString(NAMA, nm)
                    prefEditor.putBoolean(LOGIN, false)
                    prefEditor.commit()

                    startActivity(Intent(this, com.example.ert.Admin.MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                    finishAffinity()
                } else {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.warning)
                        .setTitle("Peringatan!")
                        .setMessage("Username dan Password yang Anda masukkan salah!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            null
                        })
                        .show()
                    true
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(this)
                    .setTitle("Peringatan!")
                    .setMessage("Tidak dapat terhubung ke server")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                        null
                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "login" -> {
                        hm.put("mode", "login")
                        hm.put("username", b.loginUsername.text.toString())
                        hm.put("password", b.loginPassword.text.toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}