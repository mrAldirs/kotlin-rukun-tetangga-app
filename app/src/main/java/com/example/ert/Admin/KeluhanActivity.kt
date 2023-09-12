package com.example.ert.Admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.Adapter.AdapterKeluhan
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityKeluhanAdminBinding
import org.json.JSONArray

class KeluhanActivity : AppCompatActivity() {
    private lateinit var b: ActivityKeluhanAdminBinding

    lateinit var urlClass: UrlClass
    val dataKeluhan = mutableListOf<HashMap<String,String>>()
    lateinit var keluhanAdp : AdapterKeluhan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKeluhanAdminBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Keluhan dan Masukan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        keluhanAdp = AdapterKeluhan(dataKeluhan, this)
        b.rvKeluhan.layoutManager = LinearLayoutManager(this)
        b.rvKeluhan.adapter = keluhanAdp
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataKeluhan("show_data_keluhan")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun showDataKeluhan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.keluhan,
            Response.Listener { response ->
                dataKeluhan.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("teks_keluhan",jsonObject.getString("teks_keluhan"))
                        frm.put("img",jsonObject.getString("img"))
                        frm.put("kd_keluhan",jsonObject.getString("kd_keluhan"))

                        dataKeluhan.add(frm)
                    }
                    keluhanAdp.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_keluhan" -> {
                        hm.put("mode","show_data_keluhan")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}