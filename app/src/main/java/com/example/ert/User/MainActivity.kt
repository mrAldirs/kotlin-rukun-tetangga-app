package com.example.ert.User

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.Adapter.AdapterKegiatan
import com.example.ert.Adapter.AdapterLaporan
import com.example.ert.Admin.KegiatanTambahActivity
import com.example.ert.LoginActivity
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityMainUserBinding
import com.example.ert.databinding.NavHeaderBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var b : ActivityMainUserBinding
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var hb: NavHeaderBinding

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""
    val NAMA = "nama"
    val DEF_NAMA = ""
    val LOGIN = "isLoggedIn"

    val dataKegiatan = mutableListOf<HashMap<String,String>>()
    lateinit var kegiatanAdp : AdapterKegiatan

    val dataLaporan = mutableListOf<HashMap<String,String>>()
    lateinit var laporanAdp : AdapterLaporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainUserBinding.inflate(layoutInflater)
        hb = NavHeaderBinding.bind(b.navView.getHeaderView(0))
        setContentView(b.root)
        supportActionBar?.setTitle("Dashboard")

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        kegiatanAdp = AdapterKegiatan(dataKegiatan)
        b.rvKegiatan.layoutManager = LinearLayoutManager(this)
        b.rvKegiatan.adapter = kegiatanAdp

        laporanAdp = AdapterLaporan(dataLaporan)
        b.rvKeuangan.layoutManager = LinearLayoutManager(this)
        b.rvKeuangan.adapter = laporanAdp

        toggle = ActionBarDrawerToggle(this, b.drawerLayout, R.string.open, R.string.close)
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_homeUser -> {
                    b.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_akunUser -> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_insertKegiatan -> {
                    startActivity(Intent(this, KegiatanTambahActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_kegiatanUser -> {
                    startActivity(Intent(this, KegiatanActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_keuanganUser -> {
                    startActivity(Intent(this, LaporanActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_keluhanUser -> {
                    startActivity(Intent(this, KeluhanActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_logoutUser -> {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.warning)
                        .setTitle("Logout")
                        .setMessage("Apakah Anda ingin keluar aplikasi?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                            val prefEditor = preferences.edit()
                            prefEditor.putString(USER,null)
                            prefEditor.putString(NAMA,null)
                            prefEditor.putBoolean(LOGIN,false)
                            prefEditor.commit()

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
                            finishAffinity()
                        })
                        .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                        })
                        .show()
                    true
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
        showDataKegiatan("show_data_kegiatan")
        showDataLaporan("show_data_laporan")
    }

    private fun showDataLaporan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                dataLaporan.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonObject = JSONObject(response)
                    var  frm = HashMap<String,String>()
                    frm.put("nama_kegiatan",jsonObject.getString("nama_kegiatan"))
                    frm.put("uang_masuk",jsonObject.getString("uang_masuk"))
                    frm.put("uang_keluar",jsonObject.getString("uang_keluar"))
                    frm.put("sisa",jsonObject.getString("sisa"))
                    frm.put("total",jsonObject.getString("total"))
                    frm.put("keterangan",jsonObject.getString("keterangan"))

                    dataLaporan.add(frm)
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
                        hm.put("mode","show_data_laporan_main")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun showDataKegiatan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.kegiatan,
            Response.Listener { response ->
                dataKegiatan.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("nama_kegiatan",jsonObject.getString("nama_kegiatan"))
                        frm.put("tgl_kegiatan",jsonObject.getString("tgl_kegiatan"))
                        frm.put("jam_kegiatan",jsonObject.getString("jam_kegiatan"))
                        frm.put("nama",jsonObject.getString("nama"))

                        dataKegiatan.add(frm)
                    }
                    kegiatanAdp.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_kegiatan" -> {
                        hm.put("mode","show_data_kegiatan_main")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.profil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama")
                val st2 = jsonObject.getString("foto")
                val st3 = jsonObject.getString("nik")

                hb.usernameHeader.setText(st1)
                Picasso.get().load(st2).into(hb.profilHeader)

                Picasso.get().load(st2).into(b.mainFoto)
                b.mainNama.setText(st1)
                b.mainNik.setText(st3)

                hb.profilHeader.setOnClickListener {
//                    val intent = Intent(this, ImageDetailActivity::class.java)
//                    intent.putExtra("img", foto)
//                    startActivity(intent)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_profil" -> {
                        hm.put("mode","show_profil")
                        hm.put("kd_user", preferences.getString(USER, DEF_USER).toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}