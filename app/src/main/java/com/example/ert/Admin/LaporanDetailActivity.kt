package com.example.ert.Admin

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
                val st8 = jsonObject.getString("kd_laporan")
                val st9 = jsonObject.getString("img_bayar")

                Picasso.get().load(st9).into(b.detailImage)
                b.detailUangMasuk.setText("Rp."+st1)
                b.detailUangKeluar.setText("Rp."+st2)
                b.detailTgl.setText(st3)
                b.detailSisa.setText("Rp."+st4)
                b.detailKeterangan.setText(st5)
                b.detailKegiatan.setText(st6)
                b.detailTotal.setText("Rp."+st7)

                if (tgl > st3) {
                    b.btnHapus.setOnClickListener {
                        AlertDialog.Builder(this)
                            .setTitle("Informasi!")
                            .setIcon(R.drawable.warning)
                            .setMessage("Anda tidak dapat menghapus laporan keuangan ini dikarenakan tanggal sudah terlewat!")
                            .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                                null
                            })
                            .show()
                        true
                    }
                } else {
                    b.btnHapus.setOnClickListener {
                        AlertDialog.Builder(this)
                            .setTitle("Peringatan!")
                            .setIcon(R.drawable.warning)
                            .setMessage("Apakah Anda yakin ingin menghapus laporan keuangan ini?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                delete("delete", st8)
                            })
                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                                null
                            })
                            .show()
                        true
                    }
                }

                b.btnEdit.setOnClickListener {
                    val intent = Intent(it.context, LaporanEditActivity::class.java)
                    intent.putExtra("kode", st8.toString())
                    it.context.startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
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
                        hm.put("kd_laporan", paket?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun delete(mode: String, kd: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.laporan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil menghapus kegiatan RT!", Toast.LENGTH_SHORT).show()
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
                    "delete"->{
                        hm.put("mode","delete")
                        hm.put("kd_laporan", kd)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}