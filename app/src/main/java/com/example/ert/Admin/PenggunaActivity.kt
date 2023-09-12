package com.example.ert.Admin

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.Adapter.AdapterPengguna
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityPenggunaBinding
import org.json.JSONArray

class PenggunaActivity : AppCompatActivity() {
    private lateinit var b: ActivityPenggunaBinding

    lateinit var urlClass: UrlClass
    val dataPengguna = mutableListOf<HashMap<String,String>>()
    lateinit var penggunaAdp : AdapterPengguna

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPenggunaBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Daftar Pengguna")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        penggunaAdp = AdapterPengguna(dataPengguna, this)
        b.rvPengguna.layoutManager = LinearLayoutManager(this)
        b.rvPengguna.adapter = penggunaAdp
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
        showDataPengguna("show_data_pengguna", "")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showDataPengguna("show_data_pengguna", query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                showDataPengguna("show_data_pengguna", newText)
                return true
            }
        })
        return true
    }

    private fun showDataPengguna(mode: String, nama: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.profil,
            Response.Listener { response ->
                dataPengguna.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("nama",jsonObject.getString("nama"))
                        frm.put("no_hp",jsonObject.getString("no_hp"))
                        frm.put("img",jsonObject.getString("img"))
                        frm.put("kd_user",jsonObject.getString("kd_user"))

                        dataPengguna.add(frm)
                    }
                    penggunaAdp.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_pengguna" -> {
                        hm.put("mode","show_data_pengguna")
                        hm.put("nama",nama)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}