package com.example.ert.Admin

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityLaporanEditBinding
import org.json.JSONObject

class LaporanEditActivity : AppCompatActivity() {
    private lateinit var b: ActivityLaporanEditBinding

    lateinit var urlClass: UrlClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLaporanEditBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Edit Laporan Keuangan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        detail("detail")

        b.btnResult.setOnClickListener {
            val plus = b.edtUangMasuk.text.toString().toInt()
            val min = b.edtUangKeluar.text.toString().toInt()
            val result = plus - min
            b.edtTotal.setText(result.toString())
        }

        b.btnSend.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin mengedit laporan keuangan ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    edit("edit")
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
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

    private fun detail(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("uang_masuk")
                val st2 = jsonObject.getString("uang_keluar")
                val st4 = jsonObject.getString("sisa")
                val st5 = jsonObject.getString("keterangan")
                val st6 = jsonObject.getString("nama_kegiatan")

                b.edtUangMasuk.setText(st1)
                b.edtUangKeluar.setText(st2)
                b.edtTotal.setText(st4)
                b.edtKeterangan.setText(st5)
                b.edtKegiatan.setText(st6)
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

    fun edit(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil mengedit laporan keuangan!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "edit"->{
                        hm.put("mode","edit")
                        hm.put("kd_laporan", paket?.getString("kode").toString())
                        hm.put("uang_masuk", b.edtUangMasuk.text.toString())
                        hm.put("uang_keluar", b.edtUangKeluar.text.toString())
                        hm.put("sisa", b.edtTotal.text.toString())
                        hm.put("keterangan", b.edtKeterangan.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}