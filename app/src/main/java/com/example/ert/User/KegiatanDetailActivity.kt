package com.example.ert.User

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ert.Adapter.AdapterDokumentasi
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
    lateinit var albAdapter : AdapterDokumentasi

    var tgl = ""
    var kd = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityKegiatanDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Detail Kegiatan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tanggalHariIni = LocalDate.now()
        tgl = tanggalHariIni.toString()

        urlClass = UrlClass()
        albAdapter = AdapterDokumentasi(dataAlb, this)
        b.recycleView.layoutManager = GridLayoutManager(this, 2)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration = GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
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

    fun delete(kode: String){
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

    fun detail(mode: String, kode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.kegiatan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_kegiatan")
                val st2 = jsonObject.getString("jam_kegiatan")
                val st3 = jsonObject.getString("tgl_kegiatan")
                val st4 = jsonObject.getString("nama")
                val st5 = jsonObject.getString("tempat_kegiatan")
                val st6 = jsonObject.getString("kd_kegiatan")

                kd = st6
                b.detailNamaKegiatan.setText(st1)
                b.detailJam.setText(st2)
                b.detailTgl.setText(st3)
                b.detailNama.setText(st4)
                b.detailTempat.setText(st5)
                b.btnEdit.visibility = View.GONE
                b.btnHapus.visibility = View.GONE
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server!", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "detail" -> {
                        hm.put("mode","detail")
                        hm.put("kd_kegiatan", kode)
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