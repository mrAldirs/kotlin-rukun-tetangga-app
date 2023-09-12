package com.example.ert

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.databinding.ActivityRegisterBinding
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding
    lateinit var urlClass : UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()

        urlClass = UrlClass()

        b.btnRegis.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Registrasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda yakin ingin membuat Akun baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    if (b.regisNama.text.toString().equals("") || b.regisNoHp.text.toString().equals("") ||
                        b.regisUsername.text.toString().equals("") || b.regisPassword.text.toString().equals("")) {
                        Toast.makeText(this, "Tolong isi data dengan benar!", Toast.LENGTH_SHORT).show()
                    } else {
                        registrasi("regis")
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }

        b.btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.stay)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun registrasi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("0")) {
                    Toast.makeText(this, "Username telah digunakan!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("1")) {
                    Toast.makeText(this, "Nomor Handphone telah terdaftar!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("2")) {
                    Toast.makeText(this, "Nama Anda telah terdaftar!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("3")) {
                    Toast.makeText(this, "Berhasil mendaftarkan Akun!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.stay)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "regis"->{
                        hm.put("mode","regis")
                        hm.put("nama", b.regisNama.text.toString())
                        hm.put("no_hp", b.regisNoHp.text.toString())
                        hm.put("username", b.regisUsername.text.toString())
                        hm.put("password", b.regisPassword.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}