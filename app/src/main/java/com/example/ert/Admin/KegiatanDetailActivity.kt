package com.example.ert.Admin

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.Adapter.AdapterDokumentasiAdmin
import com.example.ert.R
import com.example.ert.UrlClass
import com.example.ert.databinding.ActivityKegiatanDetailBinding
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

class KegiatanDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityKegiatanDetailBinding
    lateinit var urlClass: UrlClass

    val dataAlb = mutableListOf<HashMap<String,String>>()
    lateinit var albAdapter : AdapterDokumentasiAdmin

    var tgl = ""
    var kd = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKegiatanDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Detail Kegiatan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        val tanggalHariIni = LocalDate.now()
        tgl = tanggalHariIni.toString()

        albAdapter = AdapterDokumentasiAdmin(dataAlb, this)
        b.recycleView.layoutManager = GridLayoutManager(this, 2)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration =
            KegiatanDetailActivity.GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
        b.recycleView.addItemDecoration(itemDecoration)

        b.recycleView.adapter = albAdapter

        b.btnInsertDokumentasi.setOnClickListener {
            var frag = DokumentasiInsertFragment()

            val bundle = Bundle()
            bundle.putString("kode", kd)
            frag.arguments = bundle

            frag.show(supportFragmentManager, "")
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

    override fun onStart() {
        super.onStart()
        var paket : Bundle? = intent.extras
        detail("detail", paket?.getString("kode").toString())
        showData(paket?.getString("kode").toString())
    }

    fun showData(kode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.dokumentasi,
            Response.Listener { response ->
                dataAlb.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("foto_dokumentasi",jsonObject.getString("foto_dokumentasi"))
                        frm.put("kd_dokumentasi",jsonObject.getString("kd_dokumentasi"))
                        frm.put("img_dokumentasi",jsonObject.getString("img_dokumentasi"))

                        dataAlb.add(frm)
                    }
                    albAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode","show_dokumentasi")
                hm.put("kd_kegiatan", kode)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun detail(mode: String, kode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.kegiatan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_kegiatan")
                val st2 = jsonObject.getString("jam_kegiatan")
                val st3 = jsonObject.getString("tgl_kegiatan")
                val st4 = jsonObject.getString("nama")
                val st5 = jsonObject.getString("kd_kegiatan")
                val st6 = jsonObject.getString("tempat_kegiatan")

                kd = st5
                b.detailNamaKegiatan.setText(st1)
                b.detailJam.setText(st2)
                b.detailTgl.setText(st3)
                b.detailNama.setText(st4)
                b.detailTempat.setText(st6)

                if (tgl > st3) {
                    b.btnEdit.setOnClickListener {
                        AlertDialog.Builder(this)
                            .setTitle("Informasi!")
                            .setIcon(R.drawable.warning)
                            .setMessage("Anda tidak dapat mengedit kegiatan ini dikarenakan kegiatan ini sudah selesai!")
                            .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                                null
                            })
                            .show()
                        true
                    }
                } else if (tgl < st3) {
                    b.btnEdit.setOnClickListener {
                        val intent = Intent(it.context, KegiatanEditActivity::class.java)
                        intent.putExtra("kode", st5.toString())
                        it.context.startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                    }
                } else if (tgl == st3) {
                    b.btnEdit.setOnClickListener {
                        AlertDialog.Builder(this)
                            .setTitle("Informasi!")
                            .setIcon(R.drawable.warning)
                            .setMessage("Kegiatan ini dilaksanakan hari ini, apakah Anda ingin mengeditnya?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                val intent = Intent(it.context, KegiatanEditActivity::class.java)
                                intent.putExtra("kode", st5.toString())
                                it.context.startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
                            })
                            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                                null
                            })
                            .show()
                        true
                    }
                }

                b.btnHapus.setOnClickListener {
                    AlertDialog.Builder(this)
                        .setTitle("Peringatan!")
                        .setIcon(R.drawable.warning)
                        .setMessage("Jika Anda menghapus Kegiatan ini maka Laporan Keuangan yang berkaitan dengan Kegiatan juga akan terhapus, Yakin?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                            delete("delete", st5)
                        })
                        .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                            null
                        })
                        .show()
                    true
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
                        hm.put("kd_kegiatan", paket?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun deleteDokumentasi(kode: String){
        val request = object : StringRequest(
            Method.POST,urlClass.dokumentasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    var paket : Bundle? = intent.extras
                    showData(paket?.getString("kode").toString())
                    Toast.makeText(this, "Berhasil menghapus Dokumentasi!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                hm.put("mode","delete")
                hm.put("kd_dokumentasi", kode)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun delete(mode: String, kd: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.kegiatan,
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
                        hm.put("kd_kegiatan", kd)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Mendapatkan posisi item
            val column = position % spanCount // Mendapatkan kolom saat ini

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 2) * spacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 2) * spacing / spanCount

                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}