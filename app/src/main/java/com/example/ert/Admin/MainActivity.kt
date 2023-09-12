package com.example.ert.Admin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.LoginActivity
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityMainAdminBinding
import com.example.ert.databinding.NavHeaderBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var b : ActivityMainAdminBinding
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var hb: NavHeaderBinding

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val USER = "kd_user"
    val DEF_USER = ""
    val NAMA = "nama"
    val DEF_NAMA = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainAdminBinding.inflate(layoutInflater)
        hb = NavHeaderBinding.bind(b.navView.getHeaderView(0))
        setContentView(b.root)
        supportActionBar?.setTitle("Dashboard Admin")

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        toggle = ActionBarDrawerToggle(this, b.drawerLayout, R.string.open, R.string.close)
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_homeAdmin -> {
                    b.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_userAdmin -> {
                    startActivity(Intent(this, PenggunaActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_insertKegiatan -> {
                    startActivity(Intent(this, KegiatanTambahActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_kegiatanAdmin -> {
                    startActivity(Intent(this, KegiatanActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_keuanganAdmin -> {
                    startActivity(Intent(this, LaporanActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_keluhanAdmin -> {
                    startActivity(Intent(this, KeluhanActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                }
                R.id.nav_logoutAdmin -> {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.warning)
                        .setTitle("Logout")
                        .setMessage("Apakah Anda ingin keluar aplikasi?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                            val prefEditor = preferences.edit()
                            prefEditor.putString(USER,null)
                            prefEditor.putString(NAMA,null)
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

    override fun onStart() {
        super.onStart()
//        setLineChartData()
        showDataLaporan("laporan_chart")
        showDataKeuangan("laporan_chart_keuangan")
        showProfil("show_profil")
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.profil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama")
                val st2 = jsonObject.getString("foto")

                hb.usernameHeader.setText(st1)
                hb.subHeader.visibility = View.GONE
                Picasso.get().load(st2).into(hb.profilHeader)

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
                        hm.put("kd_user",preferences.getString(USER, DEF_USER).toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun showDataLaporan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                val jsonArray = JSONArray(response)
                val entries = mutableListOf<Entry>()
                val xvalue = ArrayList<String>()
                for (i in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(i)
                    val x = jsonObject.getString("y")
                    val y = jsonObject.getString("x")
                    val index = (i).toFloat()

                    entries.add(Entry(x.toFloat(), index.toInt()))
                    xvalue.add(y)
                }

                drawChart(entries, xvalue)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "laporan_chart" -> {
                        hm.put("mode","laporan_chart")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun showDataKeuangan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                val jsonArray = JSONArray(response)
                val entries1 = mutableListOf<Entry>()
                val entries2 = mutableListOf<Entry>()
                val xvalue = ArrayList<String>()
                for (i in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(i)
                    val x = jsonObject.getString("x")
                    val y1 = jsonObject.getString("y1")
                    val y2 = jsonObject.getString("y2")
                    val index = (i).toFloat()

                    entries1.add(Entry(y1.toFloat(), index.toInt()))
                    entries2.add(Entry(y2.toFloat(), index.toInt()))
                    xvalue.add(x)
                }

                drawChartKeuangan(entries1, entries2, xvalue)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "laporan_chart_keuangan" -> {
                        hm.put("mode","laporan_chart_keuangan")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun drawChart(entries: List<Entry>, xvalue: List<String>) {
        val dataSet = LineDataSet(entries, "Total Sisa Kas")
        dataSet.color = resources.getColor(R.color.colorAccent)
        dataSet.valueTextColor = Color.BLACK
        dataSet.circleRadius = 0f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = resources.getColor(R.color.colorPrimaryDark)
        dataSet.fillAlpha = 30

        val lineData = LineData(xvalue, dataSet)
        b.chartLine.data = lineData
        b.chartLine.setBackgroundColor(resources.getColor(R.color.white))
        b.chartLine.animateXY(3000, 3000)
    }

    private fun drawChartKeuangan(entries1: List<Entry>, entries2: List<Entry>, xvalue: List<String>) {
        val dataSet1 = LineDataSet(entries1, "Uang Masuk")
        dataSet1.color = resources.getColor(R.color.colorAccent)
        dataSet1.valueTextColor = Color.BLACK

        val dataSet2 = LineDataSet(entries2, "Uang Keluar")
        dataSet2.color = Color.RED
        dataSet2.valueTextColor = Color.BLACK

        val finaldataset = ArrayList<LineDataSet>()
        finaldataset.add(dataSet1)
        finaldataset.add(dataSet2)

        val lineData = LineData(xvalue, finaldataset as List<ILineDataSet>?)
        b.chartLineKeuangan.data = lineData
        b.chartLineKeuangan.setBackgroundColor(resources.getColor(R.color.white))
        b.chartLineKeuangan.animateXY(3000, 3000)
    }

    /*
    fun setLineChartData() {
        val xvalue = ArrayList<String>()
        xvalue.add("11.00 AM")
        xvalue.add("12.00 AM")
        xvalue.add("1.00 AM")
        xvalue.add("3.00 PM")
        xvalue.add("7.00 PM")

        val lineentry = ArrayList<Entry>();
        lineentry.add(Entry(210f, 0))
        lineentry.add(Entry(50f, 1))
        lineentry.add(Entry(60f, 2))
        lineentry.add(Entry(30f, 3))
        lineentry.add(Entry(10f, 4))

        val lineentry1 = ArrayList<Entry>();
        lineentry1.add(Entry(10f, 0))
        lineentry1.add(Entry(40f, 1))
        lineentry1.add(Entry(30f, 2))
        lineentry1.add(Entry(50f, 3))
        lineentry1.add(Entry(70f, 4))

        val linedataset = LineDataSet(lineentry, "First")
        linedataset.color = resources.getColor(R.color.colorAccent)

        val linedataset1 = LineDataSet(lineentry1, "Second")
        linedataset1.color = resources.getColor(R.color.black)


        linedataset.circleRadius = 0f
        linedataset.setDrawFilled(true)
        linedataset.fillColor = resources.getColor(R.color.colorPrimaryDark)
        linedataset.fillAlpha = 30


        val finaldataset = ArrayList<LineDataSet>()
        finaldataset.add(linedataset)
        finaldataset.add(linedataset1)

        val data = LineData(xvalue, finaldataset as List<ILineDataSet>?)
        b.chartLine.data = data
        b.chartLine.setBackgroundColor(resources.getColor(R.color.white))
        b.chartLine.animateXY(3000, 3000)
    }

     */
}