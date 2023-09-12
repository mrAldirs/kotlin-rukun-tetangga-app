package com.example.ert.Admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityKeluhanDetailBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class KeluhanDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityKeluhanDetailBinding

    lateinit var urlClass: UrlClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKeluhanDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Detail Keluhan dan Masukan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    override fun onStart() {
        super.onStart()
        detail("detail")
    }

    private fun detail(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.keluhan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama")
                val st2 = jsonObject.getString("teks_keluhan")
                val st3 = jsonObject.getString("tgl_keluhan")
                val st4 = jsonObject.getString("img_keluhan")

                b.detailNama.setText(st1)
                b.detailKeluhan.setText(st2)
                b.detailTgl.setText(st3)
                if (st4.toString().equals("null")) {
                    b.detailImage.visibility = View.GONE
                } else {
                    Picasso.get().load(st4).into(b.detailImage)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server!", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "detail" -> {
                        hm.put("mode","detail")
                        hm.put("kd_keluhan", paket?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}