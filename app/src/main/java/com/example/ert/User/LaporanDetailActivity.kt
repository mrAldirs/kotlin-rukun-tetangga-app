package com.example.ert.User

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityLaporanDetailBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.time.LocalDate

class LaporanDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityLaporanDetailBinding
    lateinit var urlClass: UrlClass

    var tgl = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLaporanDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Detail Laporan Keuangan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        val tanggalHariIni = LocalDate.now()
        tgl = tanggalHariIni.toString()
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
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("uang_masuk")
                val st2 = jsonObject.getString("uang_keluar")
                val st3 = jsonObject.getString("tgl_laporan")
                val st4 = jsonObject.getString("sisa")
                val st5 = jsonObject.getString("keterangan")
                val st6 = jsonObject.getString("nama_kegiatan")
                val st7 = jsonObject.getString("total")
                val st9 = jsonObject.getString("img_bayar")

                Picasso.get().load(st9).into(b.detailImage)
                b.detailUangMasuk.setText("Rp."+st1)
                b.detailUangKeluar.setText("Rp."+st2)
                b.detailTgl.setText(st3)
                b.detailSisa.setText("Rp."+st4)
                b.detailKeterangan.setText(st5)
                b.detailKegiatan.setText(st6)
                b.detailTotal.setText("Rp."+st7)
                b.btnEdit.visibility = View.GONE
                b.btnHapus.visibility = View.GONE
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
                        hm.put("kd_laporan", paket?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}