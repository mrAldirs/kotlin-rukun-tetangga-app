package com.example.ert.Admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityLaporanBinding
import org.json.JSONArray

class LaporanActivity : AppCompatActivity() {
    private lateinit var b: ActivityLaporanBinding

    lateinit var urlClass: UrlClass

    val dataLaporan = mutableListOf<HashMap<String,String>>()
    lateinit var laporanAdp : AdapterLaporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Laporan Keuangan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        laporanAdp = AdapterLaporan(dataLaporan, this)
        b.rvLaporan.layoutManager = LinearLayoutManager(this)
        b.rvLaporan.adapter = laporanAdp

        b.btnTambah.setOnClickListener {
            startActivity(Intent(this, LaporanTambahActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        b.judul.setText("Kelola \nKeuangan \nRT Anda!")
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showDataLaporan("show_data_laporan", query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                showDataLaporan("show_data_laporan", newText)
                return true
            }
        })
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataLaporan("show_data_laporan", "")
    }

    private fun showDataLaporan(mode: String, tgl: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                dataLaporan.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("total",jsonObject.getString("total"))
                        frm.put("kd_laporan",jsonObject.getString("kd_laporan"))
                        frm.put("tgl_laporan",jsonObject.getString("tgl_laporan"))

                        dataLaporan.add(frm)
                    }
                    laporanAdp.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_laporan" -> {
                        hm.put("mode","show_data_laporan")
                        hm.put("tgl_laporan", tgl)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class AdapterLaporan(val dataLaporan: List<HashMap<String,String>>, val parent: LaporanActivity) :
        RecyclerView.Adapter<AdapterLaporan.HolderDataLaporan>(){
        class HolderDataLaporan (v: View) : RecyclerView.ViewHolder(v) {
            val tt = v.findViewById<TextView>(R.id.laporanTotal)
            val tgl = v.findViewById<TextView>(R.id.laporanTanggal)
            val dt = v.findViewById<TextView>(R.id.btnDetail)
            val cd = v.findViewById<CardView>(R.id.cardLaporan)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataLaporan {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_laporan, parent, false)
            return HolderDataLaporan(v)
        }

        override fun getItemCount(): Int {
            return dataLaporan.size
        }

        override fun onBindViewHolder(holder: HolderDataLaporan, position: Int) {
            val data = dataLaporan.get(position)
            holder.tgl.setText(data.get("tgl_laporan"))
            holder.tt.setText(" Sisa Kas RT : Rp. "+data.get("total"))
            holder.dt.setOnClickListener {
                val intent = Intent(it.context, LaporanDetailActivity::class.java)
                intent.putExtra("kode", data.get("kd_laporan").toString())
                it.context.startActivity(intent)
                parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
            }
        }
    }
}